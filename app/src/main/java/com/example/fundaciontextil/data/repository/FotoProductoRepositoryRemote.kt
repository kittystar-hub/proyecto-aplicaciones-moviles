package com.example.fundaciontextil.data.repository

import com.example.fundaciontextil.data.model.FotoProductoInsertRemote
import com.example.fundaciontextil.data.model.FotoProductoRemote
import com.example.fundaciontextil.data.remote.SupabaseClient
import io.github.jan.supabase.postgrest.from

class FotoProductoRepositoryRemote {

    private val supabase = SupabaseClient.client

    // Registrar fotografía
    suspend fun registrarFoto(foto: FotoProductoInsertRemote) {
        supabase
            .from("fotos_productos")
            .insert(foto)
    }

    // Obtener fotografías de un emprendimiento
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

    // Obtener fotografía por ID
    suspend fun obtenerFotoPorId(
        id: String
    ): FotoProductoRemote? {
        return supabase
            .from("fotos_productos")
            .select {
                filter {
                    eq("id", id)
                }
            }
            .decodeList<FotoProductoRemote>()
            .firstOrNull()
    }

    // Actualizar referencia de fotografía
    suspend fun actualizarFoto(
        id: String,
        foto: FotoProductoInsertRemote
    ) {
        supabase
            .from("fotos_productos")
            .update(foto) {
                filter {
                    eq("id", id)
                }
            }
    }

    // Eliminar registro de fotografía
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