package com.example.fundaciontextil.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "emprendedoras")
data class Emprendedora(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val fotoUri: String?,
    val rut: String,
    val nombre: String,
    val rangoEtario: String,
    val genero: String,
    val nombreTaller: String,
    val descripcion: String,
    val region: String,
    val comuna: String,
    val rubros: String,
    val whatsapp: String,
    val email: String,
    val consentimiento: Boolean,
    val fechaRegistro: Long = System.currentTimeMillis()
)