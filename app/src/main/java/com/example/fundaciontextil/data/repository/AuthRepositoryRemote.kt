package com.example.fundaciontextil.data.repository

import com.example.fundaciontextil.data.remote.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

class AuthRepositoryRemote {

    private val supabase = SupabaseClient.client

    // Registrar usuario
    suspend fun registrarUsuario(
        email: String,
        contrasena: String,
        rol: String
    ) {
        require(rol == "cliente" || rol == "emprendedora") {
            "El rol seleccionado no está permitido para el registro público."
        }

        supabase.auth.signUpWith(Email) {
            this.email = email
            this.password = contrasena

            data = buildJsonObject {
                put("rol", rol)
            }
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

    // Obtener usuario autenticado
    fun obtenerUsuarioActual(): String? {
        return obtenerUsuarioId()
    }
}