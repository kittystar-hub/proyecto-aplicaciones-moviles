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

    fun cargarPerfil() {
        if (_cargando.value) return

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

                if (_emprendimiento.value == null) {
                    _error.value = "No se encontró el emprendimiento asociado"
                }

            } catch (e: Exception) {
                _error.value = "No se pudo cargar el perfil"
            } finally {
                _cargando.value = false
            }
        }
    }
}