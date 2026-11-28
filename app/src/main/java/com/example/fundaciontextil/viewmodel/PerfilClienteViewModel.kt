package com.example.fundaciontextil.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fundaciontextil.data.model.ClienteRemote
import com.example.fundaciontextil.data.repository.ClienteRepositoryRemote
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import com.example.fundaciontextil.data.repository.StorageRepositoryRemote
import com.example.fundaciontextil.data.repository.AuthRepositoryRemote
import kotlinx.coroutines.CancellationException
import java.util.UUID

class PerfilClienteViewModel : ViewModel() {

    private val repositorio = ClienteRepositoryRemote()
    private val storageRepository = StorageRepositoryRemote()
    private val authRepository = AuthRepositoryRemote()

    private val _cliente = MutableStateFlow<ClienteRemote?>(null)
    val cliente: StateFlow<ClienteRemote?> = _cliente.asStateFlow()

    private val _cargando = MutableStateFlow(false)
    val cargando: StateFlow<Boolean> = _cargando.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private val _fotoUrl = MutableStateFlow<String?>(null)
    val fotoUrl = _fotoUrl.asStateFlow()

    private val _subiendoFoto = MutableStateFlow(false)
    val subiendoFoto = _subiendoFoto.asStateFlow()

    private val _errorFoto = MutableStateFlow<String?>(null)
    val errorFoto = _errorFoto.asStateFlow()


    fun cargarPerfil() {
        if (_cargando.value || _subiendoFoto.value) return

        viewModelScope.launch {
            _cargando.value = true
            _error.value = null
            _cliente.value = null
            _fotoUrl.value = null
            _errorFoto.value = null

            try {
                val clienteActual = repositorio.obtenerClienteActual()

                if (clienteActual != null) {
                    _cliente.value = clienteActual
                    cargarFoto(clienteActual.fotoUrl)
                } else {
                    _error.value = "No se encontró el perfil del cliente"
                }

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
            _fotoUrl.value =
                storageRepository.obtenerUrlFirmada(ruta)

        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            _errorFoto.value = "No se pudo cargar la fotografía"
        }
    }

    fun subirFoto(imagen: ByteArray, extension: String) {
        if (_subiendoFoto.value || _cargando.value) return

        _errorFoto.value = null

        if (_cliente.value == null) {
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

                val clienteActual = repositorio.obtenerClienteActual()
                    ?: error("No se encontró el cliente")

                val fotoAnterior = clienteActual.fotoUrl

                val nuevaRuta =
                    "perfiles/$usuarioId/${UUID.randomUUID()}.$extension"

                storageRepository.subirImagen(
                    ruta = nuevaRuta,
                    imagen = imagen
                )

                val clienteActualizado =
                    repositorio.actualizarFotoCliente(nuevaRuta)

                _cliente.value = clienteActualizado

                cargarFoto(clienteActualizado.fotoUrl)

                if (!fotoAnterior.isNullOrBlank() &&
                    fotoAnterior != nuevaRuta
                ) {
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
                    "Error al actualizar la fotografía",
                    e
                )

                _errorFoto.value =
                    "Error: ${e.message ?: "Error desconocido"}"
            } finally {
                _subiendoFoto.value = false
            }
        }
    }
}