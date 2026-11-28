package com.example.fundaciontextil.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ClienteRemote(
    val id: String? = null,
    val nombre: String,
    val apellido: String,
    val email: String,
    val telefono: String? = null,

    @SerialName("foto_url")
    val fotoUrl: String? = null,

    @SerialName("fecha_registro")
    val fechaRegistro: String? = null
)