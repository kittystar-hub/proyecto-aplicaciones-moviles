package com.example.fundaciontextil.ui.vistas.catalogo

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fundaciontextil.data.model.EmprendimientoRemote
import com.example.fundaciontextil.viewmodel.GaleriaViewModel

private val Turquesa = Color(0xFF168C8C)

@Composable
fun PantallaDetalleEmprendimiento(
    emprendimiento: EmprendimientoRemote,
    galeriaViewModel: GaleriaViewModel,
    alVolver: () -> Unit,
    alContactar: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Button(onClick = alVolver) {
            Text("Volver al catálogo")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = emprendimiento.nombreTaller,
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Descripción",
            style = MaterialTheme.typography.titleMedium
        )

        Text(
            text = emprendimiento.descripcion.orEmpty().ifBlank {
                "Este emprendimiento aún no tiene una descripción."
            }
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Ubicación: ${
                listOfNotNull(
                    emprendimiento.comuna,
                    emprendimiento.region
                )
                    .filter { it.isNotBlank() }
                    .distinct()
                    .joinToString(", ")
                    .ifBlank {
                        emprendimiento.ubicacion.orEmpty()
                    }
                    .ifBlank {
                        "No registrada"
                    }
            }"
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Rubros: ${
                emprendimiento.rubros.orEmpty().ifBlank {
                    emprendimiento.categoria.orEmpty()
                }.ifBlank {
                    "No registrados"
                }
            }"
        )

        Spacer(modifier = Modifier.height(18.dp))

        // Botón de contacto directo con la emprendedora
        Button(
            onClick = alContactar,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Turquesa,
                contentColor = Color.White
            )
        ) {
            Icon(
                imageVector = Icons.Default.Email,
                contentDescription = "Enviar mensaje",
                tint = Color.White
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = "Contactar emprendedora",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Galería de productos",
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Galería temporalmente no disponible.",
            style = MaterialTheme.typography.bodyMedium
        )
    }
}