package com.example.fundaciontextil.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PerfilRemote(
    val id: String,
    val rol: String,
    @SerialName("fecha_registro")
    val fechaRegistro: String? = null
)