package com.example.fundaciontextil.data.repository

import com.example.fundaciontextil.data.remote.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import com.example.fundaciontextil.data.model.ClienteInsertRemote
import com.example.fundaciontextil.data.model.EmprendedoraInsertRemote
import kotlinx.serialization.json.JsonObject

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

    // Registrar cliente completo
    suspend fun registrarCliente(
        email: String,
        contrasena: String,
        cliente: ClienteInsertRemote
    ) {
        supabase.auth.signUpWith(Email) {
            this.email = email
            this.password = contrasena

            data = buildJsonObject {
                put("rol", "cliente")
                put("nombre", cliente.nombre)
                put("apellido", cliente.apellido)

                cliente.telefono?.let {
                    put("telefono", it)
                }

                cliente.fotoUrl?.let {
                    put("foto_url", it)
                }
            }
        }
    }

    // Registrar emprendedora completa
    suspend fun registrarEmprendedora(
        email: String,
        contrasena: String,
        emprendedora: EmprendedoraInsertRemote,
        nombreTaller: String,
        categoria: String,
        rubros: String,
        region: String,
        comuna: String,
        descripcion: String
    ) {
        supabase.auth.signUpWith(Email) {
            this.email = email
            this.password = contrasena

            data = buildJsonObject {
                put("rol", "emprendedora")
                put("rut", emprendedora.rut)
                put("nombre", emprendedora.nombre)
                put("apellido", emprendedora.apellido)
                put("consentimiento", emprendedora.consentimiento)

                emprendedora.rangoEtario?.let {
                    put("rango_etario", it)
                }

                emprendedora.genero?.let {
                    put("genero", it)
                }

                emprendedora.whatsapp?.let {
                    put("whatsapp", it)
                }

                put("nombre_taller", nombreTaller)
                put("categoria", categoria)
                put("rubros", rubros)
                put("region", region)
                put("comuna", comuna)
                put("ubicacion", comuna)
                put("descripcion", descripcion)
            }
        }
    }

    suspend fun iniciarSesionConRol(
        email: String,
        contrasena: String,
        rolEsperado: String
    ) {
        iniciarSesion(email, contrasena)

        try {
            val perfil = PerfilRepositoryRemote().obtenerPerfilActual()

            check(perfil?.rol == rolEsperado) {
                "La cuenta no corresponde al tipo de usuario seleccionado."
            }
        } catch (e: Exception) {
            cerrarSesion()
            throw e
        }
    }
}