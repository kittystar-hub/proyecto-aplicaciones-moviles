package com.example.fundaciontextil.viewmodel

data class RegistroClienteUiState(
    val nombre: String = "",
    val apellido: String = "",
    val email: String = "",
    val contrasenia: String = "",
    val confirmarContrasenia: String = "",
    val telefono: String = "",
    val fotoUri: String? = null,
    val nombreError: String? = null,
    val apellidoError: String? = null,
    val emailError: String? = null,
    val contraseniaError: String? = null,
    val confirmarContraseniaError: String? = null,
    val telefonoError: String? = null,
    val guardando: Boolean = false,
    val guardadoExitoso: Boolean = false,
    val mensajeExito: String? = null
)