package com.example.fundaciontextil.ui.vistas.catalogo

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil.compose.AsyncImage
import com.example.fundaciontextil.model.Emprendimiento
import com.example.fundaciontextil.viewmodel.GaleriaViewModel
import java.io.File

private val Turquesa = Color(0xFF168C8C)

@Composable
fun PantallaDetalleEmprendimiento(
    emprendimiento: Emprendimiento,
    galeriaViewModel: GaleriaViewModel,
    alVolver: () -> Unit,
    alContactar: () -> Unit = {}
) {
    val fotos by galeriaViewModel.fotos.collectAsState()

    var fotoAmpliada by remember {
        mutableStateOf<String?>(null)
    }

    val selectorImagen = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        if (uri != null) {
            galeriaViewModel.agregarFoto(
                idEmprendimiento = emprendimiento.id,
                uri = uri
            )
        }
    }

    LaunchedEffect(emprendimiento.id) {
        galeriaViewModel.cargarFotos(emprendimiento.id)
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

        OutlinedButton(
            onClick = {
                selectorImagen.launch("image/*")
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Agregar fotografía de producto")
        }

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
                                .aspectRatio(1f)
                                .clip(RoundedCornerShape(12.dp))
                                .clickable {
                                    fotoAmpliada = foto.fotoUri
                                },
                            contentScale = ContentScale.Crop
                        )
                    }
                }
            }
        }
    }

    if (fotoAmpliada != null) {
        Dialog(
            onDismissRequest = { fotoAmpliada = null },
            properties = DialogProperties(
                usePlatformDefaultWidth = false,
                decorFitsSystemWindows = true
            )
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.80f))
                    .clickable(
                        indication = null,
                        interactionSource = remember {
                            androidx.compose.foundation.interaction
                                .MutableInteractionSource()
                        }
                    ) {
                        fotoAmpliada = null
                    },
                contentAlignment = Alignment.Center
            ) {
                AsyncImage(
                    model = File(fotoAmpliada!!),
                    contentDescription = "Fotografía de producto ampliada",
                    modifier = Modifier
                        .fillMaxWidth(0.95f)
                        .fillMaxHeight(0.80f)
                        .clickable(
                            indication = null,
                            interactionSource = remember {
                                androidx.compose.foundation.interaction
                                    .MutableInteractionSource()
                            }
                        ) {
                            // Mantiene abierta la imagen al tocarla.
                        },
                    contentScale = ContentScale.Fit
                )
            }
        }
    }
}
