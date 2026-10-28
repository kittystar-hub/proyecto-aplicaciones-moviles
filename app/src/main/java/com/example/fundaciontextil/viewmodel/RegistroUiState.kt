package com.example.fundaciontextil.viewmodel

data class RegistroUiState(
    // Foto
    val fotoUri: String? = null,
    // Datos personales
    val rut: String = "",
    val nombre: String = "",
    val rangoEtario: String = "",
    val genero: String = "",
    // Emprendimiento
    val nombreTaller: String = "",
    val descripcion: String = "",
    val region: String = "",
    val comuna: String = "",
    val rubros: Set<String> = emptySet(),
    // Contacto
    val whatsapp: String = "",
    val email: String = "",
    //contraseña
    val contrasenia: String = "",
    val confirmarContrasenia: String = "",
    // Legal
    val consentimiento: Boolean = false,
    // Errores centralizados
    val errores: RegistroErrores = RegistroErrores(),
    // Proceso
    val guardando: Boolean = false,
    val guardadoExitoso: Boolean = false,
    val idGuardado: Long? = null,
    val errorGeneral: String? = null,
    // Lista de comunas (calculada en viewmodel)
    val comunasDisponibles: List<String> = emptyList()
)

data class RegistroErrores(
    val rut: String? = null,
    val nombre: String? = null,
    val rangoEtario: String? = null,
    val genero: String? = null,
    val nombreTaller: String? = null,
    val descripcion: String? = null,
    val region: String? = null,
    val comuna: String? = null,
    val rubros: String? = null,
    val whatsapp: String? = null,
    val email: String? = null,
    val contrasenia: String? = null,
    val confirmarContrasenia: String? = null,
    val consentimiento: String? = null
)