
package com.example.fundaciontextil.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fundaciontextil.data.model.EmprendimientoRemote
import com.example.fundaciontextil.data.repository.EmprendimientoRepositoryRemote
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CatalogoViewModel : ViewModel() {

    private val repositorio = EmprendimientoRepositoryRemote()

    private val _emprendimientos =
        MutableStateFlow<List<EmprendimientoRemote>>(emptyList())

    val emprendimientos: StateFlow<List<EmprendimientoRemote>> =
        _emprendimientos.asStateFlow()

    private val _cargando = MutableStateFlow(false)
    val cargando: StateFlow<Boolean> = _cargando.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    init {
        cargarEmprendimientos()
    }

    fun cargarEmprendimientos() {
        if (_cargando.value) return

        viewModelScope.launch {
            _cargando.value = true
            _error.value = null

            try {
                _emprendimientos.value =
                    repositorio.obtenerEmprendimientosAprobados()

            } catch (e: Exception) {
                _error.value = "No se pudieron cargar los emprendimientos"

            } finally {
                _cargando.value = false
            }
        }
    }
}