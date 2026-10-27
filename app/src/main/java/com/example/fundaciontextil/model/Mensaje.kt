package com.example.fundaciontextil.model

import java.util.UUID

/**
 * Representa un mensaje dentro del servicio de comunicación
 * entre el usuario/cliente y la emprendedora de Fundación Textil Circular.
 */
data class Mensaje(
    val id: String = UUID.randomUUID().toString(),
    val contenido: String,
    val esUsuario: Boolean, // true = mensaje del cliente; false = respuesta de la emprendedora
    val hora: String,
    val emprendimientoId: Long? = null
)
