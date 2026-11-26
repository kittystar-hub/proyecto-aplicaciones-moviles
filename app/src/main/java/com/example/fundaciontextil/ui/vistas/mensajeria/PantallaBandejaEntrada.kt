package com.example.fundaciontextil.ui.vistas.mensajeria

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fundaciontextil.model.ConversacionItem

private val Turquesa = Color(0xFF168C8C)
private val Amarillo = Color(0xFFFFD84D)
private val Fondo = Color(0xFFFFFEFA)
private val Negro = Color(0xFF202020)
private val GrisTexto = Color(0xFF6B7280)
private val GrisBorde = Color(0xFFE5E7EB)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaBandejaEntrada(
    modifier: Modifier = Modifier,
    alVolver: () -> Unit = {},
    alSeleccionarConversacion: (nombreEmprendimiento: String) -> Unit = {},
    alExplorarCatalogo: () -> Unit = {}
) {
    // Lista de conversaciones iniciales simuladas
    val conversaciones = remember {
        listOf(
            ConversacionItem(
                id = "1",
                nombreEmprendimiento = "Taller Costuras Verdes",
                ultimoMensaje = "¡Hola! Sí, recibimos mezclilla y retazos esta semana.",
                horaUltimoMensaje = "14:35",
                mensajesNoLeidos = 1,
                rubro = "Upcycling & Confección"
            ),
            ConversacionItem(
                id = "2",
                nombreEmprendimiento = "EcoRetazos Providencia",
                ultimoMensaje = "Perfecto, coordinemos la entrega para este viernes.",
                horaUltimoMensaje = "11:20",
                mensajesNoLeidos = 0,
                rubro = "Retazos y Telas"
            ),
            ConversacionItem(
                id = "3",
                nombreEmprendimiento = "Creaciones Circulares Valparaíso",
                ultimoMensaje = "Muchas gracias por tu interés en nuestros productos.",
                horaUltimoMensaje = "Ayer",
                mensajesNoLeidos = 0,
                rubro = "Accesorios Sostenibles"
            )
        )
    }

    var busqueda by remember { mutableStateOf("") }

    val conversacionesFiltradas = remember(busqueda, conversaciones) {
        if (busqueda.isBlank()) {
            conversaciones
        } else {
            conversaciones.filter {
                it.nombreEmprendimiento.contains(busqueda, ignoreCase = true) ||
                        it.ultimoMensaje.contains(busqueda, ignoreCase = true) ||
                        it.rubro.contains(busqueda, ignoreCase = true)
            }
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = Fondo,
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "Bandeja de Entrada",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = Negro
                        )
                        Text(
                            text = "${conversaciones.size} conversaciones activas",
                            style = MaterialTheme.typography.bodySmall,
                            color = GrisTexto
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = alVolver) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver",
                            tint = Turquesa
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Fondo
                )
            )
        }
    ) { paddingValores ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValores)
                .padding(horizontal = 16.dp)
        ) {
            // Buscador de chats
            OutlinedTextField(
                value = busqueda,
                onValueChange = { busqueda = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                placeholder = {
                    Text(text = "Buscar por taller o mensaje...", color = GrisTexto)
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Buscar",
                        tint = Turquesa
                    )
                },
                trailingIcon = {
                    if (busqueda.isNotEmpty()) {
                        IconButton(onClick = { busqueda = "" }) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Limpiar búsqueda",
                                tint = GrisTexto
                            )
                        }
                    }
                },
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Turquesa,
                    unfocusedBorderColor = GrisBorde,
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White
                ),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(8.dp))

            if (conversacionesFiltradas.isEmpty()) {
                // Estado vacío cuando no hay resultados
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .size(72.dp)
                                .clip(CircleShape)
                                .background(Amarillo.copy(alpha = 0.4f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Email,
                                contentDescription = null,
                                tint = Negro,
                                modifier = Modifier.size(36.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = "No se encontraron mensajes",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = Negro
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = if (busqueda.isNotEmpty()) "Intenta con otro término de búsqueda."
                            else "Aún no tienes conversaciones. Explora el catálogo para contactar talleres.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = GrisTexto,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        Button(
                            onClick = alExplorarCatalogo,
                            shape = RoundedCornerShape(14.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Turquesa)
                        ) {
                            Text(text = "Explorar catálogo", color = Color.White)
                        }
                    }
                }
            } else {
                // Lista de conversaciones
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    contentPadding = PaddingValues(vertical = 8.dp)
                ) {
                    items(conversacionesFiltradas, key = { it.id }) { conversacion ->
                        ItemConversacion(
                            conversacion = conversacion,
                            onClick = {
                                alSeleccionarConversacion(conversacion.nombreEmprendimiento)
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ItemConversacion(
    conversacion: ConversacionItem,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Avatar con la inicial del taller
            val inicial = conversacion.nombreEmprendimiento.firstOrNull()?.uppercase() ?: "T"
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape)
                    .background(Amarillo),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = inicial,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = Negro
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            // Contenido central del mensaje
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = conversacion.nombreEmprendimiento,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Negro,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f, fill = false)
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = conversacion.horaUltimoMensaje,
                        style = MaterialTheme.typography.bodySmall,
                        color = if (conversacion.mensajesNoLeidos > 0) Turquesa else GrisTexto,
                        fontWeight = if (conversacion.mensajesNoLeidos > 0) FontWeight.Bold else FontWeight.Normal
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = conversacion.ultimoMensaje,
                        style = MaterialTheme.typography.bodyMedium,
                        color = if (conversacion.mensajesNoLeidos > 0) Negro else GrisTexto,
                        fontWeight = if (conversacion.mensajesNoLeidos > 0) FontWeight.SemiBold else FontWeight.Normal,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )

                    if (conversacion.mensajesNoLeidos > 0) {
                        Spacer(modifier = Modifier.width(8.dp))
                        Box(
                            modifier = Modifier
                                .size(20.dp)
                                .clip(CircleShape)
                                .background(Turquesa),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = conversacion.mensajesNoLeidos.toString(),
                                style = MaterialTheme.typography.labelSmall,
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                if (conversacion.rubro.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "🧵 ${conversacion.rubro}",
                        style = MaterialTheme.typography.labelSmall,
                        color = Turquesa
                    )
                }
            }
        }
    }
}
