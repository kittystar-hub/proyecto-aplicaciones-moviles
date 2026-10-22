package com.example.fundaciontextil.model

class EmprendimientoRepository(private val dao: EmprendimientoDao) {

    suspend fun guardar(emprendimiento: Emprendimiento): Long = dao.insertar(emprendimiento)

    suspend fun obtener(id: Long): Emprendimiento? = dao.obtenerPorId(id)

    suspend fun listar(): List<Emprendimiento> = dao.listarTodos()
}