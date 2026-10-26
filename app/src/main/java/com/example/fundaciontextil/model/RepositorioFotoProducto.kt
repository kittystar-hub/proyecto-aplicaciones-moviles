package com.example.fundaciontextil.model

class FotoProductoRepository(
    private val dao: FotoProductoDao
) {

    suspend fun guardar(foto: FotoProducto): Long {
        return dao.insertar(foto)
    }

    suspend fun listarPorEmprendimiento(
        idEmprendimiento: Long
    ): List<FotoProducto> {
        return dao.listarPorEmprendimiento(idEmprendimiento)
    }

    suspend fun eliminar(foto: FotoProducto) {
        dao.eliminar(foto)
    }
}
