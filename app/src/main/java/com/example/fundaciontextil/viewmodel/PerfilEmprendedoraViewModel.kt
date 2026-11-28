package com.example.fundaciontextil.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fundaciontextil.data.model.EmprendedoraRemote
import com.example.fundaciontextil.data.model.EmprendimientoRemote
import com.example.fundaciontextil.data.repository.EmprendedoraRepositoryRemote
import com.example.fundaciontextil.data.repository.EmprendimientoRepositoryRemote
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import com.example.fundaciontextil.data.model.EmprendimientoInsertRemote
import kotlinx.coroutines.CancellationException
import com.example.fundaciontextil.data.repository.StorageRepositoryRemote
import com.example.fundaciontextil.data.repository.AuthRepositoryRemote
import java.util.UUID

class PerfilEmprendedoraViewModel : ViewModel() {

    private val emprendedoraRepository =
        EmprendedoraRepositoryRemote()

    private val emprendimientoRepository =
        EmprendimientoRepositoryRemote()

    private val storageRepository = StorageRepositoryRemote()
    private val authRepository = AuthRepositoryRemote()

    private val _emprendedora =
        MutableStateFlow<EmprendedoraRemote?>(null)

    val emprendedora: StateFlow<EmprendedoraRemote?> =
        _emprendedora.asStateFlow()

    private val _emprendimiento =
        MutableStateFlow<EmprendimientoRemote?>(null)

    val emprendimiento: StateFlow<EmprendimientoRemote?> =
        _emprendimiento.asStateFlow()

    private val _cargando = MutableStateFlow(false)
    val cargando: StateFlow<Boolean> = _cargando.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private val _guardando = MutableStateFlow(false)
    val guardando = _guardando.asStateFlow()

    private val _errorRegistro = MutableStateFlow<String?>(null)
    val errorRegistro = _errorRegistro.asStateFlow()

    private val _fotoUrl = MutableStateFlow<String?>(null)
    val fotoUrl = _fotoUrl.asStateFlow()

    private val _subiendoFoto = MutableStateFlow(false)
    val subiendoFoto = _subiendoFoto.asStateFlow()

    private val _errorFoto = MutableStateFlow<String?>(null)
    val errorFoto = _errorFoto.asStateFlow()
    private val _fotoEmprendimientoUrl = MutableStateFlow<String?>(null)
    val fotoEmprendimientoUrl = _fotoEmprendimientoUrl.asStateFlow()

    private val _subiendoFotoEmprendimiento = MutableStateFlow(false)
    val subiendoFotoEmprendimiento = _subiendoFotoEmprendimiento.asStateFlow()

    private val _errorFotoEmprendimiento = MutableStateFlow<String?>(null)
    val errorFotoEmprendimiento = _errorFotoEmprendimiento.asStateFlow()


    fun cargarPerfil() {
        if (
            _cargando.value ||
            _guardando.value ||
            _subiendoFoto.value ||
            _subiendoFotoEmprendimiento.value
        ) return

        viewModelScope.launch {
            _cargando.value = true
            _error.value = null
            _emprendedora.value = null
            _emprendimiento.value = null
            _fotoUrl.value = null
            _errorFoto.value = null
            _fotoEmprendimientoUrl.value = null
            _errorFotoEmprendimiento.value = null

            try {
                val emprendedoraActual =
                    emprendedoraRepository.obtenerEmprendedoraActual()

                if (emprendedoraActual == null) {
                    _error.value = "No se encontró el perfil"
                    return@launch
                }

                _emprendedora.value = emprendedoraActual

                val emprendedoraId = emprendedoraActual.id

                if (emprendedoraId == null) {
                    _error.value = "No se encontró el identificador del perfil"
                    return@launch
                }

                _emprendimiento.value =
                    emprendimientoRepository.obtenerPorEmprendedoraId(
                        emprendedoraId
                    )

                cargarFoto(emprendedoraActual.fotoUrl)
                cargarFotoEmprendimiento(_emprendimiento.value?.fotoUrl)

            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _error.value = "No se pudo cargar el perfil"
            } finally {
                _cargando.value = false
            }
        }
    }

    private suspend fun cargarFoto(ruta: String?) {
        _fotoUrl.value = null

        if (ruta.isNullOrBlank()) return

        try {
            _fotoUrl.value = storageRepository.obtenerUrlFirmada(ruta)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            _errorFoto.value = "No se pudo cargar la fotografía"
        }
    }

    fun subirFoto(imagen: ByteArray, extension: String) {
        if (
            _subiendoFoto.value ||
            _cargando.value ||
            _guardando.value ||
            _subiendoFotoEmprendimiento.value
        ) return

        _errorFoto.value = null

        if (_emprendedora.value == null) {
            _errorFoto.value = "Debes cargar tu perfil primero"
            return
        }

        if (imagen.isEmpty()) {
            _errorFoto.value = "La imagen seleccionada está vacía"
            return
        }

        if (imagen.size > 5 * 1024 * 1024) {
            _errorFoto.value = "La imagen no puede superar los 5 MB"
            return
        }

        if (extension !in listOf("jpg", "jpeg", "png", "webp")) {
            _errorFoto.value = "Formato de imagen no permitido"
            return
        }

        _subiendoFoto.value = true

        viewModelScope.launch {
            try {
                val usuarioId = authRepository.obtenerUsuarioId()
                    ?: error("No hay una sesión activa")

                val emprendedoraActual =
                    emprendedoraRepository.obtenerEmprendedoraActual()
                        ?: error("No se encontró el perfil")

                val fotoAnterior = emprendedoraActual.fotoUrl

                val nuevaRuta =
                    "perfiles/$usuarioId/${UUID.randomUUID()}.$extension"

                storageRepository.subirImagen(
                    ruta = nuevaRuta,
                    imagen = imagen
                )

                val emprendedoraActualizada =
                    emprendedoraRepository.actualizarFotoEmprendedora(
                        nuevaRuta
                    )

                _emprendedora.value = emprendedoraActualizada

                cargarFoto(emprendedoraActualizada.fotoUrl)

                if (!fotoAnterior.isNullOrBlank() && fotoAnterior != nuevaRuta) {
                    try {
                        storageRepository.eliminarImagen(fotoAnterior)
                    } catch (e: CancellationException) {
                        throw e
                    } catch (e: Exception) {
                        _errorFoto.value =
                            "Foto actualizada, pero no se pudo eliminar la anterior"
                    }
                }

            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                android.util.Log.e(
                    "SUPABASE_FOTO",
                    "Error al actualizar fotografía: ${e.javaClass.simpleName}"
                )

                _errorFoto.value = "No se pudo actualizar la fotografía"
            } finally {
                _subiendoFoto.value = false
            }
        }
    }

    private suspend fun cargarFotoEmprendimiento(ruta: String?) {
        _fotoEmprendimientoUrl.value = null
        _errorFotoEmprendimiento.value = null

        if (ruta.isNullOrBlank()) return

        try {
            _fotoEmprendimientoUrl.value =
                storageRepository.obtenerUrlFirmada(ruta)

        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            _errorFotoEmprendimiento.value =
                "No se pudo cargar la fotografía del emprendimiento"
        }
    }


    fun subirFotoEmprendimiento(
        imagen: ByteArray,
        extension: String
    ) {
        if (
            _cargando.value ||
            _guardando.value ||
            _subiendoFoto.value ||
            _subiendoFotoEmprendimiento.value
        ) return

        _errorFotoEmprendimiento.value = null

        val emprendimientoActual = _emprendimiento.value
        val emprendimientoId = emprendimientoActual?.id

        if (emprendimientoActual == null || emprendimientoId == null) {
            _errorFotoEmprendimiento.value =
                "No tienes un emprendimiento registrado"
            return
        }

        if (imagen.isEmpty()) {
            _errorFotoEmprendimiento.value =
                "La imagen seleccionada está vacía"
            return
        }

        if (imagen.size > 5 * 1024 * 1024) {
            _errorFotoEmprendimiento.value =
                "La imagen no puede superar los 5 MB"
            return
        }

        if (extension !in listOf("jpg", "jpeg", "png", "webp")) {
            _errorFotoEmprendimiento.value =
                "Formato de imagen no permitido"
            return
        }

        _subiendoFotoEmprendimiento.value = true

        viewModelScope.launch {
            try {
                val fotoAnterior = emprendimientoActual.fotoUrl

                val nuevaRuta =
                    "emprendimientos/$emprendimientoId/${UUID.randomUUID()}.$extension"

                storageRepository.subirImagen(
                    ruta = nuevaRuta,
                    imagen = imagen
                )

                val emprendimientoActualizado =
                    emprendimientoRepository.actualizarFotoEmprendimiento(
                        id = emprendimientoId,
                        ruta = nuevaRuta
                    )

                _emprendimiento.value = emprendimientoActualizado

                cargarFotoEmprendimiento(
                    emprendimientoActualizado.fotoUrl
                )

                if (!fotoAnterior.isNullOrBlank() &&
                    fotoAnterior != nuevaRuta
                ) {
                    try {
                        storageRepository.eliminarImagen(fotoAnterior)
                    } catch (e: CancellationException) {
                        throw e
                    } catch (e: Exception) {
                        _errorFotoEmprendimiento.value =
                            "Foto actualizada, pero no se pudo eliminar la anterior"
                    }
                }

            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                android.util.Log.e(
                    "SUPABASE_FOTO",
                    "Error en fotografía del emprendimiento: ${e.javaClass.simpleName}"
                )

                _errorFotoEmprendimiento.value =
                    "No se pudo actualizar la fotografía del emprendimiento"

            } finally {
                _subiendoFotoEmprendimiento.value = false
            }
        }
    }

    fun registrarEmprendimiento(
        nombreTaller: String,
        categoria: String,
        rubros: String,
        region: String,
        comuna: String,
        descripcion: String
    ) {
        if (
            _guardando.value ||
            _cargando.value ||
            _subiendoFoto.value ||
            _subiendoFotoEmprendimiento.value
        ) return

        _errorRegistro.value = null

        if (_error.value != null || _emprendedora.value == null) {
            _errorRegistro.value = "Debes cargar tu perfil primero"
            return
        }

        if (_emprendimiento.value != null) {
            _errorRegistro.value = "Ya tienes un emprendimiento registrado"
            return
        }

        if (
            nombreTaller.isBlank() ||
            categoria.isBlank() ||
            rubros.isBlank() ||
            region.isBlank() ||
            comuna.isBlank() ||
            descripcion.isBlank()
        ) {
            _errorRegistro.value = "Debes completar todos los campos"
            return
        }

        _guardando.value = true

        viewModelScope.launch {
            try {
                val emprendedoraActual =
                    emprendedoraRepository.obtenerEmprendedoraActual()
                        ?: error("Perfil no encontrado")

                val emprendedoraId =
                    emprendedoraActual.id
                        ?: error("Identificador no encontrado")

                val nuevoEmprendimiento = EmprendimientoInsertRemote(
                    emprendedoraId = emprendedoraId,
                    nombreTaller = nombreTaller.trim(),
                    categoria = categoria.trim(),
                    rubros = rubros.trim(),
                    region = region.trim(),
                    comuna = comuna.trim(),
                    ubicacion = comuna.trim(),
                    descripcion = descripcion.trim(),
                    estado = "pendiente"
                )

                _emprendimiento.value =
                    emprendimientoRepository.registrarEmprendimiento(
                        nuevoEmprendimiento
                    )

            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _errorRegistro.value =
                    "No se pudo registrar el emprendimiento. Actualiza tu perfil e inténtalo nuevamente."
            } finally {
                _guardando.value = false
            }
        }
    }

    fun limpiarErrorRegistro() {
        _errorRegistro.value = null
    }
}