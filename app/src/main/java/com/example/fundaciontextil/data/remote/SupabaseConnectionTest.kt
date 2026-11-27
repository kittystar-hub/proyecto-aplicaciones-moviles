package com.example.fundaciontextil.data.remote

import android.util.Log
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object SupabaseConnectionTest {

    suspend fun verificarConexion() {
        withContext(Dispatchers.IO) {
            try {
                SupabaseClient.client
                    .from("clientes")
                    .select()

                Log.d(
                    "SupabaseTest",
                    "Petición HTTP completada correctamente"
                )

            } catch (e: Exception) {
                Log.e(
                    "SupabaseTest",
                    "Error al conectar con Supabase: ${e.message}",
                    e
                )
            }
        }
    }
}