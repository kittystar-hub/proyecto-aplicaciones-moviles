package com.example.fundaciontextil.ui.vistas.mensajeria

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.fundaciontextil.model.Mensaje
import com.example.fundaciontextil.viewmodel.MensajeriaViewModel

// Paleta institucional de Fundación Textil Circular
private val Turquesa = Color(0xFF168C8C)
private val Amarillo = Color(0xFFFFD84D)
private val Fondo = Color(0xFFFFFEFA)
private val Negro = Color(0xFF202020)
private val GrisClaro = Color(0xFFF3F3F3)
private val GrisBorde = Color(0xFFE2E2E2)
private val VerdeOnline = Color(0xFF2E7D32)

@Composable
fun PantallaMensajeria(
    modifier: Modifier = Modifier,
    nombreEmprendimiento: String = "Taller Textil Circular",
    viewModel: MensajeriaViewModel = viewModel(),
    alVolver: () -> Unit = {}
) {
    val mensajes by viewModel.mensajes.collectAsState()
    val textoMensaje by viewModel.textoMensaje.collectAsState()
    val estaEscribiendo by viewModel.estaEscribiendo.collectAsState()

    val listState = rememberLazyListState()

    // Desplaza automáticamente al último mensaje al recibir o enviar
    LaunchedEffect(mensajes.size, estaEscribiendo) {
        if (mensajes.isNotEmpty()) {
            listState.animateScrollToItem(mensajes.size)
        }
    }

    Surface(
        modifier = modifier.fillMaxSize(),
        color = Fondo
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // 1. Barra superior (TopBar) con identidad visual
            CabeceraChat(
                nombreEmprendimiento = nombreEmprendimiento,
                alVolver = alVolver
            )

            // 2. Lista de Mensajes y animación de "Escribiendo..."
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                state = listState,
                verticalArrangement = Arrangement.spacedBy(10.dp),
                contentPadding = PaddingValues(vertical = 14.dp)
            ) {
                items(
                    items = mensajes,
                    key = { it.id }
                ) { mensaje ->
                    AnimatedVisibility(
                        visible = true,
                        enter = fadeIn() + slideInVertically { it / 2 }
                    ) {
                        BurbujaMensaje(mensaje = mensaje)
                    }
                }

                // Animación funcional: Indicador de "Escribiendo..."
                if (estaEscribiendo) {
                    item {
                        IndicadorEscribiendoAnimado()
                    }
                }
            }

            // 3. Fila de Sugerencias Rápidas (Requerimiento de entrevista)
            FilaSugerencias(
                sugerencias = viewModel.sugerenciasRapidas,
                alSeleccionarSugerencia = { sugerencia ->
                    viewModel.seleccionarSugerencia(sugerencia)
                }
            )

            // 4. Barra inferior de Entrada de Texto
            BarraEntradaMensaje(
                texto = textoMensaje,
                alCambiarTexto = { viewModel.actualizarTexto(it) },
                alEnviar = { viewModel.enviarMensaje() }
            )
        }
    }
}

/**
 * Cabecera con datos del emprendimiento y estado "En línea".
 */
@Composable
private fun CabeceraChat(
    nombreEmprendimiento: String,
    alVolver: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = Color.White,
        shadowElevation = 3.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = alVolver) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Volver",
                    tint = Negro
                )
            }

            // Avatar circular con colores de la Fundación
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .background(Amarillo, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = nombreEmprendimiento.take(2).uppercase(),
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = Negro
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = nombreEmprendimiento,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Negro,
                    maxLines = 1
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(7.dp)
                            .background(VerdeOnline, CircleShape)
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    Text(
                        text = "En línea • Fundación Textil",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray,
                        fontSize = 11.sp
                    )
                }
            }
        }
    }
}

/**
 * Burbuja individual de mensaje (Cliente a la derecha, Emprendedora a la izquierda).
 */
@Composable
private fun BurbujaMensaje(mensaje: Mensaje) {
    val alineacion = if (mensaje.esUsuario) Alignment.CenterEnd else Alignment.CenterStart
    val colorFondo = if (mensaje.esUsuario) Turquesa else Color.White
    val colorTexto = if (mensaje.esUsuario) Color.White else Negro
    val formaBurbuja = if (mensaje.esUsuario) {
        RoundedCornerShape(topStart = 16.dp, topEnd = 4.dp, bottomStart = 16.dp, bottomEnd = 16.dp)
    } else {
        RoundedCornerShape(topStart = 4.dp, topEnd = 16.dp, bottomStart = 16.dp, bottomEnd = 16.dp)
    }

    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = alineacion
    ) {
        Card(
            shape = formaBurbuja,
            colors = CardDefaults.cardColors(containerColor = colorFondo),
            elevation = CardDefaults.cardElevation(defaultElevation = if (mensaje.esUsuario) 1.dp else 2.dp),
            modifier = Modifier.fillMaxWidth(0.82f)
        ) {
            Column(
                modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp)
            ) {
                Text(
                    text = mensaje.contenido,
                    color = colorTexto,
                    style = MaterialTheme.typography.bodyMedium,
                    lineHeight = 20.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = mensaje.hora,
                    color = if (mensaje.esUsuario) Color.White.copy(alpha = 0.75f) else Color.Gray,
                    fontSize = 10.sp,
                    modifier = Modifier.align(Alignment.End)
                )
            }
        }
    }
}

/**
 * Animación funcional con puntos que rebotan para simular que la emprendedora escribe.
 * (Aporta al indicador IE 2.2.2 de la Rúbrica).
 */
@Composable
private fun IndicadorEscribiendoAnimado() {
    val transition = rememberInfiniteTransition(label = "Escribiendo")

    val dot1Offset by transition.animateFloat(
        initialValue = 0f,
        targetValue = -6f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 400, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "Punto1"
    )
    val dot2Offset by transition.animateFloat(
        initialValue = 0f,
        targetValue = -6f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 400, delayMillis = 130, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "Punto2"
    )
    val dot3Offset by transition.animateFloat(
        initialValue = 0f,
        targetValue = -6f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 400, delayMillis = 260, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "Punto3"
    )

    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(14.dp))
            .background(Color.White)
            .border(1.dp, GrisBorde, RoundedCornerShape(14.dp))
            .padding(horizontal = 14.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Emprendedora respondiendo",
            fontSize = 11.sp,
            color = Color.Gray,
            fontWeight = FontWeight.Medium
        )
        Spacer(modifier = Modifier.width(8.dp))
        Box(modifier = Modifier.offset(y = dot1Offset.dp).size(5.dp).background(Turquesa, CircleShape))
        Spacer(modifier = Modifier.width(3.dp))
        Box(modifier = Modifier.offset(y = dot2Offset.dp).size(5.dp).background(Turquesa, CircleShape))
        Spacer(modifier = Modifier.width(3.dp))
        Box(modifier = Modifier.offset(y = dot3Offset.dp).size(5.dp).background(Turquesa, CircleShape))
    }
}

/**
 * Fila desplazable horizontal con sugerencias de preguntas rápidas.
 */
@Composable
private fun FilaSugerencias(
    sugerencias: List<String>,
    alSeleccionarSugerencia: (String) -> Unit
) {
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 6.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 4.dp)
    ) {
        items(sugerencias) { sugerencia ->
            Surface(
                modifier = Modifier
                    .clip(RoundedCornerShape(18.dp))
                    .clickable { alSeleccionarSugerencia(sugerencia) },
                color = GrisClaro,
                shape = RoundedCornerShape(18.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, Turquesa.copy(alpha = 0.35f))
            ) {
                Text(
                    text = sugerencia,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 7.dp),
                    fontSize = 12.sp,
                    color = Negro,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

/**
 * Barra inferior con campo de texto y botón enviar.
 */
@Composable
private fun BarraEntradaMensaje(
    texto: String,
    alCambiarTexto: (String) -> Unit,
    alEnviar: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = Color.White,
        shadowElevation = 8.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = texto,
                onValueChange = alCambiarTexto,
                placeholder = {
                    Text(
                        text = "Escribe un mensaje...",
                        color = Color.Gray,
                        fontSize = 14.sp
                    )
                },
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(24.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Turquesa,
                    unfocusedBorderColor = GrisBorde,
                    focusedContainerColor = Fondo,
                    unfocusedContainerColor = Fondo
                ),
                maxLines = 4
            )

            Spacer(modifier = Modifier.width(8.dp))

            val botonHabilitado = texto.isNotBlank()
            IconButton(
                onClick = alEnviar,
                enabled = botonHabilitado,
                modifier = Modifier
                    .size(46.dp)
                    .clip(CircleShape)
                    .background(if (botonHabilitado) Turquesa else Color.LightGray)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Send,
                    contentDescription = "Enviar mensaje",
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}
