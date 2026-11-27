package com.example.fundaciontextil

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.fundaciontextil.model.Emprendimiento
import com.example.fundaciontextil.ui.theme.FundacionTextilTheme
import com.example.fundaciontextil.ui.vistas.bienvenida.PantallaBienvenida
import com.example.fundaciontextil.ui.vistas.catalogo.PantallaCatalogo
import com.example.fundaciontextil.ui.vistas.catalogo.PantallaDetalleEmprendimiento
import com.example.fundaciontextil.ui.vistas.login.PantallaLogin
import com.example.fundaciontextil.ui.vistas.login.PantallaLoginAdmin
import com.example.fundaciontextil.ui.vistas.login.PantallaLoginCliente
import com.example.fundaciontextil.ui.vistas.login.PantallaSeleccionLogin
import com.example.fundaciontextil.ui.vistas.mensajeria.PantallaBandejaEntrada
import com.example.fundaciontextil.ui.vistas.mensajeria.PantallaMensajeria
import com.example.fundaciontextil.ui.vistas.perfiles.PantallaEmprendedor
import com.example.fundaciontextil.ui.vistas.registro.PantallaConfirmacion
import com.example.fundaciontextil.ui.vistas.registro.PantallaRegistroCliente
import com.example.fundaciontextil.ui.vistas.registro.PantallaSeleccionRegistro
import com.example.fundaciontextil.ui.vistas.registro.RegistroScreen
import com.example.fundaciontextil.viewmodel.CatalogoViewModel
import com.example.fundaciontextil.viewmodel.GaleriaViewModel

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

                var nombreEmprendimientoChat by remember {
                    mutableStateOf("Taller Textil Circular")
                }

                var origenMensajeria by remember {
                    mutableStateOf("detalle")
                }

                var pantallaActual by rememberSaveable {
                    mutableStateOf("bienvenida")
                }

                var idGuardado by rememberSaveable {
                    mutableStateOf<Long?>(null)
                }

                Scaffold(
                    modifier = Modifier.fillMaxSize()
                ) { espacioInterior ->

                    AnimatedContent(
                        targetState = pantallaActual,
                        transitionSpec = {
                            (fadeIn(animationSpec = tween(250)) +
                                    slideInHorizontally(animationSpec = tween(300)) { anchoCompleto ->
                                        anchoCompleto / 12
                                    })
                                .togetherWith(
                                    fadeOut(animationSpec = tween(180)) +
                                            slideOutHorizontally(animationSpec = tween(250)) { anchoCompleto ->
                                                -anchoCompleto / 12
                                            })
                        },
                        label = "Transición entre pantallas"
                    ) { pantalla ->

                        when (pantalla) {
                            // --- INICIO ---
                            "bienvenida" -> {
                                PantallaBienvenida(
                                    modifier = Modifier.padding(espacioInterior),
                                    alExplorarCatalogo = { pantallaActual = "catalogo" },
                                    alRegistrarme = { pantallaActual = "seleccionRegistro" },
                                    alIniciarSesion = { pantallaActual = "seleccionLogin" },
                                    alVerPerfil = { pantallaActual = "perfilEmprendedor" }
                                )
                            }

                            // --- FLUJO DE REGISTRO ---
                            "seleccionRegistro" -> {
                                PantallaSeleccionRegistro(
                                    modifier = Modifier.padding(espacioInterior),
                                    alVolver = { pantallaActual = "bienvenida" },
                                    alSeleccionarEmprendedora = { pantallaActual = "registro" },
                                    alSeleccionarCliente = { pantallaActual = "registroCliente" }
                                )
                            }

                            "registro" -> {
                                RegistroScreen(
                                    modifier = Modifier.padding(espacioInterior),
                                    alVolver = { pantallaActual = "seleccionRegistro" },
                                    alGuardarExitoso = { id ->
                                        catalogoViewModel.cargarEmprendimientos()
                                        idGuardado = id
                                        pantallaActual = "confirmacion"
                                    }
                                )
                            }

                            "registroCliente" -> {
                                PantallaRegistroCliente(
                                    modifier = Modifier.padding(espacioInterior),
                                    alVolver = { pantallaActual = "seleccionRegistro" },
                                    alRegistroExitoso = {
                                        // Al registrarse con exito, lo mandamos directo al login de cliente
                                        pantallaActual = "loginCliente"
                                    }
                                )
                            }

                            "confirmacion" -> {
                                PantallaConfirmacion(
                                    id = idGuardado,
                                    modifier = Modifier.padding(espacioInterior),
                                    alVolverAlInicio = { pantallaActual = "bienvenida" }
                                )
                            }

                            // --- FLUJO DE LOGIN ---
                            "seleccionLogin" -> {
                                PantallaSeleccionLogin(
                                    modifier = Modifier.padding(espacioInterior),
                                    alVolver = { pantallaActual = "bienvenida" },
                                    alSeleccionarEmprendedora = { pantallaActual = "login" },
                                    alSeleccionarCliente = { pantallaActual = "loginCliente" },
                                    alSeleccionarAdmin = { pantallaActual = "loginAdmin" }
                                )
                            }

                            "login" -> {
                                PantallaLogin(
                                    alVolver = { pantallaActual = "seleccionLogin" },
                                    alLoginExitoso = { pantallaActual = "catalogo" }
                                )
                            }

                            "loginCliente" -> {
                                PantallaLoginCliente(
                                    modifier = Modifier.padding(espacioInterior),
                                    alVolver = { pantallaActual = "seleccionLogin" },
                                    alLoginExitoso = { pantallaActual = "catalogo" }
                                )
                            }

                            "loginAdmin" -> {
                                PantallaLoginAdmin(
                                    modifier = Modifier.padding(espacioInterior),
                                    alVolver = { pantallaActual = "seleccionLogin" },
                                    alLoginExitoso = {
                                        // Al ingresar como admin, se puede redirigir a otra pantalla.
                                        // Por ahora lo enviamos al catalogo.
                                        pantallaActual = "catalogo"
                                    }
                                )
                            }

                            // --- CATÁLOGO Y DETALLE ---
                            "catalogo" -> {
                                PantallaCatalogo(
                                    modifier = Modifier.padding(espacioInterior),
                                    viewModel = catalogoViewModel,
                                    alVolver = { pantallaActual = "bienvenida" },
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
                                        alVolver = { pantallaActual = "catalogo" },
                                        alContactar = {
                                            nombreEmprendimientoChat = emprendimiento.nombre
                                            origenMensajeria = "detalle"
                                            pantallaActual = "mensajeria"
                                        }
                                    )
                                }
                            }

                            // --- PERFILES Y MENSAJERÍA ---
                            "perfilEmprendedor" -> {
                                PantallaEmprendedor(
                                    onVolverClick = { pantallaActual = "bienvenida" },
                                    onMensajesClick = { pantallaActual = "bandeja_entrada" },
                                    onSolicitudesClick = { }
                                )
                            }

                            "bandeja_entrada" -> {
                                PantallaBandejaEntrada(
                                    modifier = Modifier.padding(espacioInterior),
                                    alVolver = { pantallaActual = "perfilEmprendedor" },
                                    alSeleccionarConversacion = { nombre ->
                                        nombreEmprendimientoChat = nombre
                                        origenMensajeria = "bandeja_entrada"
                                        pantallaActual = "mensajeria"
                                    },
                                    alExplorarCatalogo = { pantallaActual = "catalogo" }
                                )
                            }

                            "mensajeria" -> {
                                PantallaMensajeria(
                                    modifier = Modifier.padding(espacioInterior),
                                    nombreEmprendimiento = nombreEmprendimientoChat,
                                    alVolver = {
                                        pantallaActual = if (origenMensajeria == "bandeja_entrada") {
                                            "bandeja_entrada"
                                        } else if (emprendimientoSeleccionado != null) {
                                            "detalle"
                                        } else {
                                            "catalogo"
                                        }
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