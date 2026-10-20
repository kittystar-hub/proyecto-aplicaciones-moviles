package com.example.fundaciontextil.ui.vistas.registro

import com.example.fundaciontextil.data.ChileData

data class RegistroUiState(
    val fotoUri: String? = null,
    // Datos personales
    val rut: String = "",
    val nombre: String = "",
    val rangoEtario: String = "",
    val genero: String = "",
    // Datos del emprendimiento
    val nombreTaller: String = "",
    val descripcion: String = "",
    val region: String = "",
    val comuna: String = "",
    val rubros: Set<String> = emptySet(),
    // Datos de contacto
    val whatsapp: String = "",
    val email: String = "",
    // Casilla de consentimiento legal
    val consentimiento: Boolean = false,
    // Errores centralizados
    val errores: RegistroErrores = RegistroErrores(),
    // Procesos
    val guardando: Boolean = false,
    val guardadoExitoso: Boolean = false,
    val idGuardado: Long? = null,
    val errorGeneral: String? = null
) {
    val comunasDisponibles: List<String>
        get() = ChileData.comunasDe(region)
}

data class RegistroErrores(
    val rut: String? = null,
    val nombre: String? = null,
    val nombreTaller: String? = null,
    val descripcion: String? = null,
    val region: String? = null,
    val comuna: String? = null,
    val rubros: String? = null,
    val whatsapp: String? = null,
    val email: String? = null,
    val consentimiento: String? = null
)