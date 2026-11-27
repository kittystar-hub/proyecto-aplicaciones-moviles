package com.example.fundaciontextil.data.repository

import com.example.fundaciontextil.data.remote.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email

class AuthRepositoryRemote {

    private val supabase = SupabaseClient.client

    // Registrar usuario
    suspend fun registrarUsuario(
        email: String,
        contrasena: String
    ) {
        supabase.auth.signUpWith(Email) {
            this.email = email
            this.password = contrasena
        }
    }

    // Iniciar sesión
    suspend fun iniciarSesion(
        email: String,
        contrasena: String
    ) {
        supabase.auth.signInWith(Email) {
            this.email = email
            this.password = contrasena
        }
    }

    // Cerrar sesión
    suspend fun cerrarSesion() {
        supabase.auth.signOut()
    }

    // Obtener ID del usuario autenticado
    fun obtenerUsuarioId(): String? {
        return supabase.auth.currentUserOrNull()?.id
    }

    // Verificar si existe una sesión
    fun haySesionActiva(): Boolean {
        return supabase.auth.currentSessionOrNull() != null
    }
}