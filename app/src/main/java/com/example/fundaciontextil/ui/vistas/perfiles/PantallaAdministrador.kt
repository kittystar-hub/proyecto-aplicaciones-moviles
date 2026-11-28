package com.example.fundaciontextil.ui.vistas.perfiles

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.fundaciontextil.ui.theme.FundacionTextilTheme
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.fundaciontextil.viewmodel.AdministradorViewModel

private val Turquesa = Color(0xFF168C8C)
private val Amarillo = Color(0xFFFFD84D)
private val Fondo = Color(0xFFFFFEFA)
private val Negro = Color(0xFF202020)
private val RojoError = Color(0xFFD32F2F)
private val VerdeAprobado = Color(0xFF2E7D32)

data class SolicitudAdmin(
    val id: String,
    val nombreEmprendimiento: String,
    val categoria: String,
    val comuna: String,
    val descripcion: String,
    val responsable: String = "No registrado",
    val correo: String = "No registrado",
    val telefono: String = "No registrado"
)

data class EmprendimientoAdmin(
    val id: String,
    val nombre: String,
    val categoria: String,
    val comuna: String,
    val descripcion: String,
    val responsable: String,
    val correo: String,
    val telefono: String,
    val estado: String = "Activo"
)

data class CuentaRegistradaAdmin(
    val id: String,
    val nombre: String,
    val rol: String, // "Cliente", "Emprendedor" o "Administrador"
    val correo: String,
    val rut: String,
    val telefono: String,
    val direccion: String
)

@Composable
fun PantallaAdministrador(
    modifier: Modifier = Modifier,
    onVolverClick: () -> Unit = {},
    viewModel: AdministradorViewModel = viewModel()
) {
    var pestanaSeleccionada by remember { mutableIntStateOf(0) }

    val solicitudesRemotas by viewModel.solicitudes.collectAsState()
    val emprendimientosRemotos by viewModel.emprendimientos.collectAsState()
    val clientesRemotos by viewModel.clientes.collectAsState()
    val emprendedorasRemotas by viewModel.emprendedoras.collectAsState()
    val administradoresRemotos by viewModel.administradores.collectAsState()

    val cargando by viewModel.cargando.collectAsState()
    val procesando by viewModel.procesando.collectAsState()
    val error by viewModel.error.collectAsState()
    val mensaje by viewModel.mensaje.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.cargarDatos()
    }

    val solicitudes = solicitudesRemotas.mapNotNull { emp ->
        val id = emp.id ?: return@mapNotNull null

        val responsable = emprendedorasRemotas.firstOrNull {
            it.id == emp.emprendedoraId
        }

        SolicitudAdmin(
            id = id,
            nombreEmprendimiento = emp.nombreTaller,
            categoria = emp.categoria.orEmpty(),
            comuna = emp.comuna.orEmpty(),
            descripcion = emp.descripcion.orEmpty(),
            responsable = responsable?.let {
                "${it.nombre} ${it.apellido}".trim()
            } ?: "No registrado",
            correo = responsable?.email ?: "No registrado",
            telefono = responsable?.whatsapp ?: "No registrado"
        )
    }

    val emprendimientos = emprendimientosRemotos.mapNotNull { emp ->
        val id = emp.id ?: return@mapNotNull null

        val responsable = emprendedorasRemotas.firstOrNull {
            it.id == emp.emprendedoraId
        }

        EmprendimientoAdmin(
            id = id,
            nombre = emp.nombreTaller,
            categoria = emp.categoria.orEmpty(),
            comuna = emp.comuna.orEmpty(),
            descripcion = emp.descripcion.orEmpty(),
            responsable = responsable?.let {
                "${it.nombre} ${it.apellido}".trim()
            } ?: "No registrado",
            correo = responsable?.email ?: "No registrado",
            telefono = responsable?.whatsapp ?: "No registrado",
            estado = "Aprobado"
        )
    }

    val cuentasRegistradas =
        clientesRemotos.mapNotNull { cliente ->
            val id = cliente.id ?: return@mapNotNull null

            CuentaRegistradaAdmin(
                id = "cliente_$id",
                nombre = "${cliente.nombre} ${cliente.apellido}".trim(),
                rol = "Cliente",
                correo = cliente.email,
                rut = "No aplica",
                telefono = cliente.telefono ?: "No registrado",
                direccion = "No registrada"
            )
        } + emprendedorasRemotas.mapNotNull { emprendedora ->
            val id = emprendedora.id ?: return@mapNotNull null

            CuentaRegistradaAdmin(
                id = "emprendedora_$id",
                nombre = "${emprendedora.nombre} ${emprendedora.apellido}".trim(),
                rol = "Emprendedor",
                correo = emprendedora.email,
                rut = emprendedora.rut,
                telefono = emprendedora.whatsapp ?: "No registrado",
                direccion = "No registrada"
            )
        } + administradoresRemotos.map { administrador ->
            CuentaRegistradaAdmin(
                id = "administrador_${administrador.id}",
                nombre = "Administrador",
                rol = "Administrador",
                correo = "No disponible",
                rut = "No registrado",
                telefono = "No registrado",
                direccion = "No registrada"
            )
        }

    val pestanas = listOf(
        "Solicitudes (${solicitudes.size})",
        "Emprendimientos (${emprendimientos.size})",
        "Cuentas (${cuentasRegistradas.size})"
    )

    var filtroRol by remember { mutableStateOf("Todos") }

    // Estados para diálogos
    var solicitudARechazar by remember { mutableStateOf<SolicitudAdmin?>(null) }
    var motivoRechazo by remember { mutableStateOf("") }

    var emprendimientoSeleccionado by remember { mutableStateOf<EmprendimientoAdmin?>(null) }
    var emprendimientoAEliminar by remember { mutableStateOf<EmprendimientoAdmin?>(null) }
    var motivoEliminacion by remember { mutableStateOf("Vocabulario inapropiado") }

    val mensajeEstado = error ?: mensaje

    Surface(
        modifier = modifier.fillMaxSize(),
        color = Fondo
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
        ) {
            // --- BARRA SUPERIOR ---
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onVolverClick,
                    enabled = !procesando && !cargando
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Volver",
                        tint = Turquesa
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Panel de Administrador",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = Negro
                )
            }

            // --- PESTAÑAS ---
            ScrollableTabRow(
                selectedTabIndex = pestanaSeleccionada,
                containerColor = Fondo,
                contentColor = Turquesa,
                edgePadding = 8.dp
            ) {
                pestanas.forEachIndexed { index, titulo ->
                    Tab(
                        selected = pestanaSeleccionada == index,
                        onClick = { pestanaSeleccionada = index },
                        text = {
                            Text(
                                text = titulo,
                                fontWeight = if (pestanaSeleccionada == index) FontWeight.Bold else FontWeight.Normal,
                                maxLines = 1
                            )
                        }
                    )
                }
            }

            // Aviso temporal de acción realizada
            if (mensajeEstado != null) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    colors = CardDefaults.cardColors(containerColor = Amarillo.copy(alpha = 0.35f)),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = mensajeEstado,
                            style = MaterialTheme.typography.bodySmall,
                            color = Negro,
                            modifier = Modifier.weight(1f)
                        )
                        TextButton(
                            onClick = {
                                viewModel.limpiarMensaje()
                                viewModel.limpiarError()
                            }
                        ) {
                            Text("OK", color = Turquesa, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            if (procesando) {
                LinearProgressIndicator(
                    modifier = Modifier.fillMaxWidth(),
                    color = Turquesa
                )
            }

            if (cargando) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Turquesa)
                }

                return@Column
            }

            if (error != null) {
                TextButton(
                    onClick = { viewModel.cargarDatos() },
                    enabled = !procesando,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    Text("Actualizar datos")
                }
            }

            // --- CONTENIDO SEGÚN PESTAÑA ---
            when (pestanaSeleccionada) {
                // 1. SOLICITUDES (verde y rojo)
                0 -> {
                    if (solicitudes.isEmpty()) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("No hay solicitudes pendientes.", color = Color.Gray)
                        }
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(solicitudes, key = { it.id }) { solicitud ->
                                Card(
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(16.dp),
                                    colors = CardDefaults.cardColors(containerColor = Color.White),
                                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                                ) {
                                    Column(modifier = Modifier.padding(16.dp)) {
                                        Text(
                                            text = solicitud.nombreEmprendimiento,
                                            style = MaterialTheme.typography.titleMedium,
                                            fontWeight = FontWeight.Bold,
                                            color = Turquesa
                                        )
                                        Text(
                                            text = "${solicitud.categoria} • ${solicitud.comuna}",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = Color.DarkGray
                                        )
                                        Spacer(modifier = Modifier.height(8.dp))
                                        Text(
                                            text = solicitud.descripcion,
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = Negro
                                        )
                                        Spacer(modifier = Modifier.height(12.dp))
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                                        ) {
                                            Button(
                                                onClick = {
                                                    viewModel.aprobarEmprendimiento(solicitud.id)
                                                },
                                                enabled = !procesando && !cargando,
                                                modifier = Modifier.weight(1f),
                                                colors = ButtonDefaults.buttonColors(containerColor = VerdeAprobado),
                                                shape = RoundedCornerShape(12.dp)
                                            ) {
                                                Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(18.dp))
                                                Spacer(modifier = Modifier.width(4.dp))
                                                Text("Aprobar")
                                            }

                                            OutlinedButton(
                                                onClick = {
                                                    solicitudARechazar = solicitud
                                                    motivoRechazo = ""
                                                },
                                                enabled = !procesando && !cargando,
                                                modifier = Modifier.weight(1f),
                                                border = BorderStroke(1.dp, RojoError),
                                                shape = RoundedCornerShape(12.dp)
                                            ) {
                                                Icon(Icons.Default.Close, contentDescription = null, tint = RojoError, modifier = Modifier.size(18.dp))
                                                Spacer(modifier = Modifier.width(4.dp))
                                                Text("Rechazar", color = RojoError)
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                // 2. EMPRENDIMIENTOS
                1 -> {
                    if (emprendimientos.isEmpty()) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("No hay emprendimientos activos.", color = Color.Gray)
                        }
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(emprendimientos, key = { it.id }) { emp ->
                                Card(
                                    onClick = { emprendimientoSeleccionado = emp },
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(16.dp),
                                    colors = CardDefaults.cardColors(containerColor = Color.White),
                                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                                ) {
                                    Column(modifier = Modifier.padding(16.dp)) {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text(
                                                text = emp.nombre,
                                                style = MaterialTheme.typography.titleMedium,
                                                fontWeight = FontWeight.Bold,
                                                color = Negro,
                                                modifier = Modifier.weight(1f)
                                            )
                                            Surface(
                                                color = Turquesa.copy(alpha = 0.14f),
                                                shape = RoundedCornerShape(8.dp)
                                            ) {
                                                Text(
                                                    text = emp.estado,
                                                    style = MaterialTheme.typography.labelMedium,
                                                    fontWeight = FontWeight.SemiBold,
                                                    color = Turquesa,
                                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                                                )
                                            }
                                        }

                                        Spacer(modifier = Modifier.height(6.dp))

                                        Text(
                                            text = "${emp.categoria} • ${emp.comuna}",
                                            style = MaterialTheme.typography.bodySmall,
                                            fontWeight = FontWeight.Medium,
                                            color = Turquesa
                                        )

                                        Spacer(modifier = Modifier.height(8.dp))

                                        Text(
                                            text = emp.descripcion,
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = Negro
                                        )

                                        Spacer(modifier = Modifier.height(10.dp))

                                        Text(
                                            text = "Toca para ver opciones",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = Color.Gray
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                // 3. CUENTAS REGISTRADAS (Cliente, Emprendedor y Administrador)
                2 -> {
                    val cuentasFiltradas = cuentasRegistradas.filter {
                        filtroRol == "Todos" || it.rol == filtroRol
                    }

                    Column(modifier = Modifier.fillMaxSize()) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .horizontalScroll(rememberScrollState())
                                .padding(horizontal = 16.dp, vertical = 12.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            listOf("Todos", "Cliente", "Emprendedor", "Administrador").forEach { rol ->
                                FilterChip(
                                    selected = filtroRol == rol,
                                    onClick = { filtroRol = rol },
                                    label = { Text(rol) },
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = Turquesa,
                                        selectedLabelColor = Color.White
                                    )
                                )
                            }
                        }

                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(
                                start = 16.dp,
                                end = 16.dp,
                                bottom = 24.dp
                            ),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(cuentasFiltradas, key = { it.id }) { cuenta ->
                                val (fondoRol, textoRol) = when (cuenta.rol) {
                                    "Administrador" -> Negro to Amarillo
                                    "Emprendedor" -> Turquesa.copy(alpha = 0.15f) to Turquesa
                                    else -> Amarillo.copy(alpha = 0.35f) to Negro
                                }

                                Card(
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(16.dp),
                                    colors = CardDefaults.cardColors(containerColor = Color.White),
                                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                                ) {
                                    Column(modifier = Modifier.padding(16.dp)) {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Row(
                                                verticalAlignment = Alignment.CenterVertically,
                                                modifier = Modifier.weight(1f)
                                            ) {
                                                Box(
                                                    modifier = Modifier
                                                        .size(42.dp)
                                                        .clip(CircleShape)
                                                        .background(if (cuenta.rol == "Administrador") Negro else Amarillo),
                                                    contentAlignment = Alignment.Center
                                                ) {
                                                    Icon(
                                                        imageVector = Icons.Default.Person,
                                                        contentDescription = null,
                                                        tint = if (cuenta.rol == "Administrador") Amarillo else Negro,
                                                        modifier = Modifier.size(24.dp)
                                                    )
                                                }
                                                Spacer(modifier = Modifier.width(12.dp))
                                                Text(
                                                    text = cuenta.nombre,
                                                    style = MaterialTheme.typography.titleMedium,
                                                    fontWeight = FontWeight.Bold,
                                                    color = Negro
                                                )
                                            }

                                            Spacer(modifier = Modifier.width(8.dp))

                                            Surface(
                                                color = fondoRol,
                                                shape = RoundedCornerShape(8.dp)
                                            ) {
                                                Text(
                                                    text = cuenta.rol,
                                                    style = MaterialTheme.typography.labelMedium,
                                                    fontWeight = FontWeight.Bold,
                                                    color = textoRol,
                                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                                )
                                            }
                                        }

                                        HorizontalDivider(
                                            modifier = Modifier.padding(vertical = 10.dp),
                                            color = Color.LightGray.copy(alpha = 0.5f)
                                        )

                                        DatoFilaAdmin(etiqueta = "Correo:", valor = cuenta.correo)
                                        Spacer(modifier = Modifier.height(4.dp))
                                        DatoFilaAdmin(etiqueta = "RUT:", valor = cuenta.rut)
                                        Spacer(modifier = Modifier.height(4.dp))
                                        DatoFilaAdmin(etiqueta = "Teléfono:", valor = cuenta.telefono)
                                        Spacer(modifier = Modifier.height(4.dp))
                                        DatoFilaAdmin(etiqueta = "Dirección:", valor = cuenta.direccion)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // --- DIÁLOGO DETALLE Y OPCIONES DE EMPRENDIMIENTO ---
    if (emprendimientoSeleccionado != null) {
        val emp = emprendimientoSeleccionado!!
        AlertDialog(
            onDismissRequest = { emprendimientoSeleccionado = null },
            title = {
                Text(
                    text = emp.nombre,
                    fontWeight = FontWeight.Bold,
                    color = Turquesa
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    DatoFilaAdmin(etiqueta = "Estado:", valor = emp.estado)
                    DatoFilaAdmin(etiqueta = "Categoría:", valor = emp.categoria)
                    DatoFilaAdmin(etiqueta = "Comuna:", valor = emp.comuna)
                    DatoFilaAdmin(etiqueta = "Encargada:", valor = emp.responsable)
                    DatoFilaAdmin(etiqueta = "Correo:", valor = emp.correo)
                    DatoFilaAdmin(etiqueta = "Teléfono:", valor = emp.telefono)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Descripción:",
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Bold,
                        color = Color.DarkGray
                    )
                    Text(
                        text = emp.descripcion,
                        style = MaterialTheme.typography.bodySmall,
                        color = Negro
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        emprendimientoAEliminar = emp
                        motivoEliminacion = "Vocabulario inapropiado"
                        emprendimientoSeleccionado = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = RojoError)
                ) {
                    Text("Eliminar")
                }
            },
            dismissButton = {
                TextButton(onClick = { emprendimientoSeleccionado = null }) {
                    Text("Cerrar", color = Negro)
                }
            }
        )
    }

    // --- DIÁLOGO RECHAZAR SOLICITUD ---
    if (solicitudARechazar != null) {
        AlertDialog(
            onDismissRequest = { solicitudARechazar = null },
            title = { Text("Rechazar solicitud", fontWeight = FontWeight.Bold) },
            text = {
                Column {
                    Text("Indica el motivo del rechazo para '${solicitudARechazar!!.nombreEmprendimiento}':")
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = motivoRechazo,
                        onValueChange = { motivoRechazo = it },
                        placeholder = { Text("Ej: Falta información de contacto") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        solicitudARechazar?.let { solicitud ->
                            viewModel.rechazarEmprendimiento(
                                id = solicitud.id,
                                motivo = motivoRechazo
                            )
                        }

                        solicitudARechazar = null
                    },
                    enabled = !procesando &&
                            !cargando &&
                            motivoRechazo.isNotBlank(),
                    colors = ButtonDefaults.buttonColors(containerColor = RojoError)
                ) {
                    Text("Confirmar rechazo")
                }
            },
            dismissButton = {
                TextButton(onClick = { solicitudARechazar = null }) {
                    Text("Cancelar", color = Negro)
                }
            }
        )
    }

    // --- DIÁLOGO ELIMINAR EMPRENDIMIENTO ---
    if (emprendimientoAEliminar != null) {
        val motivosComunes = listOf(
            "Vocabulario inapropiado",
            "Información falsa",
            "Incumplimiento de normas"
        )
        AlertDialog(
            onDismissRequest = { emprendimientoAEliminar = null },
            title = { Text("Eliminar emprendimiento", fontWeight = FontWeight.Bold) },
            text = {
                Column {
                    Text("Selecciona el motivo para eliminar '${emprendimientoAEliminar!!.nombre}':")
                    Spacer(modifier = Modifier.height(8.dp))
                    motivosComunes.forEach { motivo ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            RadioButton(
                                selected = motivoEliminacion == motivo,
                                onClick = { motivoEliminacion = motivo }
                            )
                            Text(motivo, style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        emprendimientoAEliminar?.let { emprendimiento ->
                            viewModel.eliminarEmprendimiento(emprendimiento.id)
                        }

                        emprendimientoAEliminar = null
                    },
                    enabled = !procesando && !cargando,
                    colors = ButtonDefaults.buttonColors(containerColor = RojoError)
                ) {
                    Text("Confirmar eliminación")
                }
            },
            dismissButton = {
                TextButton(onClick = { emprendimientoAEliminar = null }) {
                    Text("Cancelar", color = Negro)
                }
            }
        )
    }
}

@Composable
private fun DatoFilaAdmin(etiqueta: String, valor: String) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = etiqueta,
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.Bold,
            color = Color.DarkGray,
            modifier = Modifier.weight(0.32f)
        )
        Text(
            text = valor,
            style = MaterialTheme.typography.bodySmall,
            color = Negro,
            modifier = Modifier.weight(0.68f)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PantallaAdministradorPreview() {
    FundacionTextilTheme {
        PantallaAdministrador()
    }
}