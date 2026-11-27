package com.example.fundaciontextil.ui.vistas.catalogo

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
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
import coil.compose.AsyncImage
import com.example.fundaciontextil.model.Emprendimiento
import com.example.fundaciontextil.viewmodel.CatalogoViewModel

private val Turquesa = Color(0xFF168C8C)
private val Negro = Color(0xFF202020)

@Composable
fun PantallaCatalogo(
    modifier: Modifier = Modifier,
    viewModel: CatalogoViewModel,
    mostrarPerfil: Boolean = false,
    alVerPerfil: () -> Unit = {},
    alVolver: () -> Unit = {},
    alSeleccionar: (Emprendimiento) -> Unit = {}
) {
    val emprendimientos by viewModel.emprendimientos.collectAsState()
    var regionSeleccionada by remember { mutableStateOf("Todas") }
    var menuRegionesExpandido by remember { mutableStateOf(false) }

    var comunaSeleccionada by remember { mutableStateOf("Todas") }
    var menuComunasExpandido by remember { mutableStateOf(false) }

    val regiones = listOf("Todas") +
            emprendimientos
                .map { it.region.ifBlank { it.ubicacion }.trim() }
                .filter { it.isNotBlank() }
                .distinct()
                .sorted()

    val comunas = listOf("Todas") +
            emprendimientos
                .filter {
                    regionSeleccionada == "Todas" ||
                            it.region.ifBlank { it.ubicacion }
                                .trim()
                                .equals(regionSeleccionada, ignoreCase = true)
                }
                .map { it.comuna.trim() }
                .filter { it.isNotBlank() }
                .distinct()
                .sorted()

    var rubroSeleccionado by remember { mutableStateOf("Todos") }
    var menuRubrosExpandido by remember { mutableStateOf(false) }

    val rubros = listOf("Todos") +
            emprendimientos
                .flatMap { emprendimiento ->
                    val rubrosTexto = emprendimiento.rubros
                        .ifBlank { emprendimiento.categoria }

                    rubrosTexto
                        .split(",")
                        .map { it.trim() }
                        .filter { it.isNotBlank() }
                }
                .distinct()
                .sorted()

    val emprendimientosFiltrados =
        emprendimientos.filter { emprendimiento ->
            val region = emprendimiento.region
                .ifBlank { emprendimiento.ubicacion }
                .trim()

            val coincideRegion =
                regionSeleccionada == "Todas" ||
                        region.equals(regionSeleccionada, ignoreCase = true)

            val coincideComuna =
                comunaSeleccionada == "Todas" ||
                        emprendimiento.comuna.trim()
                            .equals(comunaSeleccionada, ignoreCase = true)

            val rubrosEmprendimiento = emprendimiento.rubros
                .ifBlank { emprendimiento.categoria }
                .split(",")
                .map { it.trim() }

            val coincideRubro =
                rubroSeleccionado == "Todos" ||
                        rubrosEmprendimiento.any {
                            it.equals(rubroSeleccionado, ignoreCase = true)
                        }

            coincideRegion && coincideComuna && coincideRubro
        }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(top = 24.dp)
    ) {
        // Barra superior: Volver a la izquierda y Perfil a la derecha (solo si inició sesión)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextButton(onClick = alVolver) {
                Text(
                    text = "← Volver",
                    color = Turquesa
                )
            }

            if (mostrarPerfil) {
                IconButton(onClick = alVerPerfil) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Mi perfil",
                        tint = Turquesa
                    )
                }
            }
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

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Filtro de región
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Región",
                        style = MaterialTheme.typography.titleSmall
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Box {
                        Button(
                            onClick = { menuRegionesExpandido = true },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(text = regionSeleccionada, maxLines = 1)
                        }

                        DropdownMenu(
                            expanded = menuRegionesExpandido,
                            onDismissRequest = { menuRegionesExpandido = false }
                        ) {
                            regiones.forEach { region ->
                                DropdownMenuItem(
                                    text = { Text(region) },
                                    onClick = {
                                        regionSeleccionada = region
                                        comunaSeleccionada = "Todas"
                                        menuRegionesExpandido = false
                                    }
                                )
                            }
                        }
                    }
                }

                // Filtro de comuna
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Comuna",
                        style = MaterialTheme.typography.titleSmall
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Box {
                        Button(
                            onClick = { menuComunasExpandido = true },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(text = comunaSeleccionada, maxLines = 1)
                        }

                        DropdownMenu(
                            expanded = menuComunasExpandido,
                            onDismissRequest = { menuComunasExpandido = false }
                        ) {
                            comunas.forEach { comuna ->
                                DropdownMenuItem(
                                    text = { Text(comuna) },
                                    onClick = {
                                        comunaSeleccionada = comuna
                                        menuComunasExpandido = false
                                    }
                                )
                            }
                        }
                    }
                }

                // Filtro de rubro
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Rubro",
                        style = MaterialTheme.typography.titleSmall
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Box {
                        Button(
                            onClick = { menuRubrosExpandido = true },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(text = rubroSeleccionado, maxLines = 1)
                        }

                        DropdownMenu(
                            expanded = menuRubrosExpandido,
                            onDismissRequest = { menuRubrosExpandido = false }
                        ) {
                            rubros.forEach { rubro ->
                                DropdownMenuItem(
                                    text = { Text(rubro) },
                                    onClick = {
                                        rubroSeleccionado = rubro
                                        menuRubrosExpandido = false
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(
                start = 16.dp,
                end = 16.dp,
                bottom = 24.dp
            ),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(emprendimientosFiltrados) { emprendimiento ->
                Card(
                    onClick = { alSeleccionar(emprendimiento) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFEFA)),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
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

                            Spacer(modifier = Modifier.height(12.dp))
                        }

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