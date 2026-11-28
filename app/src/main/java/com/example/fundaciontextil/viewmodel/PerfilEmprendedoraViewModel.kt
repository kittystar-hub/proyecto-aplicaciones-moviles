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

class PerfilEmprendedoraViewModel : ViewModel() {

    private val emprendedoraRepository =
        EmprendedoraRepositoryRemote()

    private val emprendimientoRepository =
        EmprendimientoRepositoryRemote()

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

    fun cargarPerfil() {
        if (_cargando.value || _guardando.value) return

        viewModelScope.launch {
            _cargando.value = true
            _error.value = null
            _emprendedora.value = null
            _emprendimiento.value = null

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

            } catch (e: Exception) {
                _error.value = "No se pudo cargar el perfil"
            } finally {
                _cargando.value = false
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
        if (_guardando.value || _cargando.value) return

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