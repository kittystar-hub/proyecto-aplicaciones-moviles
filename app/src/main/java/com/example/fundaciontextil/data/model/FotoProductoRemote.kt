package com.example.fundaciontextil.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FotoProductoRemote(
    val id: String? = null,

    @SerialName("emprendimiento_id")
    val emprendimientoId: String,

    @SerialName("foto_url")
    val fotoUrl: String,

    @SerialName("fecha_registro")
    val fechaRegistro: String? = null
)