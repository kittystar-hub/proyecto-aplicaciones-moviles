package com.example.fundaciontextil.ui.vistas.catalogo

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.material3.TextButton
import androidx.compose.foundation.layout.Row
import androidx.compose.ui.Alignment

private val Turquesa = Color(0xFF168C8C)
private val Negro = Color(0xFF202020)

private data class Emprendimiento(
    val nombre: String,
    val categoria: String,
    val ubicacion: String,
    val descripcion: String
)

private val emprendimientosPrueba = listOf(
    Emprendimiento(
        nombre = "Emprendimiento 1",
        categoria = "Prueba",
        ubicacion = "Santiago",
        descripcion = "Emprendimiento simulado para pruebas."
    ),
    Emprendimiento(
        nombre = "Emprendimiento 2",
        categoria = "Prueba",
        ubicacion = "Valparaíso",
        descripcion = "Emprendimiento simulado n°2"
    ),
    Emprendimiento(
        nombre = "Emprendimiento 3",
        categoria = "Prueba",
        ubicacion = "Concepción",
        descripcion = "Emprendimiento Simulado n°3"
    )
)

@Composable
fun PantallaCatalogo(
    modifier: Modifier = Modifier,
    alVolver: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 24.dp)
    ) {
        TextButton(
            onClick = alVolver,
            modifier = Modifier.padding(start = 8.dp)
        ) {
            Text(
                text = "← Volver",
                color = Turquesa
            )
        }
        Text(
            text = "Catálogo textil",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = Negro,
            modifier = Modifier.padding(horizontal = 20.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Descubre emprendimientos que dan nueva vida a residuos textiles.",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(horizontal = 20.dp)
        )

        Spacer(modifier = Modifier.height(20.dp))

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(
                start = 16.dp,
                end = 16.dp,
                bottom = 24.dp
            ),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(emprendimientosPrueba) { emprendimiento ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFFFFEFA)
                    ),
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 2.dp
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = emprendimiento.nombre,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = Turquesa
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = emprendimiento.categoria,
                            style = MaterialTheme.typography.labelMedium
                        )

                        Text(
                            text = emprendimiento.ubicacion,
                            style = MaterialTheme.typography.bodySmall
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = emprendimiento.descripcion,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
        }
    }
}
