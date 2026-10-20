package com.example.fundaciontextil.data.repository

import com.example.fundaciontextil.data.local.Emprendedora
import com.example.fundaciontextil.data.local.EmprendedoraDao

class EmprendedoraRepository(private val dao: EmprendedoraDao) {

    suspend fun guardar(emprendedora: Emprendedora): Long = dao.insertar(emprendedora)

    suspend fun obtener(id: Long): Emprendedora? = dao.obtenerPorId(id)

    suspend fun listar(): List<Emprendedora> = dao.listarTodas()
}