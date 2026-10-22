package com.example.fundaciontextil.model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface EmprendedoraDao {

    @Insert
    suspend fun insertar(emprendedora: Emprendedora): Long

    @Query("SELECT * FROM emprendedoras WHERE id = :id")
    suspend fun obtenerPorId(id: Long): Emprendedora?

    @Query("SELECT * FROM emprendedoras ORDER BY fechaRegistro DESC")
    suspend fun listarTodas(): List<Emprendedora>
}
