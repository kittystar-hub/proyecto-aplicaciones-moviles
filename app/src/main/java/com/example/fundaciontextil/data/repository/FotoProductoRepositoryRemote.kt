package com.example.fundaciontextil.data.repository

import com.example.fundaciontextil.data.model.FotoProductoInsertRemote
import com.example.fundaciontextil.data.model.FotoProductoRemote
import com.example.fundaciontextil.data.remote.SupabaseClient
import io.github.jan.supabase.postgrest.from

class FotoProductoRepositoryRemote {

    private val supabase = SupabaseClient.client

    suspend fun registrarFoto(
        foto: FotoProductoInsertRemote
    ) {
        supabase
            .from("fotos_productos")
            .insert(foto)
    }

    suspend fun obtenerFotosPorEmprendimiento(
        emprendimientoId: String
    ): List<FotoProductoRemote> {
        return supabase
            .from("fotos_productos")
            .select {
                filter {
                    eq("emprendimiento_id", emprendimientoId)
                }
            }
            .decodeList<FotoProductoRemote>()
    }

    suspend fun eliminarFoto(id: String) {
        supabase
            .from("fotos_productos")
            .delete {
                filter {
                    eq("id", id)
                }
            }
    }
}