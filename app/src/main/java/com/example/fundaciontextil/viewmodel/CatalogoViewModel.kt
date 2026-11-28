package com.example.fundaciontextil.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fundaciontextil.data.model.EmprendimientoRemote
import com.example.fundaciontextil.data.repository.EmprendimientoRepositoryRemote
import com.example.fundaciontextil.data.repository.StorageRepositoryRemote
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CatalogoViewModel : ViewModel() {

    private val repositorio = EmprendimientoRepositoryRemote()
    private val storageRepository = StorageRepositoryRemote()

    private val _emprendimientos =
        MutableStateFlow<List<EmprendimientoRemote>>(emptyList())

    val emprendimientos: StateFlow<List<EmprendimientoRemote>> =
        _emprendimientos.asStateFlow()

    private val _fotosEmprendimientos =
        MutableStateFlow<Map<String, String>>(emptyMap())

    val fotosEmprendimientos: StateFlow<Map<String, String>> =
        _fotosEmprendimientos.asStateFlow()

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
            _fotosEmprendimientos.value = emptyMap()

            try {
                val lista =
                    repositorio.obtenerEmprendimientosAprobados()

                _emprendimientos.value = lista

                cargarFotografias(lista)

            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _error.value =
                    "No se pudieron cargar los emprendimientos"
            } finally {
                _cargando.value = false
            }
        }
    }

    private suspend fun cargarFotografias(
        emprendimientos: List<EmprendimientoRemote>
    ) {
        val fotografias = mutableMapOf<String, String>()

        for (emprendimiento in emprendimientos) {
            val id = emprendimiento.id ?: continue
            val ruta = emprendimiento.fotoUrl

            if (ruta.isNullOrBlank()) continue

            try {
                val url = storageRepository.obtenerUrlFirmada(ruta)

                fotografias[id] = url

            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                android.util.Log.e(
                    "SUPABASE_CATALOGO",
                    "No se pudo cargar fotografía: ${e.javaClass.simpleName}"
                )
            }
        }

        _fotosEmprendimientos.value = fotografias.toMap()
    }
}