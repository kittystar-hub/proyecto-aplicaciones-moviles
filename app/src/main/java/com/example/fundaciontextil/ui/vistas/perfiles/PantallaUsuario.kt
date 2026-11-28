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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.fundaciontextil.viewmodel.PerfilClienteViewModel
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.ui.platform.LocalContext
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.CancellationException
import coil.compose.SubcomposeAsyncImage
import androidx.compose.ui.layout.ContentScale

private val Turquesa = Color(0xFF168C8C)
private val Amarillo = Color(0xFFFFD84D)
private val Fondo = Color(0xFFFFFEFA)
private val Negro = Color(0xFF202020)

@Composable
fun PantallaUsuario(
    modifier: Modifier = Modifier,
    onVolverClick: () -> Unit = {},
    onMensajesClick: () -> Unit = {},
    onDonacionesClick: () -> Unit = {},
    onCerrarSesionClick: () -> Unit = {},
    viewModel: PerfilClienteViewModel = viewModel()
) {
    val cliente by viewModel.cliente.collectAsState()
    val cargando by viewModel.cargando.collectAsState()
    val error by viewModel.error.collectAsState()
    val fotoUrl by viewModel.fotoUrl.collectAsState()
    val subiendoFoto by viewModel.subiendoFoto.collectAsState()
    val errorFoto by viewModel.errorFoto.collectAsState()

    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    var errorSeleccion by remember {
        mutableStateOf<String?>(null)
    }

    var leyendoImagen by remember {
        mutableStateOf(false)
    }

    val procesandoFoto = leyendoImagen || subiendoFoto


    val selectorImagen = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->

        if (uri != null && !procesandoFoto) {
            errorSeleccion = null
            leyendoImagen = true

            coroutineScope.launch {
                try {
                    val extension = when (
                        context.contentResolver.getType(uri)
                    ) {
                        "image/jpeg" -> "jpg"
                        "image/png" -> "png"
                        "image/webp" -> "webp"
                        else -> error("Formato de imagen no permitido")
                    }

                    val imagen = withContext(Dispatchers.IO) {
                        context.contentResolver
                            .openInputStream(uri)
                            ?.use { entrada ->
                                val salida = java.io.ByteArrayOutputStream()
                                val buffer = ByteArray(8192)
                                var total = 0

                                while (true) {
                                    val leidos = entrada.read(buffer)

                                    if (leidos == -1) break

                                    total += leidos

                                    if (total > 5 * 1024 * 1024) {
                                        error("La imagen no puede superar los 5 MB")
                                    }

                                    salida.write(buffer, 0, leidos)
                                }

                                salida.toByteArray()
                            }
                            ?: error("No se pudo leer la imagen")
                    }

                    viewModel.subirFoto(imagen, extension)

                } catch (e: CancellationException) {
                    throw e
                } catch (e: Exception) {
                    errorSeleccion =
                        e.message ?: "No se pudo seleccionar la imagen"
                } finally {
                    leyendoImagen = false
                }
            }
        }
    }

    LaunchedEffect(Unit) {
        viewModel.cargarPerfil()
    }
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
                IconButton(
                    onClick = onVolverClick,
                    enabled = !procesandoFoto
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Volver",
                        tint = Turquesa
                    )
                }

                Text(
                    text = "Perfil de Usuario",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = Turquesa
                )

                IconButton(
                    onClick = onMensajesClick,
                    enabled = !procesandoFoto
                ) {
                    Icon(
                        imageVector = Icons.Default.Email,
                        contentDescription = "Mensajes",
                        tint = Turquesa
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            if (cargando) {
                CircularProgressIndicator(
                    color = Turquesa
                )

                Spacer(modifier = Modifier.height(16.dp))
            }

            error?.let { mensaje ->
                Text(
                    text = mensaje,
                    color = MaterialTheme.colorScheme.error
                )

                TextButton(
                    onClick = { viewModel.cargarPerfil() }
                ) {
                    Text("Reintentar")
                }
            }


            // Foto de Perfil
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .background(Amarillo),
                contentAlignment = Alignment.Center
            ) {
                if (!fotoUrl.isNullOrBlank()) {
                    SubcomposeAsyncImage(
                        model = fotoUrl,
                        contentDescription = "Foto de perfil",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop,
                        loading = {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(28.dp),
                                    color = Turquesa,
                                    strokeWidth = 2.dp
                                )
                            }
                        },
                        error = {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Person,
                                    contentDescription = "Foto no disponible",
                                    modifier = Modifier.size(52.dp),
                                    tint = Negro
                                )
                            }
                        }
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Foto de perfil",
                        modifier = Modifier.size(52.dp),
                        tint = Negro
                    )
                }
            }


            TextButton(
                onClick = {
                    selectorImagen.launch("image/*")
                },
                enabled = !cargando &&
                        !procesandoFoto &&
                        cliente != null
            ) {
                Text(
                    text = when {
                        procesandoFoto -> "Procesando fotografía..."
                        fotoUrl != null -> "Cambiar fotografía"
                        else -> "Agregar fotografía"
                    },
                    color = Turquesa
                )
            }

            if (procesandoFoto) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = Turquesa,
                    strokeWidth = 2.dp
                )
            }

            (errorSeleccion ?: errorFoto)?.let { mensaje ->
                Text(
                    text = mensaje,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Nombre principal
            Text(
                text = when {
                    cargando -> "Cargando perfil..."
                    cliente != null ->
                        "${cliente?.nombre.orEmpty()} ${cliente?.apellido.orEmpty()}".trim()
                    else -> "Perfil de usuario"
                },
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = Negro
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Tarjeta de Datos Personales
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

                    DatoFilaUsuario(
                        etiqueta = "Correo:",
                        valor = cliente?.email ?: "—"
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    DatoFilaUsuario(
                        etiqueta = "Teléfono:",
                        valor = cliente?.telefono
                            ?.takeIf { it.isNotBlank() }
                            ?: "No registrado"
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Estadísticas de impacto
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
                        text = "Impacto Ambiental",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Turquesa
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "🌱 0 Kg de ropa reciclada",
                        style = MaterialTheme.typography.bodyLarge,
                        color = Negro
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))
            Spacer(modifier = Modifier.height(24.dp))

            // Botón de acción principal
            Button(
                onClick = onDonacionesClick,
                enabled = !procesandoFoto,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Turquesa)
            ) {
                Text(
                    text = "Ver mis donaciones",
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedButton(
                onClick = onCerrarSesionClick,
                enabled = !procesandoFoto,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text(
                    text = "Cerrar sesión",
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun DatoFilaUsuario(etiqueta: String, valor: String) {
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
fun PantallaUsuarioPreview() {
    FundacionTextilTheme {
        PantallaUsuario()
    }
}