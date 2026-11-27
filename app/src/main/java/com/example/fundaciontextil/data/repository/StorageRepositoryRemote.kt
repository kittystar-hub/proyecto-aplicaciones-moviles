package com.example.fundaciontextil.data.repository

import com.example.fundaciontextil.data.remote.SupabaseClient
import io.github.jan.supabase.storage.storage
import kotlin.time.Duration.Companion.minutes

class StorageRepositoryRemote {

    private val supabase = SupabaseClient.client

    private val bucket = "imagenes-fundacion"

    suspend fun subirImagen(
        ruta: String,
        imagen: ByteArray
    ): String {
        supabase.storage
            .from(bucket)
            .upload(ruta, imagen) {
                upsert = false
            }

        return ruta
    }

    suspend fun obtenerUrlFirmada(
        ruta: String
    ): String {
        return supabase.storage
            .from(bucket)
            .createSignedUrl(
                path = ruta,
                expiresIn = 60.minutes
            )
    }

    suspend fun eliminarImagen(ruta: String) {
        supabase.storage
            .from(bucket)
            .delete(ruta)
    }
}
