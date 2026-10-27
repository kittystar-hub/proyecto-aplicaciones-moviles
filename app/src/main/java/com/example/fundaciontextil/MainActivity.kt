package com.example.fundaciontextil

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.fundaciontextil.ui.theme.FundacionTextilTheme
import com.example.fundaciontextil.ui.vistas.bienvenida.PantallaBienvenida
import com.example.fundaciontextil.ui.vistas.catalogo.PantallaCatalogo
import com.example.fundaciontextil.viewmodel.CatalogoViewModel
import com.example.fundaciontextil.ui.RegistroScreen
import com.example.fundaciontextil.ui.PantallaConfirmacion
import com.example.fundaciontextil.model.Emprendimiento
import com.example.fundaciontextil.ui.vistas.catalogo.PantallaDetalleEmprendimiento
import com.example.fundaciontextil.viewmodel.GaleriaViewModel
import com.example.fundaciontextil.ui.vistas.mensajeria.PantallaMensajeria
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.animation.core.tween

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            FundacionTextilTheme {
                val catalogoViewModel: CatalogoViewModel = viewModel()

                val galeriaViewModel: GaleriaViewModel = viewModel()

                var emprendimientoSeleccionado by remember {
                    mutableStateOf<Emprendimiento?>(null)
                }

                var pantallaActual by remember {
                    mutableStateOf("bienvenida")
                }

                var idGuardado by remember {
                    mutableStateOf<Long?>(null)
                }

                Scaffold(
                    modifier = Modifier.fillMaxSize()
                ) { espacioInterior ->

                    AnimatedContent(
                        targetState = pantallaActual,
                        transitionSpec = {
                            (
                                    fadeIn(
                                        animationSpec = tween(250)
                                    ) +
                                            slideInHorizontally(
                                                animationSpec = tween(300)
                                            ) { anchoCompleto ->
                                                anchoCompleto / 12
                                            }
                                    ) togetherWith (
                                    fadeOut(
                                        animationSpec = tween(180)
                                    ) +
                                            slideOutHorizontally(
                                                animationSpec = tween(250)
                                            ) { anchoCompleto ->
                                                -anchoCompleto / 12
                                            }
                                    )
                        },
                        label = "Transición entre pantallas"
                    ) { pantalla ->

                        when (pantalla) {
                            "bienvenida" -> {
                                PantallaBienvenida(
                                    modifier = Modifier.padding(espacioInterior),
                                    alExplorarCatalogo = {
                                        pantallaActual = "catalogo"
                                    },

                                    alRegistrarme = {
                                        pantallaActual = "registro"
                                    }
                                )
                            }

                            "catalogo" -> {
                                PantallaCatalogo(
                                    modifier = Modifier.padding(espacioInterior),
                                    viewModel = catalogoViewModel,
                                    alVolver = {
                                        pantallaActual = "bienvenida"
                                    },
                                    alSeleccionar = { emprendimiento ->
                                        emprendimientoSeleccionado = emprendimiento
                                        pantallaActual = "detalle"
                                    }
                                )
                            }

                            "detalle" -> {
                                val emprendimiento = emprendimientoSeleccionado

                                if (emprendimiento != null) {
                                    PantallaDetalleEmprendimiento(
                                        emprendimiento = emprendimiento,
                                        galeriaViewModel = galeriaViewModel,
                                        alVolver = {
                                            pantallaActual = "catalogo"
                                        }
                                    )
                                }
                            }

                            "registro" -> {
                                RegistroScreen(
                                    modifier = Modifier.padding(espacioInterior),
                                    alVolver = {
                                        pantallaActual = "bienvenida"
                                    },
                                    alGuardarExitoso = { id ->
                                        catalogoViewModel.cargarEmprendimientos()
                                        idGuardado = id //
                                        pantallaActual = "confirmacion"
                                    }
                                )
                            }

                            "confirmacion" -> {
                                PantallaConfirmacion(
                                    id = idGuardado,
                                    modifier = Modifier.padding(espacioInterior),
                                    alVolverAlInicio = {
                                        pantallaActual = "bienvenida"
                                    }
                                )
                            }

                            "mensajeria" -> {
                                PantallaMensajeria(
                                    modifier = Modifier.padding(espacioInterior),
                                    nombreEmprendimiento = emprendimientoSeleccionado?.nombre
                                        ?: "Taller Textil Circular",
                                    alVolver = {
                                        pantallaActual =
                                            if (emprendimientoSeleccionado != null) "detalle" else "catalogo"
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
