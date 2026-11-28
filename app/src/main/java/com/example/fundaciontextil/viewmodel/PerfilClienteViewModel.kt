
package com.example.fundaciontextil.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fundaciontextil.data.model.ClienteRemote
import com.example.fundaciontextil.data.repository.ClienteRepositoryRemote
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PerfilClienteViewModel : ViewModel() {

    private val repositorio = ClienteRepositoryRemote()

    private val _cliente = MutableStateFlow<ClienteRemote?>(null)
    val cliente: StateFlow<ClienteRemote?> = _cliente.asStateFlow()

    private val _cargando = MutableStateFlow(false)
    val cargando: StateFlow<Boolean> = _cargando.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    fun cargarPerfil() {
        if (_cargando.value) return

        viewModelScope.launch {
            _cargando.value = true
            _error.value = null
            _cliente.value = null

            try {
                val clienteActual = repositorio.obtenerClienteActual()

                if (clienteActual != null) {
                    _cliente.value = clienteActual
                } else {
                    _error.value = "No se encontró el perfil del cliente"
                }

            } catch (e: Exception) {
                _error.value = "No se pudo cargar el perfil"
            } finally {
                _cargando.value = false
            }
        }
    }
}
