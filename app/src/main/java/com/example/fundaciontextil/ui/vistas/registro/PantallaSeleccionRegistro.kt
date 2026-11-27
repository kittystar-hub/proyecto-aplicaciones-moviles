package com.example.fundaciontextil.ui.vistas.registro

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val Turquesa = Color(0xFF168C8C)
private val Amarillo = Color(0xFFFFD84D)
private val Fondo = Color(0xFFFFFEFA)
private val Negro = Color(0xFF202020)

@Composable
fun PantallaSeleccionRegistro(
    modifier: Modifier = Modifier,
    alVolver: () -> Unit = {},
    alSeleccionarEmprendedora: () -> Unit = {},
    alSeleccionarCliente: () -> Unit = {}
) {
    Surface(modifier = modifier.fillMaxSize(), color = Fondo) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            TextButton(
                onClick = alVolver,
                modifier = Modifier.align(Alignment.Start)
            ) {
                Text("← Volver", color = Turquesa)
            }

            Text(
                text = "Crear cuenta",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = Negro
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = "¿Cómo quieres registrarte?",
                style = MaterialTheme.typography.bodyLarge,
                color = Negro
            )
            Spacer(Modifier.height(32.dp))

            // Tarjeta emprendedora (Turquesa)
            Card(
                onClick = alSeleccionarEmprendedora,
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Turquesa),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = "Soy emprendedora",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Spacer(Modifier.height(6.dp))
                    Text(
                        text = "Registra tu taller textil y muestra tus productos.",
                        color = Color.White.copy(alpha = 0.9f)
                    )
                }
            }

            Spacer(Modifier.height(16.dp))

            // Tarjeta cliente (Amarillo)
            Card(
                onClick = alSeleccionarCliente,
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Amarillo),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = "Soy cliente",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Negro
                    )
                    Spacer(Modifier.height(6.dp))
                    Text(
                        text = "Descubre emprendimientos sostenibles y contáctalos.",
                        color = Negro.copy(alpha = 0.8f)
                    )
                }
            }
        }
    }
}