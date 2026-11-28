package com.example.fundaciontextil.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ClienteInsertRemote(
    val nombre: String,
    val apellido: String,
    val email: String,
    val telefono: String? = null,

    @SerialName("foto_url")
    val fotoUrl: String? = null,

    @SerialName("auth_user_id")
    val authUserId: String
)