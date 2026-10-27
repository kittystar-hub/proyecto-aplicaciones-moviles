package com.example.fundaciontextil.ui.vistas.perfiles

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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

@Composable
fun PantallaEmprendedor(
    onVolverClick: () -> Unit = {},
    onMensajesClick: () -> Unit = {},
    onSolicitudesClick: () -> Unit = {}
) {
    val colorVerdeTeal = Color(0xFF1A8582)
    val colorAmarillo = Color(0xFFFFD15C)
    val fondoTarjeta = Color(0xFFF9F9F9)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            .background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // --- BARRA SUPERIOR DE BOTONES ---
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onVolverClick) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Volver",
                    tint = colorVerdeTeal
                )
            }

            // Título de la pantalla
            Text(
                text = "Perfil Emprendedor",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = colorVerdeTeal
            )

            IconButton(onClick = onMensajesClick) {
                Icon(
                    imageVector = Icons.Default.Email,
                    contentDescription = "Mensajes",
                    tint = colorVerdeTeal
                )
            }
        }
        // ---------------------------------------

        Spacer(modifier = Modifier.height(16.dp))

        // Foto de Perfil / Logo
        Box(
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
                .background(colorAmarillo),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = "Foto de perfil",
                modifier = Modifier.size(50.dp),
                tint = Color.Black
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Nombre principal
        Text(
            text = "Harry Kane",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Tarjeta de Datos de Contacto
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = fondoTarjeta),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = "Datos de Contacto", style = MaterialTheme.typography.titleMedium, color = colorVerdeTeal)
                Spacer(modifier = Modifier.height(8.dp))

                DatoFilaEmp(etiqueta = "Correo:", valor = "HarryKane@gmail.com")
                Spacer(modifier = Modifier.height(4.dp))

                DatoFilaEmp(etiqueta = "RUT:", valor = "19.313.298-k")
                Spacer(modifier = Modifier.height(4.dp))

                DatoFilaEmp(etiqueta = "Teléfono:", valor = "+56 9 3113 7689")
                Spacer(modifier = Modifier.height(4.dp))

                DatoFilaEmp(etiqueta = "Dirección:", valor = "Calle machupichu 3001, Providencia")
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Tarjeta de Información del Emprendimiento
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = BorderStroke(1.dp, colorVerdeTeal)
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
                    color = colorVerdeTeal
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "🧵 Mezclilla, Algodón, Retazos",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.Black
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        // Botón de acción principal
        Button(
            onClick = onSolicitudesClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = colorVerdeTeal)
        ) {
            Text("Ver solicitudes de donación", color = Color.White)
        }
    }
}

// Cambié el nombre del componente auxiliar para evitar conflictos si ambos archivos están en el mismo paquete
@Composable
fun DatoFilaEmp(etiqueta: String, valor: String) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = etiqueta,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            color = Color.DarkGray,
            modifier = Modifier.weight(0.3f)
        )
        Text(
            text = valor,
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Black,
            modifier = Modifier.weight(0.7f)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PantallaEmprendedorPreview() {
    MaterialTheme {
        PantallaEmprendedor()
    }
}