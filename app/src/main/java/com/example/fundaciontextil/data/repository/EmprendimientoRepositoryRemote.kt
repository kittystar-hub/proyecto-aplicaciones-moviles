
package com.example.fundaciontextil.data.repository

import com.example.fundaciontextil.data.model.EmprendimientoInsertRemote
import com.example.fundaciontextil.data.model.EmprendimientoRemote
import com.example.fundaciontextil.data.remote.SupabaseClient
import io.github.jan.supabase.postgrest.from

class EmprendimientoRepositoryRemote {

    private val supabase = SupabaseClient.client

    suspend fun registrarEmprendimiento(
        emprendimiento: EmprendimientoInsertRemote
    ) {
        supabase
            .from("emprendimientos")
            .insert(emprendimiento)
    }

    suspend fun obtenerEmprendimientos(): List<EmprendimientoRemote> {
        return supabase
            .from("emprendimientos")
            .select()
            .decodeList<EmprendimientoRemote>()
    }

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
}
