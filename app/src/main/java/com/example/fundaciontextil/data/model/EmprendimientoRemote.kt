package com.example.fundaciontextil.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class EmprendimientoRemote(
    val id: String? = null,

    @SerialName("emprendedora_id")
    val emprendedoraId: String,

    @SerialName("nombre_taller")
    val nombreTaller: String,

    val categoria: String? = null,
    val rubros: String? = null,
    val region: String? = null,
    val comuna: String? = null,
    val ubicacion: String? = null,
    val descripcion: String? = null,

    @SerialName("foto_url")
    val fotoUrl: String? = null,

    @SerialName("fecha_registro")
    val fechaRegistro: String? = null
)