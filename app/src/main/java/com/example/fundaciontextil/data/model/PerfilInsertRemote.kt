package com.example.fundaciontextil.data.model

import kotlinx.serialization.Serializable

@Serializable
data class PerfilInsertRemote(
    val id: String,
    val rol: String
)