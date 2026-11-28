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
import com.example.fundaciontextil.viewmodel.PerfilEmprendedoraViewModel
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.saveable.rememberSaveable
import com.example.fundaciontextil.model.ChileData
import com.example.fundaciontextil.ui.components.CampoTexto
import com.example.fundaciontextil.ui.components.SelectorDesplegable
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.ui.platform.LocalContext
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.remember
import androidx.compose.ui.layout.ContentScale
import coil.compose.SubcomposeAsyncImage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.CancellationException

private val Turquesa = Color(0xFF168C8C)
private val Amarillo = Color(0xFFFFD84D)
private val Fondo = Color(0xFFFFFEFA)
private val Negro = Color(0xFF202020)

@Composable
fun PantallaEmprendedor(
    modifier: Modifier = Modifier,
    onVolverClick: () -> Unit = {},
    onMensajesClick: () -> Unit = {},
    onSolicitudesClick: () -> Unit = {},
    onCerrarSesionClick: () -> Unit = {},
    viewModel: PerfilEmprendedoraViewModel = viewModel()
) {
    val emprendedora by viewModel.emprendedora.collectAsState()
    val emprendimiento by viewModel.emprendimiento.collectAsState()
    val cargando by viewModel.cargando.collectAsState()
    val error by viewModel.error.collectAsState()
    val guardando by viewModel.guardando.collectAsState()
    val errorRegistro by viewModel.errorRegistro.collectAsState()
    val fotoUrl by viewModel.fotoUrl.collectAsState()
    val subiendoFoto by viewModel.subiendoFoto.collectAsState()
    val errorFoto by viewModel.errorFoto.collectAsState()
    val fotoEmprendimientoUrl by viewModel.fotoEmprendimientoUrl.collectAsState()
    val subiendoFotoEmprendimiento by viewModel.subiendoFotoEmprendimiento.collectAsState()
    val errorFotoEmprendimiento by viewModel.errorFotoEmprendimiento.collectAsState()

    var seleccionandoFotoEmprendimiento by rememberSaveable {
        mutableStateOf(false)
    }

    var errorSeleccionEmprendimiento by remember {
        mutableStateOf<String?>(null)
    }

    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    var errorSeleccion by remember {
        mutableStateOf<String?>(null)
    }

    var leyendoImagen by remember {
        mutableStateOf(false)
    }

    val procesandoFoto =
        (leyendoImagen && !seleccionandoFotoEmprendimiento) ||
                subiendoFoto

    val procesandoFotoEmprendimiento =
        (leyendoImagen && seleccionandoFotoEmprendimiento) ||
                subiendoFotoEmprendimiento

    val ocupado =
        guardando || procesandoFoto || procesandoFotoEmprendimiento

    var mostrarFormulario by rememberSaveable {
        mutableStateOf(false)
    }


    val selectorImagen = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        if (uri != null && !ocupado && !cargando) {
            val esFotoEmprendimiento = seleccionandoFotoEmprendimiento

            if (esFotoEmprendimiento) {
                errorSeleccionEmprendimiento = null
            } else {
                errorSeleccion = null
            }

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
                            .openInputStream(uri)?.use { entrada ->

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

                            } ?: error("No se pudo leer la imagen")
                    }

                    if (esFotoEmprendimiento) {
                        viewModel.subirFotoEmprendimiento(imagen, extension)
                    } else {
                        viewModel.subirFoto(imagen, extension)
                    }

                } catch (e: CancellationException) {
                    throw e
                } catch (e: Exception) {
                    val mensaje =
                        "No se pudo seleccionar la imagen. Verifica su formato y tamaño."

                    if (esFotoEmprendimiento) {
                        errorSeleccionEmprendimiento = mensaje
                    } else {
                        errorSeleccion = mensaje
                    }
                } finally {
                    leyendoImagen = false
                }
            }
        }
    }

    LaunchedEffect(Unit) {
        viewModel.cargarPerfil()
    }

    LaunchedEffect(emprendimiento) {
        if (emprendimiento != null) {
            mostrarFormulario = false
        }
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
                    enabled = !ocupado
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Volver",
                        tint = Turquesa
                    )
                }

                Text(
                    text = "Perfil Emprendedor",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = Turquesa
                )

                IconButton(
                    onClick = onMensajesClick,
                    enabled = !ocupado
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
                CircularProgressIndicator(color = Turquesa)

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


            // Foto de Perfil / Logo
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
                    seleccionandoFotoEmprendimiento = false
                    selectorImagen.launch("image/*")
                },
                enabled = !cargando &&
                        !ocupado &&
                        !mostrarFormulario &&
                        emprendedora != null
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
                    emprendedora != null ->
                        "${emprendedora?.nombre.orEmpty()} ${emprendedora?.apellido.orEmpty()}".trim()
                    else -> "Perfil Emprendedor"
                },
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = Negro
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Tarjeta de Datos de Contacto
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

                    DatoFilaEmp(
                        etiqueta = "Correo:",
                        valor = emprendedora?.email ?: "—"
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    DatoFilaEmp(
                        etiqueta = "RUT:",
                        valor = emprendedora?.rut ?: "—"
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    DatoFilaEmp(
                        etiqueta = "WhatsApp:",
                        valor = emprendedora?.whatsapp
                            ?.takeIf { it.isNotBlank() }
                            ?: "No registrado"
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    DatoFilaEmp(
                        etiqueta = "Región:",
                        valor = emprendimiento?.region ?: "—"
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    DatoFilaEmp(
                        etiqueta = "Comuna:",
                        valor = emprendimiento?.comuna ?: "—"
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))


            // Tarjeta de Información del Emprendimiento
            if (!cargando && error == null &&
                emprendedora != null && emprendimiento == null
            ) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White
                    ),
                    border = BorderStroke(1.dp, Turquesa)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = "Mi emprendimiento",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = Turquesa
                        )


                        if (mostrarFormulario) {
                            FormularioNuevoEmprendimiento(
                                guardando = guardando,
                                errorRegistro = errorRegistro,
                                onCancelar = {
                                    viewModel.limpiarErrorRegistro()
                                    mostrarFormulario = false
                                },
                                onGuardar = viewModel::registrarEmprendimiento
                            )
                        } else {
                            Text(
                                text = "Actualmente no tienes un emprendimiento registrado.",
                                color = Negro
                            )

                            Button(
                                onClick = {
                                    viewModel.limpiarErrorRegistro()
                                    mostrarFormulario = true
                                },
                                enabled = !ocupado,
                                modifier = Modifier.fillMaxWidth(),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Turquesa
                                )
                            ) {
                                Text("Registrar emprendimiento")
                            }
                        }
                    }
                }
            }
            if (!cargando && error == null && emprendimiento != null) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White
                    ),
                    border = BorderStroke(1.dp, Turquesa)
                ) {
                    Column(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth()
                    ) {
                        Text(
                            text = "Mi emprendimiento",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = Turquesa
                        )

                        Spacer(modifier = Modifier.height(12.dp))

// Fotografía principal del emprendimiento
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(180.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(Turquesa.copy(alpha = 0.08f)),
                            contentAlignment = Alignment.Center
                        ) {
                            if (!fotoEmprendimientoUrl.isNullOrBlank()) {
                                SubcomposeAsyncImage(
                                    model = fotoEmprendimientoUrl,
                                    contentDescription = "Fotografía del emprendimiento",
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

                        TextButton(
                            onClick = {
                                seleccionandoFotoEmprendimiento = true
                                selectorImagen.launch("image/*")
                            },
                            enabled = !ocupado && !cargando,
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        ) {
                            Text(
                                text = when {
                                    procesandoFotoEmprendimiento ->
                                        "Procesando fotografía..."

                                    !emprendimiento?.fotoUrl.isNullOrBlank() ->
                                        "Cambiar fotografía del emprendimiento"

                                    else ->
                                        "Agregar fotografía del emprendimiento"
                                },
                                color = Turquesa
                            )
                        }

                        if (procesandoFotoEmprendimiento) {
                            CircularProgressIndicator(
                                modifier = Modifier
                                    .size(24.dp)
                                    .align(Alignment.CenterHorizontally),
                                color = Turquesa,
                                strokeWidth = 2.dp
                            )
                        }

                        (errorSeleccionEmprendimiento ?: errorFotoEmprendimiento)?.let { mensaje ->
                            Text(
                                text = mensaje,
                                color = MaterialTheme.colorScheme.error,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        DatoFilaEmp(
                            etiqueta = "Taller:",
                            valor = emprendimiento?.nombreTaller ?: "—"
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        DatoFilaEmp(
                            etiqueta = "Categoría:",
                            valor = emprendimiento?.categoria ?: "—"
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        DatoFilaEmp(
                            etiqueta = "Rubros:",
                            valor = emprendimiento?.rubros ?: "—"
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = "Descripción",
                            fontWeight = FontWeight.Bold,
                            color = Negro
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = emprendimiento?.descripcion
                                ?: "Sin descripción",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Negro
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        HorizontalDivider()

                        Spacer(modifier = Modifier.height(16.dp))

                        val estado = emprendimiento?.estado

                        val textoEstado = when (estado) {
                            "aprobado" -> "Aprobado"
                            "rechazado" -> "Rechazado"
                            "pendiente" -> "Pendiente de aprobación"
                            else -> "No disponible"
                        }

                        val colorEstado = when (estado) {
                            "aprobado" -> Color(0xFF2E7D32)
                            "rechazado" -> Color(0xFFC62828)
                            "pendiente" -> Color(0xFFB45309)
                            else -> Color.Gray
                        }

                        Text(
                            text = "Estado del emprendimiento",
                            fontWeight = FontWeight.Bold,
                            color = Negro
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Surface(
                            color = colorEstado.copy(alpha = 0.12f),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = textoEstado,
                                color = colorEstado,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.padding(
                                    horizontal = 12.dp,
                                    vertical = 8.dp
                                )
                            )
                        }

                        if (estado == "rechazado") {
                            Spacer(modifier = Modifier.height(12.dp))

                            Text(
                                text = "Motivo del rechazo:",
                                fontWeight = FontWeight.Bold,
                                color = colorEstado
                            )

                            Text(
                                text = emprendimiento?.motivoRechazo
                                    ?: "No se indicó un motivo",
                                color = Negro
                            )
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.weight(1f))
            Spacer(modifier = Modifier.height(24.dp))

            // Botón de acción principal
            Button(
                onClick = onSolicitudesClick,
                enabled = !ocupado,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Turquesa)
            ) {
                Text(
                    text = "Ver solicitudes de donación",
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold
                )
            }
            Spacer(modifier = Modifier.height(12.dp))

            OutlinedButton(
                onClick = onCerrarSesionClick,
                enabled = !ocupado,
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
private fun FormularioNuevoEmprendimiento(
    guardando: Boolean,
    errorRegistro: String?,
    onCancelar: () -> Unit,
    onGuardar: (
        String, String, String,
        String, String, String
    ) -> Unit
) {
    var nombreTaller by rememberSaveable {
        mutableStateOf("")
    }

    var descripcion by rememberSaveable {
        mutableStateOf("")
    }

    var region by rememberSaveable {
        mutableStateOf("")
    }

    var comuna by rememberSaveable {
        mutableStateOf("")
    }

    var rubros by rememberSaveable {
        mutableStateOf(arrayListOf<String>())
    }

    val formularioValido =
        nombreTaller.isNotBlank() &&
                descripcion.isNotBlank() &&
                region.isNotBlank() &&
                comuna.isNotBlank() &&
                rubros.isNotEmpty()

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Registrar nuevo emprendimiento",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = Turquesa
        )

        Text(
            text = "Completa los datos de tu nueva ficha. " +
                    "Será revisada por un administrador.",
            style = MaterialTheme.typography.bodyMedium,
            color = Negro
        )

        CampoTexto(
            valor = nombreTaller,
            onValorCambio = { nombreTaller = it },
            label = "Nombre del taller / emprendimiento",
            placeholder = "Ej: Taller Raíz",
            error = null
        )

        OutlinedTextField(
            value = descripcion,
            onValueChange = {
                if (it.length <= 300) {
                    descripcion = it
                }
            },
            label = {
                Text("¿Qué hace tu emprendimiento?")
            },
            placeholder = {
                Text("Breve descripción")
            },
            minLines = 3,
            maxLines = 3,
            supportingText = {
                Text("${descripcion.length}/300")
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
        )

        SelectorDesplegable(
            label = "Región",
            valor = region,
            opciones = ChileData.regiones,
            onSeleccionar = {
                if (region != it) {
                    region = it
                    comuna = ""
                }
            },
            error = null
        )

        SelectorDesplegable(
            label = "Comuna",
            valor = comuna,
            opciones = ChileData.comunasDe(region),
            onSeleccionar = { comuna = it },
            habilitado = region.isNotBlank(),
            error = null
        )

        Text(
            text = "Rubros textiles (máx. 2)",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.SemiBold,
            color = Negro
        )

        ChileData.rubrosTextiles.forEach { rubro ->

            val marcado = rubro in rubros

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(
                        if (marcado) Color(0xFFE6F4F4)
                        else Color.Transparent
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = marcado,
                    enabled = !guardando &&
                            (marcado || rubros.size < 2),
                    onCheckedChange = { seleccionado ->
                        rubros = if (seleccionado) {
                            ArrayList(rubros + rubro)
                        } else {
                            ArrayList(rubros - rubro)
                        }
                    },
                    colors = CheckboxDefaults.colors(
                        checkedColor = Turquesa
                    )
                )

                Text(
                    text = rubro,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        errorRegistro?.let {
            Text(
                text = it,
                color = MaterialTheme.colorScheme.error
            )
        }

        Button(
            onClick = {
                onGuardar(
                    nombreTaller,
                    rubros.first(),
                    rubros.joinToString(", "),
                    region,
                    comuna,
                    descripcion
                )
            },
            enabled = formularioValido && !guardando,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Turquesa
            )
        ) {
            if (guardando) {
                CircularProgressIndicator(
                    color = Color.White,
                    modifier = Modifier.size(22.dp),
                    strokeWidth = 2.dp
                )

                Spacer(modifier = Modifier.width(12.dp))

                Text("Guardando...")
            } else {
                Text("Guardar ficha")
            }
        }

        OutlinedButton(
            onClick = onCancelar,
            enabled = !guardando,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp)
        ) {
            Text("Cancelar")
        }
    }
}

@Composable
private fun DatoFilaEmp(etiqueta: String, valor: String) {
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
fun PantallaEmprendedorPreview() {
    FundacionTextilTheme {
        PantallaEmprendedor()
    }
}