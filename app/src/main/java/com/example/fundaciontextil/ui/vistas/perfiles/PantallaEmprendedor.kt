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

private val Turquesa = Color(0xFF168C8C)
private val Amarillo = Color(0xFFFFD84D)
private val Fondo = Color(0xFFFFFEFA)
private val Negro = Color(0xFF202020)

@Composable
fun PantallaEmprendedor(
    modifier: Modifier = Modifier,
    onVolverClick: () -> Unit = {},
    onMensajesClick: () -> Unit = {},
    onSolicitudesClick: () -> Unit = {}
) {
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
                text = "Harry Kane",
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

                    DatoFilaEmp(etiqueta = "Correo:", valor = "HarryKane@gmail.com")
                    Spacer(modifier = Modifier.height(6.dp))

                    DatoFilaEmp(etiqueta = "RUT:", valor = "19.313.298-K")
                    Spacer(modifier = Modifier.height(6.dp))

                    DatoFilaEmp(etiqueta = "Teléfono:", valor = "+56 9 3113 7689")
                    Spacer(modifier = Modifier.height(6.dp))

                    DatoFilaEmp(etiqueta = "Dirección:", valor = "Calle Machu Picchu 3001, Providencia")
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Tarjeta de Información del Emprendimiento
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(1.dp, Turquesa)
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Materiales que recibe",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Turquesa
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "🧵 Mezclilla, Algodón, Retazos",
                        style = MaterialTheme.typography.bodyLarge,
                        color = Negro
                    )
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