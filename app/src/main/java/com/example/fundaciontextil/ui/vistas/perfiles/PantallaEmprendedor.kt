package com.example.fundaciontextil.ui.vistas.perfiles

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.fundaciontextil.ui.theme.FundacionTextilTheme
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.fundaciontextil.viewmodel.PerfilEmprendedoraViewModel

private val Turquesa = Color(0xFF168C8C)
private val Amarillo = Color(0xFFFFD84D)
private val Fondo = Color(0xFFFFFEFA)
private val Negro = Color(0xFF202020)

@Composable
fun PantallaEmprendedor(
    modifier: Modifier = Modifier,
    onVolverClick: () -> Unit = {},
    onMensajesClick: () -> Unit = {},
    onSolicitudesClick: () -> Unit = {},
    onCerrarSesionClick: () -> Unit = {},
    viewModel: PerfilEmprendedoraViewModel = viewModel()
) {
    val emprendedora by viewModel.emprendedora.collectAsState()
    val emprendimiento by viewModel.emprendimiento.collectAsState()
    val cargando by viewModel.cargando.collectAsState()
    val error by viewModel.error.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.cargarPerfil()
    }
    Surface(
        modifier = modifier.fillMaxSize(),
        color = Fondo
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // --- BARRA SUPERIOR ---
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onVolverClick) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Volver",
                        tint = Turquesa
                    )
                }

                Text(
                    text = "Perfil Emprendedor",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = Turquesa
                )

                IconButton(onClick = onMensajesClick) {
                    Icon(
                        imageVector = Icons.Default.Email,
                        contentDescription = "Mensajes",
                        tint = Turquesa
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            if (cargando) {
                CircularProgressIndicator(color = Turquesa)

                Spacer(modifier = Modifier.height(16.dp))
            }

            error?.let { mensaje ->
                Text(
                    text = mensaje,
                    color = MaterialTheme.colorScheme.error
                )

                TextButton(
                    onClick = { viewModel.cargarPerfil() }
                ) {
                    Text("Reintentar")
                }
            }

            // Foto de Perfil / Logo
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .background(Amarillo),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Foto de perfil",
                    modifier = Modifier.size(52.dp),
                    tint = Negro
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Nombre principal
            Text(
                text = when {
                    cargando -> "Cargando perfil..."
                    emprendedora != null ->
                        "${emprendedora?.nombre.orEmpty()} ${emprendedora?.apellido.orEmpty()}".trim()
                    else -> "Perfil Emprendedor"
                },
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = Negro
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Tarjeta de Datos de Contacto
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Datos de Contacto",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Turquesa
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    DatoFilaEmp(
                        etiqueta = "Correo:",
                        valor = emprendedora?.email ?: "—"
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    DatoFilaEmp(
                        etiqueta = "RUT:",
                        valor = emprendedora?.rut ?: "—"
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    DatoFilaEmp(
                        etiqueta = "WhatsApp:",
                        valor = emprendedora?.whatsapp
                            ?.takeIf { it.isNotBlank() }
                            ?: "No registrado"
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    DatoFilaEmp(
                        etiqueta = "Región:",
                        valor = emprendimiento?.region ?: "—"
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    DatoFilaEmp(
                        etiqueta = "Comuna:",
                        valor = emprendimiento?.comuna ?: "—"
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))


// Tarjeta de Información del Emprendimiento
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                ),
                border = BorderStroke(1.dp, Turquesa)
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                ) {
                    Text(
                        text = "Mi emprendimiento",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Turquesa
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    DatoFilaEmp(
                        etiqueta = "Taller:",
                        valor = emprendimiento?.nombreTaller ?: "—"
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    DatoFilaEmp(
                        etiqueta = "Categoría:",
                        valor = emprendimiento?.categoria ?: "—"
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    DatoFilaEmp(
                        etiqueta = "Rubros:",
                        valor = emprendimiento?.rubros ?: "—"
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "Descripción",
                        fontWeight = FontWeight.Bold,
                        color = Negro
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = emprendimiento?.descripcion
                            ?: "Sin descripción",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Negro
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    HorizontalDivider()

                    Spacer(modifier = Modifier.height(16.dp))

                    val estado = emprendimiento?.estado

                    val textoEstado = when (estado) {
                        "aprobado" -> "Aprobado"
                        "rechazado" -> "Rechazado"
                        "pendiente" -> "Pendiente de aprobación"
                        else -> "No disponible"
                    }

                    val colorEstado = when (estado) {
                        "aprobado" -> Color(0xFF2E7D32)
                        "rechazado" -> Color(0xFFC62828)
                        "pendiente" -> Color(0xFFB45309)
                        else -> Color.Gray
                    }

                    Text(
                        text = "Estado del emprendimiento",
                        fontWeight = FontWeight.Bold,
                        color = Negro
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Surface(
                        color = colorEstado.copy(alpha = 0.12f),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = textoEstado,
                            color = colorEstado,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(
                                horizontal = 12.dp,
                                vertical = 8.dp
                            )
                        )
                    }

                    if (estado == "rechazado") {
                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = "Motivo del rechazo:",
                            fontWeight = FontWeight.Bold,
                            color = colorEstado
                        )

                        Text(
                            text = emprendimiento?.motivoRechazo
                                ?: "No se indicó un motivo",
                            color = Negro
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.weight(1f))
            Spacer(modifier = Modifier.height(24.dp))

            // Botón de acción principal
            Button(
                onClick = onSolicitudesClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Turquesa)
            ) {
                Text(
                    text = "Ver solicitudes de donación",
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold
                )
            }
            Spacer(modifier = Modifier.height(12.dp))

            OutlinedButton(
                onClick = onCerrarSesionClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text(
                    text = "Cerrar sesión",
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun DatoFilaEmp(etiqueta: String, valor: String) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = etiqueta,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            color = Color.DarkGray,
            modifier = Modifier.weight(0.32f)
        )
        Text(
            text = valor,
            style = MaterialTheme.typography.bodyMedium,
            color = Negro,
            modifier = Modifier.weight(0.68f)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PantallaEmprendedorPreview() {
    FundacionTextilTheme {
        PantallaEmprendedor()
    }
}