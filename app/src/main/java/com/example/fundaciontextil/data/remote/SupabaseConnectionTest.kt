package com.example.fundaciontextil.data.remote

import android.util.Log
import com.example.fundaciontextil.BuildConfig

object SupabaseConnectionTest {

    fun verificarConfiguracion() {
        try {
            require(BuildConfig.SUPABASE_URL.isNotBlank()) {
                "Falta configurar SUPABASE_URL"
            }

            require(BuildConfig.SUPABASE_KEY.isNotBlank()) {
                "Falta configurar SUPABASE_KEY"
            }

            // Fuerza la inicialización del cliente.
            val cliente = SupabaseClient.client

            Log.d(
                "SupabaseTest",
                "Cliente Supabase inicializado correctamente"
            )

        } catch (e: Exception) {
            Log.e(
                "SupabaseTest",
                "Error al inicializar Supabase",
                e
            )
        }
    }
}