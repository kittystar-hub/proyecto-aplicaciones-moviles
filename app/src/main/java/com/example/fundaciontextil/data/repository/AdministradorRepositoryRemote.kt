package com.example.fundaciontextil.data.repository

import com.example.fundaciontextil.data.model.ClienteRemote
import com.example.fundaciontextil.data.model.EmprendedoraRemote
import com.example.fundaciontextil.data.model.EmprendimientoRemote
import com.example.fundaciontextil.data.model.PerfilRemote
import com.example.fundaciontextil.data.remote.SupabaseClient
import io.github.jan.supabase.postgrest.from

class AdministradorRepositoryRemote {

    private val supabase = SupabaseClient.client

    suspend fun obtenerEmprendimientos(): List<EmprendimientoRemote> {
        return supabase.from("emprendimientos")
            .select()
            .decodeList<EmprendimientoRemote>()
    }

    suspend fun obtenerClientes(): List<ClienteRemote> {
        return supabase.from("clientes")
            .select()
            .decodeList<ClienteRemote>()
    }

    suspend fun obtenerEmprendedoras(): List<EmprendedoraRemote> {
        return supabase.from("emprendedoras")
            .select()
            .decodeList<EmprendedoraRemote>()
    }

    suspend fun obtenerAdministradores(): List<PerfilRemote> {
        return supabase.from("perfiles").select {
            filter {
                eq("rol", "administrador")
            }
        }.decodeList<PerfilRemote>()
    }

    suspend fun aprobarEmprendimiento(id: String) {
        supabase.from("emprendimientos").update(
            {
                set("estado", "aprobado")
                set("motivo_rechazo", null as String?)
            }
        ) {
            filter {
                eq("id", id)
                eq("estado", "pendiente")
            }
            select()
        }.decodeSingle<EmprendimientoRemote>()
    }

    suspend fun rechazarEmprendimiento(
        id: String,
        motivo: String
    ) {
        require(motivo.isNotBlank()) {
            "Debes indicar un motivo de rechazo."
        }

        supabase.from("emprendimientos").update(
            {
                set("estado", "rechazado")
                set("motivo_rechazo", motivo.trim())
            }
        ) {
            filter {
                eq("id", id)
                eq("estado", "pendiente")
            }
            select()
        }.decodeSingle<EmprendimientoRemote>()
    }

    suspend fun eliminarEmprendimiento(id: String) {
        supabase.from("emprendimientos").delete {
            filter {
                eq("id", id)
            }
            select()
        }.decodeSingle<EmprendimientoRemote>()
    }
}