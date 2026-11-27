package com.example.fundaciontextil.model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ClienteDao {

    @Insert
    suspend fun insertar(cliente: Cliente): Long

    @Query("SELECT * FROM clientes WHERE id = :id")
    suspend fun obtenerPorId(id: Long): Cliente?

    @Query("SELECT * FROM clientes WHERE email = :email LIMIT 1")
    suspend fun obtenerPorEmail(email: String): Cliente?

    @Query("SELECT * FROM clientes WHERE email = :email AND contrasenia = :contrasenia LIMIT 1")
    suspend fun login(email: String, contrasenia: String): Cliente?
}