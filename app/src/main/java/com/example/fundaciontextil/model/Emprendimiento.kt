package com.example.fundaciontextil.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "emprendimientos")
data class Emprendimiento(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val nombre: String = "",
    val categoria: String = "",
    val ubicacion: String = "",
    val descripcion: String = "",

    // Campos de formulario de registro
    val fotoUri: String? = null,
    val rut: String = "",
    val rangoEtario: String = "",
    val genero: String = "",
    val nombreTaller: String = "",
    val region: String = "",
    val comuna: String = "",
    val rubros: String = "",
    val whatsapp: String = "",
    val email: String = "",
    val consentimiento: Boolean = false,
    val fechaRegistro: Long = System.currentTimeMillis(),
    val password: String = ""
)