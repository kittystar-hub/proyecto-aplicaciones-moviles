package com.example.fundaciontextil.model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface EmprendimientoDao {

    @Insert
    suspend fun insertar(emprendimiento: Emprendimiento): Long

    @Query("SELECT * FROM emprendimientos WHERE id = :id")
    suspend fun obtenerPorId(id: Long): Emprendimiento?

    @Query("SELECT * FROM emprendimientos ORDER BY fechaRegistro DESC")
    suspend fun listarTodos(): List<Emprendimiento>

    @Query("SELECT * FROM emprendimientos WHERE rut = :rut AND contrasenia = :contrasenia LIMIT 1")
    suspend fun login(rut: String, contrasenia: String): Emprendimiento?
}