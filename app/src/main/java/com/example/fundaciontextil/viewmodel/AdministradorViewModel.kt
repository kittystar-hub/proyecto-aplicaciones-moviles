package com.example.fundaciontextil.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fundaciontextil.data.model.ClienteRemote
import com.example.fundaciontextil.data.model.EmprendedoraRemote
import com.example.fundaciontextil.data.model.EmprendimientoRemote
import com.example.fundaciontextil.data.model.PerfilRemote
import com.example.fundaciontextil.data.repository.AdministradorRepositoryRemote
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AdministradorViewModel : ViewModel() {

    private val repositorio = AdministradorRepositoryRemote()

    private val _solicitudes =
        MutableStateFlow<List<EmprendimientoRemote>>(emptyList())
    val solicitudes = _solicitudes.asStateFlow()

    private val _emprendimientos =
        MutableStateFlow<List<EmprendimientoRemote>>(emptyList())
    val emprendimientos = _emprendimientos.asStateFlow()

    private val _clientes =
        MutableStateFlow<List<ClienteRemote>>(emptyList())
    val clientes = _clientes.asStateFlow()

    private val _emprendedoras =
        MutableStateFlow<List<EmprendedoraRemote>>(emptyList())
    val emprendedoras = _emprendedoras.asStateFlow()

    private val _administradores =
        MutableStateFlow<List<PerfilRemote>>(emptyList())
    val administradores = _administradores.asStateFlow()

    private val _cargando = MutableStateFlow(false)
    val cargando = _cargando.asStateFlow()

    private val _procesando = MutableStateFlow(false)
    val procesando = _procesando.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()

    private val _mensaje = MutableStateFlow<String?>(null)
    val mensaje = _mensaje.asStateFlow()

    fun cargarDatos() {
        if (_cargando.value || _procesando.value) return

        _cargando.value = true
        _error.value = null
        _mensaje.value = null

        _solicitudes.value = emptyList()
        _emprendimientos.value = emptyList()
        _clientes.value = emptyList()
        _emprendedoras.value = emptyList()
        _administradores.value = emptyList()

        viewModelScope.launch {
            try {
                actualizarDatos()
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _error.value = "No se pudieron cargar los datos del panel."
            } finally {
                _cargando.value = false
            }
        }
    }

    private suspend fun actualizarDatos() {
        val todos = repositorio.obtenerEmprendimientos()
        val clientes = repositorio.obtenerClientes()
        val emprendedoras = repositorio.obtenerEmprendedoras()
        val administradores = repositorio.obtenerAdministradores()

        _solicitudes.value = todos.filter {
            it.estado == "pendiente"
        }

        _emprendimientos.value = todos.filter {
            it.estado == "aprobado"
        }

        _clientes.value = clientes
        _emprendedoras.value = emprendedoras
        _administradores.value = administradores
    }

    fun aprobarEmprendimiento(id: String) {
        ejecutarOperacion(
            mensajeExito = "Emprendimiento aprobado correctamente."
        ) {
            repositorio.aprobarEmprendimiento(id)
        }
    }

    fun rechazarEmprendimiento(id: String, motivo: String) {
        if (motivo.isBlank()) {
            _error.value = "Debes indicar un motivo de rechazo."
            return
        }

        ejecutarOperacion(
            mensajeExito = "Solicitud rechazada correctamente."
        ) {
            repositorio.rechazarEmprendimiento(id, motivo)
        }
    }

    fun eliminarEmprendimiento(id: String) {
        ejecutarOperacion(
            mensajeExito = "Emprendimiento eliminado correctamente."
        ) {
            repositorio.eliminarEmprendimiento(id)
        }
    }

    private fun ejecutarOperacion(
        mensajeExito: String,
        operacion: suspend () -> Unit
    ) {
        if (_procesando.value || _cargando.value) return

        _procesando.value = true
        _error.value = null
        _mensaje.value = null

        viewModelScope.launch {
            try {
                operacion()

                _mensaje.value = mensajeExito

                try {
                    actualizarDatos()
                } catch (e: CancellationException) {
                    throw e
                } catch (e: Exception) {
                    _error.value =
                        "Operación realizada, pero no se pudieron actualizar los datos."
                }

            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _error.value = "No se pudo confirmar el resultado de la operación."
            } finally {
                _procesando.value = false
            }
        }
    }

    fun limpiarMensaje() {
        _mensaje.value = null
    }

    fun limpiarError() {
        _error.value = null
    }
}