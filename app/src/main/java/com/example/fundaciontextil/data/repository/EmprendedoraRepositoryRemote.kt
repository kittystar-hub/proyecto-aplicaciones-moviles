package com.example.fundaciontextil.data.repository

import com.example.fundaciontextil.data.model.EmprendedoraInsertRemote
import com.example.fundaciontextil.data.model.EmprendedoraRemote
import com.example.fundaciontextil.data.remote.SupabaseClient
import io.github.jan.supabase.postgrest.from

class EmprendedoraRepositoryRemote {

    private val supabase = SupabaseClient.client

    suspend fun registrarEmprendedora(
        emprendedora: EmprendedoraInsertRemote
    ) {
        supabase
            .from("emprendedoras")
            .insert(emprendedora)
    }

    suspend fun obtenerEmprendedoras(): List<EmprendedoraRemote> {
        return supabase
            .from("emprendedoras")
            .select()
            .decodeList<EmprendedoraRemote>()
    }

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
}
