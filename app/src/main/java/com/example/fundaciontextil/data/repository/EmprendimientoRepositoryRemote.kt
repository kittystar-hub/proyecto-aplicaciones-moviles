package com.example.fundaciontextil.data.repository

import com.example.fundaciontextil.data.model.EmprendimientoInsertRemote
import com.example.fundaciontextil.data.model.EmprendimientoRemote
import com.example.fundaciontextil.data.remote.SupabaseClient
import io.github.jan.supabase.postgrest.from

class EmprendimientoRepositoryRemote {

    private val supabase = SupabaseClient.client

    // Registrar emprendimiento
    suspend fun registrarEmprendimiento(
        emprendimiento: EmprendimientoInsertRemote
    ) {
        supabase
            .from("emprendimientos")
            .insert(emprendimiento)
    }

    // Obtener todos los emprendimientos
    suspend fun obtenerEmprendimientos(): List<EmprendimientoRemote> {
        return supabase
            .from("emprendimientos")
            .select()
            .decodeList<EmprendimientoRemote>()
    }

    // Obtener emprendimiento por ID
    suspend fun obtenerEmprendimientoPorId(
        id: String
    ): EmprendimientoRemote? {
        return supabase
            .from("emprendimientos")
            .select {
                filter {
                    eq("id", id)
                }
            }
            .decodeList<EmprendimientoRemote>()
            .firstOrNull()
    }

    // Obtener emprendimiento por emprendedora
    suspend fun obtenerEmprendimientoPorEmprendedora(
        emprendedoraId: String
    ): EmprendimientoRemote? {
        return supabase
            .from("emprendimientos")
            .select {
                filter {
                    eq("emprendedora_id", emprendedoraId)
                }
            }
            .decodeList<EmprendimientoRemote>()
            .firstOrNull()
    }

    // Actualizar emprendimiento
    suspend fun actualizarEmprendimiento(
        id: String,
        emprendimiento: EmprendimientoInsertRemote
    ) {
        supabase
            .from("emprendimientos")
            .update(emprendimiento) {
                filter {
                    eq("id", id)
                }
            }
    }

    // Eliminar emprendimiento
    suspend fun eliminarEmprendimiento(id: String) {
        supabase
            .from("emprendimientos")
            .delete {
                filter {
                    eq("id", id)
                }
            }
    }
}