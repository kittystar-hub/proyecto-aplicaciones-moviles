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
import com.example.fundaciontextil.ui.vistas.perfiles.PantallaAdministrador
import com.example.fundaciontextil.ui.vistas.perfiles.PantallaEmprendedor
import com.example.fundaciontextil.ui.vistas.perfiles.PantallaUsuario
import com.example.fundaciontextil.ui.vistas.registro.PantallaConfirmacion
import com.example.fundaciontextil.ui.vistas.registro.PantallaRegistroCliente
import com.example.fundaciontextil.ui.vistas.registro.PantallaSeleccionRegistro
import com.example.fundaciontextil.ui.vistas.registro.RegistroScreen
import com.example.fundaciontextil.viewmodel.CatalogoViewModel
import com.example.fundaciontextil.viewmodel.GaleriaViewModel
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import com.example.fundaciontextil.data.remote.SupabaseConnectionTest
import com.example.fundaciontextil.data.repository.AuthRepositoryRemote
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        lifecycleScope.launch {
            SupabaseConnectionTest.verificarConexion()
        }

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

                var rolActual by rememberSaveable {
                    mutableStateOf("ninguno")
                }

                val authRepository = remember {
                    AuthRepositoryRemote()
                }

                val coroutineScope = rememberCoroutineScope()

                var cerrandoSesion by remember {
                    mutableStateOf(false)
                }

                var errorSesion by remember {
                    mutableStateOf<String?>(null)
                }

                val cerrarSesion: () -> Unit = {
                    if (!cerrandoSesion) {
                        cerrandoSesion = true
                        errorSesion = null

                        coroutineScope.launch {
                            try {
                                authRepository.cerrarSesion()

                                rolActual = "ninguno"
                                emprendimientoSeleccionado = null
                                pantallaActual = "bienvenida"

                            } catch (e: Exception) {
                                errorSesion = "No se pudo cerrar la sesión"
                            } finally {
                                cerrandoSesion = false
                            }
                        }
                    }
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
                                    alExplorarCatalogo = {
                                        rolActual = "ninguno"
                                        pantallaActual = "catalogo"
                                    },
                                    alRegistrarme = { pantallaActual = "seleccionRegistro" },
                                    alIniciarSesion = { pantallaActual = "seleccionLogin" }
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
                                    alGuardarExitoso = {
                                        pantallaActual = "confirmacion"
                                    }
                                )
                            }

                            "registroCliente" -> {
                                PantallaRegistroCliente(
                                    modifier = Modifier.padding(espacioInterior),
                                    alVolver = { pantallaActual = "seleccionRegistro" },
                                    alRegistroExitoso = {
                                        pantallaActual = "loginCliente"
                                    }
                                )
                            }

                            "confirmacion" -> {
                                PantallaConfirmacion(
                                    modifier = Modifier.padding(espacioInterior),
                                    alVolverAlInicio = {
                                        pantallaActual = "bienvenida"
                                    }
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
                                    alLoginExitoso = {
                                        rolActual = "emprendedor"
                                        pantallaActual = "catalogo"
                                    }
                                )
                            }

                            "loginCliente" -> {
                                PantallaLoginCliente(
                                    modifier = Modifier.padding(espacioInterior),
                                    alVolver = { pantallaActual = "seleccionLogin" },
                                    alLoginExitoso = {
                                        rolActual = "cliente"
                                        pantallaActual = "catalogo"
                                    }
                                )
                            }

                            "loginAdmin" -> {
                                PantallaLoginAdmin(
                                    modifier = Modifier.padding(espacioInterior),
                                    alVolver = { pantallaActual = "seleccionLogin" },
                                    alLoginExitoso = {
                                        rolActual = "admin"
                                        pantallaActual = "perfilAdmin"
                                    }
                                )
                            }

                            // --- CATÁLOGO Y DETALLE ---
                            "catalogo" -> {
                                PantallaCatalogo(
                                    modifier = Modifier.padding(espacioInterior),
                                    viewModel = catalogoViewModel,
                                    mostrarPerfil = rolActual == "emprendedor" || rolActual == "cliente",
                                    alVerPerfil = {
                                        pantallaActual = if (rolActual == "cliente") {
                                            "perfilCliente"
                                        } else {
                                            "perfilEmprendedor"
                                        }
                                    },
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
                                    onVolverClick = {
                                        pantallaActual = "catalogo"
                                    },
                                    onMensajesClick = {
                                        pantallaActual = "bandeja_entrada"
                                    },
                                    onSolicitudesClick = {},
                                    onCerrarSesionClick = cerrarSesion
                                )
                            }

                            "perfilCliente" -> {
                                PantallaUsuario(
                                    onVolverClick = {
                                        pantallaActual = "catalogo"
                                    },
                                    onMensajesClick = {
                                        pantallaActual = "bandeja_entrada"
                                    },
                                    onDonacionesClick = {
                                        pantallaActual = "catalogo"
                                    },
                                    onCerrarSesionClick = cerrarSesion
                                )
                            }

                            "perfilAdmin" -> {
                                PantallaAdministrador(
                                    modifier = Modifier.padding(espacioInterior),
                                    onVolverClick = cerrarSesion
                                )
                            }

                            "bandeja_entrada" -> {
                                PantallaBandejaEntrada(
                                    modifier = Modifier.padding(espacioInterior),
                                    alVolver = {
                                        pantallaActual = if (rolActual == "cliente") {
                                            "perfilCliente"
                                        } else {
                                            "perfilEmprendedor"
                                        }
                                    },
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