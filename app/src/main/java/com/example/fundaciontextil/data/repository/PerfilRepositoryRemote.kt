package com.example.fundaciontextil.data.repository

import com.example.fundaciontextil.data.model.PerfilInsertRemote
import com.example.fundaciontextil.data.model.PerfilRemote
import com.example.fundaciontextil.data.remote.SupabaseClient
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.auth.auth

class PerfilRepositoryRemote {

    private val supabase = SupabaseClient.client

    // Registrar perfil
    suspend fun registrarPerfil(perfil: PerfilInsertRemote) {
        supabase
            .from("perfiles")
            .insert(perfil)
    }

    // Obtener todos los perfiles
    suspend fun obtenerPerfiles(): List<PerfilRemote> {
        return supabase
            .from("perfiles")
            .select()
            .decodeList<PerfilRemote>()
    }

    // Obtener perfil por ID de usuario
    suspend fun obtenerPerfilPorId(id: String): PerfilRemote? {
        return supabase
            .from("perfiles")
            .select {
                filter {
                    eq("id", id)
                }
            }
            .decodeList<PerfilRemote>()
            .firstOrNull()
    }

    // Actualizar perfil
    suspend fun actualizarPerfil(
        id: String,
        perfil: PerfilInsertRemote
    ) {
        supabase
            .from("perfiles")
            .update(perfil) {
                filter {
                    eq("id", id)
                }
            }
    }

    // Eliminar perfil
    suspend fun eliminarPerfil(id: String) {
        supabase
            .from("perfiles")
            .delete {
                filter {
                    eq("id", id)
                }
            }
    }

    // Obtener rol del usuario
    suspend fun obtenerRolUsuario(id: String): String? {
        return obtenerPerfilPorId(id)?.rol
    }

    // Obtener perfil del usuario autenticado
    suspend fun obtenerPerfilActual(): PerfilRemote? {
        val usuarioId = SupabaseClient.client.auth.currentUserOrNull()?.id
            ?: return null

        return obtenerPerfilPorId(usuarioId)
    }
}