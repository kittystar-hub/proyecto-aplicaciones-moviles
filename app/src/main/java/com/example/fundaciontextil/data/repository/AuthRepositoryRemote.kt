package com.example.fundaciontextil.data.repository

import com.example.fundaciontextil.data.remote.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import com.example.fundaciontextil.data.model.ClienteInsertRemote
import com.example.fundaciontextil.data.repository.ClienteRepositoryRemote
import com.example.fundaciontextil.data.model.EmprendedoraInsertRemote
import com.example.fundaciontextil.data.repository.EmprendedoraRepositoryRemote

class AuthRepositoryRemote {

    private val supabase = SupabaseClient.client
    private val clienteRepository = ClienteRepositoryRemote()
    private val emprendedoraRepository = EmprendedoraRepositoryRemote()

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

    // Registrar cliente completo
    suspend fun registrarCliente(
        email: String,
        contrasena: String,
        cliente: ClienteInsertRemote
    ) {
        registrarUsuario(
            email = email,
            contrasena = contrasena,
            rol = "cliente"
        )

        val usuarioId = obtenerUsuarioId()
            ?: throw Exception(
                "Cuenta creada. Es necesario iniciar sesión para completar el registro."
            )

        clienteRepository.registrarCliente(
            cliente.copy(
                email = email,
                authUserId = usuarioId
            )
        )
    }
    // Registrar emprendedora completa
    suspend fun registrarEmprendedora(
        email: String,
        contrasena: String,
        emprendedora: EmprendedoraInsertRemote
    ) {
        registrarUsuario(
            email = email,
            contrasena = contrasena,
            rol = "emprendedora"
        )

        val usuarioId = obtenerUsuarioId()
            ?: throw Exception(
                "Cuenta creada. Es necesario iniciar sesión para completar el registro."
            )

        emprendedoraRepository.registrarEmprendedora(
            emprendedora.copy(
                email = email,
                authUserId = usuarioId
            )
        )
    }
}