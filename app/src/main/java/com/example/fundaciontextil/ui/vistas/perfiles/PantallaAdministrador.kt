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

private val Turquesa = Color(0xFF168C8C)
private val Amarillo = Color(0xFFFFD84D)
private val Fondo = Color(0xFFFFFEFA)
private val Negro = Color(0xFF202020)
private val RojoError = Color(0xFFD32F2F)
private val VerdeAprobado = Color(0xFF2E7D32)

data class SolicitudAdmin(
    val id: Int,
    val nombreEmprendimiento: String,
    val categoria: String,
    val comuna: String,
    val descripcion: String,
    val responsable: String = "Por confirmar",
    val correo: String = "contacto@emprendimiento.cl",
    val telefono: String = "+56 9 0000 0000"
)

data class EmprendimientoAdmin(
    val id: Int,
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
    val id: Int,
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
    onVolverClick: () -> Unit = {}
) {
    var pestanaSeleccionada by remember { mutableIntStateOf(0) }

    // Datos de ejemplo para Solicitudes pendientes
    var solicitudes by remember {
        mutableStateOf(
            listOf(
                SolicitudAdmin(
                    id = 1,
                    nombreEmprendimiento = "Retazos del Sur",
                    categoria = "Upcycling / Bolsos",
                    comuna = "Viña del Mar",
                    descripcion = "Confección de bolsos y mochilas con mezclilla reciclada.",
                    responsable = "Valentina Pérez",
                    correo = "retazosdelsur@gmail.com",
                    telefono = "+56 9 6123 4567"
                ),
                SolicitudAdmin(
                    id = 2,
                    nombreEmprendimiento = "Hilo Vivo Taller",
                    categoria = "Reparación textil",
                    comuna = "Providencia",
                    descripcion = "Arreglos y transformación de prendas en desuso.",
                    responsable = "Sofía Muñoz",
                    correo = "hilovivo.taller@gmail.com",
                    telefono = "+56 9 7234 5678"
                )
            )
        )
    }

    // Datos de ejemplo para Emprendimientos publicados
    var emprendimientos by remember {
        mutableStateOf(
            listOf(
                EmprendimientoAdmin(
                    id = 1,
                    nombre = "Moda Circular Quilpué",
                    categoria = "Upcycling",
                    comuna = "Quilpué",
                    descripcion = "Chaquetas de mezclilla intervenidas a mano.",
                    responsable = "Daniela Soto",
                    correo = "modacircular.quilpue@gmail.com",
                    telefono = "+56 9 8111 2233"
                ),
                EmprendimientoAdmin(
                    id = 2,
                    nombre = "Costura Sustentable",
                    categoria = "Reparaciones",
                    comuna = "Quillota",
                    descripcion = "Arreglos, zurcido visible y transformación de prendas.",
                    responsable = "Marcela Díaz",
                    correo = "costurasustentable@gmail.com",
                    telefono = "+56 9 9444 5566"
                )
            )
        )
    }

    // Datos de ejemplo para Cuentas registradas (incluye Administradores)
    val cuentasRegistradas = remember {
        listOf(
            CuentaRegistradaAdmin(
                id = 1,
                nombre = "Lamine Yabien",
                rol = "Cliente",
                correo = "LamineYabien777@gmail.com",
                rut = "12.345.678-9",
                telefono = "+56 9 3333 2387",
                direccion = "Calle Tutancamon 418, Providencia"
            ),
            CuentaRegistradaAdmin(
                id = 2,
                nombre = "Harry Kane",
                rol = "Emprendedor",
                correo = "HarryKane@gmail.com",
                rut = "19.313.298-K",
                telefono = "+56 9 3113 7689",
                direccion = "Calle Machu Picchu 3001, Providencia"
            ),
            CuentaRegistradaAdmin(
                id = 3,
                nombre = "Camila Rojas",
                rol = "Cliente",
                correo = "camila.rojas@gmail.com",
                rut = "20.456.123-4",
                telefono = "+56 9 8765 4321",
                direccion = "Av. Libertad 1020, Viña del Mar"
            ),
            CuentaRegistradaAdmin(
                id = 4,
                nombre = "Taller Textil Circular",
                rol = "Emprendedor",
                correo = "contacto@textilcircular.cl",
                rut = "76.543.210-8",
                telefono = "+56 9 5544 3322",
                direccion = "Calle O'Higgins 450, Quillota"
            ),
            CuentaRegistradaAdmin(
                id = 5,
                nombre = "Admin Fundación",
                rol = "Administrador",
                correo = "admin@textilcircular.cl",
                rut = "18.765.432-1",
                telefono = "+56 9 9000 1122",
                direccion = "Sede Central, Viña del Mar"
            ),
            CuentaRegistradaAdmin(
                id = 6,
                nombre = "Coordinación Textil",
                rol = "Administrador",
                correo = "coordinacion@textilcircular.cl",
                rut = "17.234.567-5",
                telefono = "+56 9 9000 3344",
                direccion = "Sede Central, Valparaíso"
            )
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

    var mensajeEstado by remember { mutableStateOf<String?>(null) }

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
                IconButton(onClick = onVolverClick) {
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
                            text = mensajeEstado!!,
                            style = MaterialTheme.typography.bodySmall,
                            color = Negro,
                            modifier = Modifier.weight(1f)
                        )
                        TextButton(onClick = { mensajeEstado = null }) {
                            Text("OK", color = Turquesa, fontWeight = FontWeight.Bold)
                        }
                    }
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
                                                    emprendimientos = emprendimientos + EmprendimientoAdmin(
                                                        id = (emprendimientos.maxOfOrNull { it.id } ?: 0) + 1,
                                                        nombre = solicitud.nombreEmprendimiento,
                                                        categoria = solicitud.categoria,
                                                        comuna = solicitud.comuna,
                                                        descripcion = solicitud.descripcion,
                                                        responsable = solicitud.responsable,
                                                        correo = solicitud.correo,
                                                        telefono = solicitud.telefono
                                                    )
                                                    solicitudes = solicitudes.filterNot { it.id == solicitud.id }
                                                    mensajeEstado = "Solicitud '${solicitud.nombreEmprendimiento}' aprobada."
                                                },
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
                        val nombre = solicitudARechazar!!.nombreEmprendimiento
                        solicitudes = solicitudes.filterNot { it.id == solicitudARechazar!!.id }
                        mensajeEstado = "Solicitud '$nombre' rechazada. Motivo: ${motivoRechazo.ifBlank { "No especificado" }}"
                        solicitudARechazar = null
                    },
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
                        val nombre = emprendimientoAEliminar!!.nombre
                        emprendimientos = emprendimientos.filterNot { it.id == emprendimientoAEliminar!!.id }
                        mensajeEstado = "'$nombre' eliminado ($motivoEliminacion)."
                        emprendimientoAEliminar = null
                    },
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