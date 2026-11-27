package com.example.fundaciontextil.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FotoProductoInsertRemote(
    @SerialName("emprendimiento_id")
    val emprendimientoId: String,
    @SerialName("foto_url")
    val fotoUrl: String
)
