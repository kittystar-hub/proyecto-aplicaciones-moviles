package com.example.fundaciontextil.model

class ClienteRepository(private val dao: ClienteDao) {

    suspend fun guardar(cliente: Cliente): Long = dao.insertar(cliente)

    suspend fun obtener(id: Long): Cliente? = dao.obtenerPorId(id)

    suspend fun obtenerPorEmail(email: String): Cliente? = dao.obtenerPorEmail(email)

    suspend fun login(email: String, contrasenia: String): Cliente? =
        dao.login(email, contrasenia)
}