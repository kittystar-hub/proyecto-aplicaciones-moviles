package com.example.fundaciontextil.viewmodel

import androidx.lifecycle.ViewModel
import com.example.fundaciontextil.model.Emprendimiento
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class CatalogoViewModel : ViewModel() {

    private val _emprendimientos = MutableStateFlow<List<Emprendimiento>>(emptyList())
    val emprendimientos: StateFlow<List<Emprendimiento>> = _emprendimientos.asStateFlow()

    init {
        cargarEmprendimientos()
    }

    private fun cargarEmprendimientos() {
        _emprendimientos.value = listOf(
            Emprendimiento(
                id = 1,
                nombre = "Taller Upcycling Valparaíso",
                categoria = "Suprarreciclaje (Upcycling)",
                ubicacion = "Valparaíso",
                descripcion = "Prendas de mezclilla reciclada intervenidas a mano con diseño sustentable y retazos textiles."
            ),
            Emprendimiento(
                id = 2,
                nombre = "Reparaciones Textiles Sandra",
                categoria = "Reparación y Modistería",
                ubicacion = "Santiago",
                descripcion = "Arreglos, composturas y transformación de prendas para alargar su ciclo de vida."
            ),
            Emprendimiento(
                id = 3,
                nombre = "Moda Circular Biobío",
                categoria = "Segunda Mano y Talleres",
                ubicacion = "Concepción",
                descripcion = "Venta y educación comunitaria sobre reutilización de telas y economía circular textil."
            ),
            Emprendimiento(
                id = 4,
                nombre = "Hilos del Huasco",
                categoria = "Confección y Upcycling",
                ubicacion = "Atacama",
                descripcion = "Confección de bolsos y accesorios a partir de descartes textiles industriales."
            )
        )
    }

    fun agregarEmprendimiento(emprendimiento: Emprendimiento) {
        val listaActual = _emprendimientos.value.toMutableList()
        listaActual.add(0, emprendimiento)
        _emprendimientos.value = listaActual
    }
}
