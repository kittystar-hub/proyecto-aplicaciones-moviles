package com.example.fundaciontextil.viewmodel

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.fundaciontextil.model.AppDatabase
import com.example.fundaciontextil.model.FotoProducto
import com.example.fundaciontextil.model.FotoProductoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream

class GaleriaViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val repositorio = FotoProductoRepository(
        AppDatabase.get(application).fotoProductoDao()
    )

    private val _fotos = MutableStateFlow<List<FotoProducto>>(emptyList())
    val fotos: StateFlow<List<FotoProducto>> = _fotos.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    fun cargarFotos(idEmprendimiento: Long) {
        viewModelScope.launch {
            try {
                _fotos.value =
                    repositorio.listarPorEmprendimiento(idEmprendimiento)
                _error.value = null
            } catch (e: Exception) {
                _error.value = "No se pudieron cargar las fotografías."
            }
        }
    }

    fun agregarFoto(idEmprendimiento: Long, uri: Uri) {
        viewModelScope.launch {
            try {
                val contexto = getApplication<Application>()
                val carpeta = File(contexto.filesDir, "fotos_productos")

                if (!carpeta.exists()) {
                    carpeta.mkdirs()
                }

                val archivo = File(
                    carpeta,
                    "producto_${idEmprendimiento}_${System.currentTimeMillis()}.jpg"
                )

                val input = contexto.contentResolver.openInputStream(uri)
                    ?: throw IllegalStateException("No se pudo abrir la imagen.")

                input.use { entrada ->
                    FileOutputStream(archivo).use { salida ->
                        entrada.copyTo(salida)
                    }
                }

                repositorio.guardar(
                    FotoProducto(
                        idEmprendimiento = idEmprendimiento,
                        fotoUri = archivo.absolutePath
                    )
                )

                cargarFotos(idEmprendimiento)
                _error.value = null

            } catch (e: Exception) {
                _error.value = "No se pudo agregar la fotografía."
            }
        }
    }

    fun eliminarFoto(foto: FotoProducto) {
        viewModelScope.launch {
            try {
                repositorio.eliminar(foto)

                val archivo = File(foto.fotoUri)
                if (archivo.exists()) {
                    archivo.delete()
                }

                cargarFotos(foto.idEmprendimiento)
                _error.value = null

            } catch (e: Exception) {
                _error.value = "No se pudo eliminar la fotografía."
            }
        }
    }
}