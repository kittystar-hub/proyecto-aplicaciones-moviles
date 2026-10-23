
package com.example.fundaciontextil.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.fundaciontextil.model.AppDatabase
import com.example.fundaciontextil.model.Emprendimiento
import com.example.fundaciontextil.model.EmprendimientoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CatalogoViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val repositorio = EmprendimientoRepository(
        AppDatabase.get(application).emprendimientoDao()
    )

    private val _emprendimientos =
        MutableStateFlow<List<Emprendimiento>>(emptyList())

    val emprendimientos: StateFlow<List<Emprendimiento>> =
        _emprendimientos.asStateFlow()

    // Registros de ejemplo para probar el catálogo.
    private val emprendimientosEjemplo = listOf(
        Emprendimiento(
            id = -1L,
            nombre = "Taller Upcycling Valparaíso",
            categoria = "Suprarreciclaje (Upcycling)",
            ubicacion = "Valparaíso",
            descripcion = "Prendas de mezclilla reciclada intervenidas a mano con diseño sustentable y retazos textiles."
        ),
        Emprendimiento(
            id = -2L,
            nombre = "Reparaciones Textiles Sandra",
            categoria = "Reparación y Modistería",
            ubicacion = "Santiago",
            descripcion = "Arreglos, composturas y transformación de prendas para alargar su ciclo de vida."
        ),
        Emprendimiento(
            id = -3L,
            nombre = "Moda Circular Biobío",
            categoria = "Segunda Mano y Talleres",
            ubicacion = "Concepción",
            descripcion = "Venta y educación comunitaria sobre reutilización de telas y economía circular textil."
        ),
        Emprendimiento(
            id = -4L,
            nombre = "Hilos del Huasco",
            categoria = "Confección y Upcycling",
            ubicacion = "Atacama",
            descripcion = "Confección de bolsos y accesorios a partir de descartes textiles industriales."
        )
    )

    init {
        cargarEmprendimientos()
    }

    fun cargarEmprendimientos() {
        viewModelScope.launch {
            try {
                val emprendimientosGuardados = repositorio.listar()

                _emprendimientos.value =
                    emprendimientosEjemplo + emprendimientosGuardados
            } catch (e: Exception) {
                // Si falla la consulta, conservamos los ejemplos.
                _emprendimientos.value = emprendimientosEjemplo
            }
        }
    }
}
