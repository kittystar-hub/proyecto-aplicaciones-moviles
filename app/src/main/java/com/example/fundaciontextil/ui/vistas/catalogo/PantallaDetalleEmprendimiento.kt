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
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import coil.compose.SubcomposeAsyncImage
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue

private val Turquesa = Color(0xFF168C8C)


@Composable
fun PantallaDetalleEmprendimiento(
    emprendimiento: EmprendimientoRemote,
    galeriaViewModel: GaleriaViewModel,
    fotoUrl: String? = null,
    alVolver: () -> Unit,
    alContactar: () -> Unit = {}
) {
    // Estados de la galería de productos
    val fotos by galeriaViewModel.fotos.collectAsState()
    val urlsFotos by galeriaViewModel.urlsFotos.collectAsState()
    val cargandoGaleria by galeriaViewModel.cargando.collectAsState()
    val errorGaleria by galeriaViewModel.error.collectAsState()

    // Cargar fotografías del emprendimiento seleccionado
    LaunchedEffect(emprendimiento.id) {
        emprendimiento.id?.let {
            galeriaViewModel.cargarFotos(it)
        }
    }

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

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(240.dp)
                .clip(RoundedCornerShape(14.dp))
                .background(Turquesa.copy(alpha = 0.08f)),
            contentAlignment = Alignment.Center
        ) {
            if (!fotoUrl.isNullOrBlank()) {
                SubcomposeAsyncImage(
                    model = fotoUrl,
                    contentDescription =
                        "Fotografía de ${emprendimiento.nombreTaller}",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop,
                    loading = {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(
                                color = Turquesa
                            )
                        }
                    },
                    error = {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Fotografía no disponible",
                                color = Color.Gray
                            )
                        }
                    }
                )
            } else {
                Text(
                    text = "Sin fotografía del emprendimiento",
                    color = Color.Gray,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

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


        when {
            cargandoGaleria -> {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(160.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Turquesa)
                }
            }

            errorGaleria != null -> {
                Text(
                    text = errorGaleria.orEmpty(),
                    color = MaterialTheme.colorScheme.error
                )

                Button(
                    onClick = {
                        emprendimiento.id?.let {
                            galeriaViewModel.cargarFotos(it)
                        }
                    }
                ) {
                    Text("Reintentar")
                }
            }

            fotos.none { it.emprendimientoId == emprendimiento.id } -> {
                Text(
                    text = "Este emprendimiento aún no tiene fotografías de productos.",
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            else -> {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(
                        fotos.filter {
                            it.emprendimientoId == emprendimiento.id
                        }
                    ) { foto ->

                        val url = foto.id?.let { urlsFotos[it] }

                        Box(
                            modifier = Modifier
                                .width(180.dp)
                                .height(180.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(
                                    Turquesa.copy(alpha = 0.08f)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            if (!url.isNullOrBlank()) {
                                SubcomposeAsyncImage(
                                    model = url,
                                    contentDescription =
                                        "Fotografía de producto",
                                    modifier = Modifier.fillMaxSize(),
                                    contentScale = ContentScale.Crop,
                                    loading = {
                                        Box(
                                            modifier = Modifier.fillMaxSize(),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            CircularProgressIndicator(
                                                color = Turquesa
                                            )
                                        }
                                    },
                                    error = {
                                        Text(
                                            text = "Imagen no disponible",
                                            color = Color.Gray
                                        )
                                    }
                                )
                            } else {
                                Text(
                                    text = "Imagen no disponible",
                                    color = Color.Gray
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}