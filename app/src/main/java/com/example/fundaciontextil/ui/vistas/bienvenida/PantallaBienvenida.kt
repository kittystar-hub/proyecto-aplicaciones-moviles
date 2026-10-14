package com.example.fundaciontextil.ui.vistas.bienvenida

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fundaciontextil.ui.theme.FundacionTextilTheme

//Paleta de colores para el tema de la aplicación
private val Turquesa = Color(0xFF168C8C)
private val Amarillo = Color(0xFFFFD84D)
private val Fondo = Color(0xFFFFFEFA)
private val Negro = Color(0xFF202020)

@Composable
fun PantallaBienvenida(modifier: Modifier = Modifier) {
    val contexto = LocalContext.current

    Surface(
        modifier = modifier.fillMaxSize(),
        color = Fondo
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    horizontal = 24.dp,
                    vertical = 28.dp
                ),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Identidad visual provisional
            Box(
                modifier = Modifier
                    .background(Amarillo, CircleShape)
                    .padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "TC",
                    fontSize = 38.sp,
                    fontWeight = FontWeight.Bold,
                    color = Negro
                )
            }

            Spacer(modifier = Modifier.height(28.dp))

            Text(
                text = "Fundación\nTextil Circular",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = Negro,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Conectamos personas, creatividad y " +
                        "emprendimientos textiles para dar " +
                        "nueva vida a los materiales.",
                style = MaterialTheme.typography.bodyLarge,
                color = Negro,
                textAlign = TextAlign.Center,
                lineHeight = 26.sp
            )

            Spacer(modifier = Modifier.height(36.dp))

            Button(
                onClick = {
                    Toast.makeText(
                        contexto,
                        "En Construcción",
                        Toast.LENGTH_SHORT
                    ).show()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Turquesa,
                    contentColor = Color.White
                )
            ) {
                Text(
                    text = "Explorar catálogo",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedButton(
                onClick = {
                    Toast.makeText(
                        contexto,
                        "En Construcción",
                        Toast.LENGTH_SHORT
                    ).show()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text(
                    text = "Conocer la fundación",
                    fontSize = 16.sp,
                    color = Turquesa
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "Un espacio para crear, compartir y reutilizar.",
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                color = Negro
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun VistaPreviaBienvenida() {
    FundacionTextilTheme {
        PantallaBienvenida()
    }
}