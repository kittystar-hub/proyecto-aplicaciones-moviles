package com.example.fundaciontextil.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class EmprendedoraRemote(
    val id: String? = null,
    val rut: String,
    val nombre: String,
    val apellido: String,

    @SerialName("rango_etario")
    val rangoEtario: String? = null,

    val genero: String? = null,
    val whatsapp: String? = null,
    val email: String,

    @SerialName("foto_url")
    val fotoUrl: String? = null,

    val consentimiento: Boolean = false,

    @SerialName("fecha_registro")
    val fechaRegistro: String? = null
)