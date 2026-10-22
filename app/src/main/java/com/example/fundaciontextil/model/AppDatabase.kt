package com.example.fundaciontextil.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Emprendedora::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun emprendedoraDao(): EmprendedoraDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun get(context: Context): AppDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "fundacion_textil.db"
                ).build().also { INSTANCE = it }
            }
    }
}