package com.example.fundaciontextil.ui.vistas.catalogo

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.fundaciontextil.model.Emprendimiento
import com.example.fundaciontextil.viewmodel.GaleriaViewModel
import java.io.File

@Composable
fun PantallaDetalleEmprendimiento(
    emprendimiento: Emprendimiento,
    galeriaViewModel: GaleriaViewModel,
    alVolver: () -> Unit
) {
    val fotos by galeriaViewModel.fotos.collectAsState()

    LaunchedEffect(emprendimiento.id) {
        galeriaViewModel.cargarFotos(emprendimiento.id)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Button(onClick = alVolver) {
            Text("Volver al catálogo")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = emprendimiento.nombre,
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(8.dp))

        if (!emprendimiento.fotoUri.isNullOrBlank()) {
            AsyncImage(
                model = emprendimiento.fotoUri,
                contentDescription = "Fotografía de ${emprendimiento.nombre}",
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(16f / 9f)
                    .clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(16.dp))
        }

        Text(
            text = "Descripción",
            style = MaterialTheme.typography.titleMedium
        )

        Text(
            text = emprendimiento.descripcion.ifBlank {
                "Este emprendimiento aún no tiene una descripción."
            }
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Ubicación: ${
                listOf(emprendimiento.comuna, emprendimiento.region)
                    .filter { it.isNotBlank() }
                    .distinct()
                    .joinToString(", ")
                    .ifBlank { emprendimiento.ubicacion }
            }"
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Rubros: ${
                emprendimiento.rubros.ifBlank {
                    emprendimiento.categoria
                }
            }"
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Galería de productos",
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(modifier = Modifier.height(12.dp))

        if (fotos.isEmpty()) {
            Text("Este emprendimiento aún no tiene fotografías de productos.")
        } else {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(end = 8.dp)
            ) {
                items(fotos, key = { it.id }) { foto ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth(0.72f)
                    ) {
                        AsyncImage(
                            model = File(foto.fotoUri),
                            contentDescription = "Fotografía de producto",
                            modifier = Modifier
                                .fillMaxWidth()
                                .aspectRatio(1f),
                            contentScale = ContentScale.Crop
                        )
                    }
                }
            }
        }
    }
}