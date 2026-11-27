package com.example.fundaciontextil.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(
    entities = [
        Emprendimiento::class,
        FotoProducto::class,
        Cliente::class
    ],
    version = 4,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun emprendimientoDao(): EmprendimientoDao
    abstract fun fotoProductoDao(): FotoProductoDao
    abstract fun clienteDao(): ClienteDao

    companion object {
        private val MIGRATION_1_2: Migration = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS fotos_productos (
                        id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        idEmprendimiento INTEGER NOT NULL,
                        fotoUri TEXT NOT NULL,
                        fechaRegistro INTEGER NOT NULL,
                        FOREIGN KEY(idEmprendimiento)
                            REFERENCES emprendimientos(id)
                            ON UPDATE NO ACTION
                            ON DELETE CASCADE
                    )
                    """.trimIndent()
                )
                db.execSQL(
                    """
                    CREATE INDEX IF NOT EXISTS
                    index_fotos_productos_idEmprendimiento
                    ON fotos_productos(idEmprendimiento)
                    """.trimIndent()
                )
            }
        }

        private val MIGRATION_2_3: Migration = object : Migration(2, 3) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    "ALTER TABLE emprendimientos ADD COLUMN contrasenia TEXT NOT NULL DEFAULT ''"
                )
            }
        }

        private val MIGRATION_3_4: Migration = object : Migration(3, 4) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS clientes (
                        id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        nombre TEXT NOT NULL,
                        apellido TEXT NOT NULL,
                        email TEXT NOT NULL,
                        contrasenia TEXT NOT NULL,
                        telefono TEXT NOT NULL,
                        fotoUri TEXT,
                        fechaRegistro INTEGER NOT NULL
                    )
                    """.trimIndent()
                )
                // Índice único para que no se puedan registrar dos clientes con el mismo correo
                db.execSQL(
                    "CREATE UNIQUE INDEX IF NOT EXISTS index_clientes_email ON clientes(email)"
                )
            }
        }

        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun get(context: Context): AppDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "fundacion_textil.db"
                )
                    .addMigrations(MIGRATION_1_2, MIGRATION_2_3, MIGRATION_3_4)
                    .build()
                    .also { INSTANCE = it }
            }
    }
}