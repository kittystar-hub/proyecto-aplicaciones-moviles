package com.example.fundaciontextil.model

/**
 * Representa el resumen de una conversación dentro de la bandeja de entrada
 * de mensajería de Fundación Textil Circular.
 */
data class ConversacionItem(
    val id: String,
    val nombreEmprendimiento: String,
    val ultimoMensaje: String,
    val horaUltimoMensaje: String,
    val mensajesNoLeidos: Int = 0,
    val emprendimientoId: Long? = null,
    val rubro: String = ""
)
