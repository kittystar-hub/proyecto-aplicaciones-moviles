package com.example.fundaciontextil.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "fotos_productos",
    foreignKeys = [
        ForeignKey(
            entity = Emprendimiento::class,
            parentColumns = ["id"],
            childColumns = ["idEmprendimiento"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("idEmprendimiento")]
)
data class FotoProducto(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val idEmprendimiento: Long,
    val fotoUri: String,
    val fechaRegistro: Long = System.currentTimeMillis()
)
