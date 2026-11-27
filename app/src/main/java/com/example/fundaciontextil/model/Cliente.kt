package com.example.fundaciontextil.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "clientes",
    indices = [Index(value = ["email"], unique = true)]
)
data class Cliente(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val nombre: String = "",
    val apellido: String = "",
    val email: String = "",
    val contrasenia: String = "",
    val telefono: String = "",
    val fotoUri: String? = null,
    val fechaRegistro: Long = System.currentTimeMillis()
)