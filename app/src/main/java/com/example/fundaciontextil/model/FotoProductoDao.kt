package com.example.fundaciontextil.model

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface FotoProductoDao {

    @Insert
    suspend fun insertar(foto: FotoProducto): Long

    @Query("""
        SELECT * FROM fotos_productos
        WHERE idEmprendimiento = :idEmprendimiento
        ORDER BY fechaRegistro DESC
    """)
    suspend fun listarPorEmprendimiento(
        idEmprendimiento: Long
    ): List<FotoProducto>

    @Delete
    suspend fun eliminar(foto: FotoProducto)
}
