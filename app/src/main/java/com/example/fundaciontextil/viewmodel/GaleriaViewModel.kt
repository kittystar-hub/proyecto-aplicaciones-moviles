package com.example.fundaciontextil.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fundaciontextil.data.model.FotoProductoRemote
import com.example.fundaciontextil.data.model.FotoProductoInsertRemote
import com.example.fundaciontextil.data.repository.FotoProductoRepositoryRemote
import com.example.fundaciontextil.data.repository.StorageRepositoryRemote
import com.example.fundaciontextil.data.repository.EmprendedoraRepositoryRemote
import com.example.fundaciontextil.data.repository.EmprendimientoRepositoryRemote
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.UUID

class GaleriaViewModel : ViewModel() {

    private val repositorio = FotoProductoRepositoryRemote()
    private val storageRepository = StorageRepositoryRemote()
    private val emprendedoraRepository = EmprendedoraRepositoryRemote()
    private val emprendimientoRepository = EmprendimientoRepositoryRemote()

    private val _fotos =
        MutableStateFlow<List<FotoProductoRemote>>(emptyList())

    val fotos: StateFlow<List<FotoProductoRemote>> =
        _fotos.asStateFlow()

    private val _urlsFotos =
        MutableStateFlow<Map<String, String>>(emptyMap())

    val urlsFotos: StateFlow<Map<String, String>> =
        _urlsFotos.asStateFlow()

    private val _cargando = MutableStateFlow(false)
    val cargando = _cargando.asStateFlow()

    private val _procesando = MutableStateFlow(false)
    val procesando = _procesando.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()

    private var cargaJob: Job? = null

    // Cargar fotografías de un emprendimiento
    fun cargarFotos(idEmprendimiento: String) {
        if (_procesando.value) return

        cargaJob?.cancel()

        cargaJob = viewModelScope.launch {
            _cargando.value = true
            _error.value = null
            _fotos.value = emptyList()
            _urlsFotos.value = emptyMap()

            try {
                actualizarGaleria(idEmprendimiento)

            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _error.value =
                    "No se pudieron cargar las fotografías."
            } finally {
                _cargando.value = false
            }
        }
    }

    // Recuperar registros y generar URLs firmadas
    private suspend fun actualizarGaleria(
        idEmprendimiento: String
    ) {
        val lista = repositorio
            .obtenerFotosPorEmprendimiento(idEmprendimiento)

        val urls = mutableMapOf<String, String>()

        for (foto in lista) {
            val id = foto.id ?: continue

            if (!foto.fotoUrl.startsWith(
                    "emprendimientos/$idEmprendimiento/productos/"
                )
            ) continue

            try {
                urls[id] = storageRepository
                    .obtenerUrlFirmada(foto.fotoUrl)

            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                android.util.Log.e(
                    "SUPABASE_GALERIA",
                    "Error al cargar imagen: ${e.javaClass.simpleName}"
                )
            }
        }

        _fotos.value = lista
        _urlsFotos.value = urls.toMap()
    }

    // Comprobar que el emprendimiento pertenece
    // a la emprendedora autenticada
    private suspend fun verificarPropiedad(
        idEmprendimiento: String
    ) {
        val emprendedora = emprendedoraRepository
            .obtenerEmprendedoraActual()
            ?: error("No hay una emprendedora autenticada.")

        val idEmprendedora = emprendedora.id
            ?: error("Emprendedora no encontrada.")

        val emprendimiento = emprendimientoRepository
            .obtenerPorEmprendedoraId(idEmprendedora)

        check(emprendimiento?.id == idEmprendimiento) {
            "No tienes autorización sobre este emprendimiento."
        }
    }

    // Agregar fotografía
    fun agregarFoto(
        idEmprendimiento: String,
        imagen: ByteArray,
        extension: String
    ) {
        if (_procesando.value || _cargando.value) return

        _error.value = null

        if (imagen.isEmpty()) {
            _error.value = "La imagen está vacía."
            return
        }

        if (imagen.size > 5 * 1024 * 1024) {
            _error.value = "La imagen no puede superar los 5 MB."
            return
        }

        val formato = extension.lowercase()

        if (formato !in listOf("jpg", "jpeg", "png", "webp")) {
            _error.value = "Formato de imagen no permitido."
            return
        }

        viewModelScope.launch {
            _procesando.value = true

            try {
                verificarPropiedad(idEmprendimiento)

                val ruta =
                    "emprendimientos/$idEmprendimiento/productos/" +
                            "${UUID.randomUUID()}.$formato"

                storageRepository.subirImagen(ruta, imagen)

                repositorio.registrarFoto(
                    FotoProductoInsertRemote(
                        emprendimientoId = idEmprendimiento,
                        fotoUrl = ruta
                    )
                )

                actualizarGaleria(idEmprendimiento)

            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _error.value =
                    "No se pudo completar la operación de agregar fotografía."

                android.util.Log.e(
                    "SUPABASE_GALERIA",
                    e.javaClass.simpleName
                )
            } finally {
                _procesando.value = false
            }
        }
    }

    // Eliminar fotografía
    fun eliminarFoto(foto: FotoProductoRemote) {
        if (_procesando.value || _cargando.value) return

        val id = foto.id ?: return

        viewModelScope.launch {
            _procesando.value = true
            _error.value = null

            try {
                val fotoActual = repositorio.obtenerFotoPorId(id)
                    ?: error("Fotografía no encontrada.")

                verificarPropiedad(fotoActual.emprendimientoId)

                val prefijo =
                    "emprendimientos/${fotoActual.emprendimientoId}/productos/"

                check(fotoActual.fotoUrl.startsWith(prefijo)) {
                    "Ruta de fotografía no válida."
                }

                repositorio.eliminarFoto(id)

                try {
                    storageRepository.eliminarImagen(
                        fotoActual.fotoUrl
                    )
                } catch (e: CancellationException) {
                    throw e
                } catch (e: Exception) {
                    _error.value =
                        "Registro eliminado, pero no se pudo eliminar el archivo."
                }

                actualizarGaleria(fotoActual.emprendimientoId)

            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _error.value =
                    "No se pudo completar la operación de eliminar fotografía."
            } finally {
                _procesando.value = false
            }
        }
    }

    fun limpiarError() {
        _error.value = null
    }
}