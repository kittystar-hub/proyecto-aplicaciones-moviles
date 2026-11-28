package com.example.fundaciontextil.data.repository

import com.example.fundaciontextil.data.model.EmprendedoraInsertRemote
import com.example.fundaciontextil.data.model.EmprendedoraRemote
import com.example.fundaciontextil.data.remote.SupabaseClient
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.auth.auth

class EmprendedoraRepositoryRemote {

    private val supabase = SupabaseClient.client

    // Obtener la emprendedora autenticada
    suspend fun obtenerEmprendedoraActual(): EmprendedoraRemote? {
        val usuarioId = supabase.auth.currentUserOrNull()?.id
            ?: return null

        return supabase
            .from("emprendedoras")
            .select {
                filter {
                    eq("auth_user_id", usuarioId)
                }
            }
            .decodeList<EmprendedoraRemote>()
            .firstOrNull()
    }

    // Registrar emprendedora
    suspend fun registrarEmprendedora(
        emprendedora: EmprendedoraInsertRemote
    ) {
        supabase
            .from("emprendedoras")
            .insert(emprendedora)
    }

    // Obtener todas las emprendedoras
    suspend fun obtenerEmprendedoras(): List<EmprendedoraRemote> {
        return supabase
            .from("emprendedoras")
            .select()
            .decodeList<EmprendedoraRemote>()
    }

    // Obtener emprendedora por ID
    suspend fun obtenerEmprendedoraPorId(
        id: String
    ): EmprendedoraRemote? {
        return supabase
            .from("emprendedoras")
            .select {
                filter {
                    eq("id", id)
                }
            }
            .decodeList<EmprendedoraRemote>()
            .firstOrNull()
    }

    // Obtener emprendedora por RUT
    suspend fun obtenerEmprendedoraPorRut(
        rut: String
    ): EmprendedoraRemote? {
        return supabase
            .from("emprendedoras")
            .select {
                filter {
                    eq("rut", rut)
                }
            }
            .decodeList<EmprendedoraRemote>()
            .firstOrNull()
    }

    // Actualizar emprendedora
    suspend fun actualizarEmprendedora(
        id: String,
        emprendedora: EmprendedoraInsertRemote
    ) {
        supabase
            .from("emprendedoras")
            .update(emprendedora) {
                filter {
                    eq("id", id)
                }
            }
    }

    // Eliminar emprendedora
    suspend fun eliminarEmprendedora(id: String) {
        supabase
            .from("emprendedoras")
            .delete {
                filter {
                    eq("id", id)
                }
            }
    }
}