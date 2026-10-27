package com.example.fundaciontextil.ui.vistas.registro

import android.Manifest
import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState // <-- IMPORT AGREGADO
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll // <-- IMPORT AGREGADO
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.fundaciontextil.model.ChileData
import com.example.fundaciontextil.ui.components.CampoTexto
import com.example.fundaciontextil.ui.components.ImagenRegistro
import com.example.fundaciontextil.ui.components.SeccionTitulo
import com.example.fundaciontextil.ui.components.SelectorDesplegable
import com.example.fundaciontextil.viewmodel.RegistroViewModel
import java.io.File

private val Turquesa = Color(0xFF168C8C)
private val Fondo = Color(0xFFFFFEFA)
private val Negro = Color(0xFF202020)

@Composable
fun RegistroScreen(
    modifier: Modifier = Modifier,
    alVolver: () -> Unit = {},
    alGuardarExitoso: (Long) -> Unit = {},
    viewModel: RegistroViewModel = viewModel(
        factory = RegistroViewModel.factory(
            LocalContext.current.applicationContext as Application
        )
    )
) {
    val state by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(state.guardadoExitoso, state.idGuardado) {
        if (state.guardadoExitoso) {
            state.idGuardado?.let { alGuardarExitoso(it) }
            viewModel.consumirGuardado()
        }
    }

    val archivoFotoTemporal = remember { mutableStateOf<File?>(null) }

    fun crearUriParaCamara(): Uri? {
        val dir = File(context.filesDir, "fotos").apply { mkdirs() }
        val archivo = File(dir, "perfil_${System.currentTimeMillis()}.jpg")
        archivoFotoTemporal.value = archivo
        return FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            archivo
        )
    }

    val camaraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { ok ->
        if (ok) archivoFotoTemporal.value?.let { viewModel.actualizarFoto(it.absolutePath) }
    }

    val camaraPermisoLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { concedido ->
        if (concedido) crearUriParaCamara()?.let { camaraLauncher.launch(it) }
    }

    val galeriaLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            val path = copiarUriAStorage(context, it)
            if (path != null) viewModel.actualizarFoto(path)
        }
    }

    fun abrirCamara() {
        val permiso = ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
        if (permiso == PackageManager.PERMISSION_GRANTED) {
            crearUriParaCamara()?.let { camaraLauncher.launch(it) }
        } else {
            camaraPermisoLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    Column(modifier = modifier.fillMaxSize().background(Fondo)) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextButton(onClick = alVolver) { Text("<- Volver", color = Turquesa) }
            Text(
                text = "Crear mi ficha",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = Negro,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.End
            )
            Spacer(Modifier.width(8.dp))
        }

        // optimizacion:  Se cambio LazyColumn por Column + verticalScroll
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            SeccionTitulo("Foto de perfil / taller")
            Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                Box(
                    modifier = Modifier.size(160.dp).clip(CircleShape).background(Color(0xFFE6F4F4)).border(3.dp, Turquesa, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    ImagenRegistro(fotoUri = state.fotoUri, modifier = Modifier.size(150.dp))
                }
                Spacer(Modifier.height(12.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    OutlinedButton(onClick = { abrirCamara() }, shape = RoundedCornerShape(12.dp)) {
                        Text("Cámara", color = Turquesa)
                    }
                    OutlinedButton(onClick = { galeriaLauncher.launch("image/*") }, shape = RoundedCornerShape(12.dp)) {
                        Text("Galería", color = Turquesa)
                    }
                }
            }

            SeccionTitulo("Datos personales")
            CampoTexto(valor = state.rut, onValorCambio = viewModel::actualizarRut, label = "RUT", placeholder = "Ej: 12345678-9", error = state.errores.rut)
            CampoTexto(valor = state.nombre, onValorCambio = viewModel::actualizarNombre, label = "Nombre completo", placeholder = "Ej: María González", error = state.errores.nombre)
            SelectorDesplegable(label = "Rango etario", valor = state.rangoEtario, opciones = ChileData.rangosEtarios, onSeleccionar = viewModel::actualizarRangoEtario, error = state.errores.rangoEtario)
            SelectorDesplegable(label = "Género", valor = state.genero, opciones = ChileData.generos, onSeleccionar = viewModel::actualizarGenero, error = state.errores.genero)

            SeccionTitulo("Datos del emprendimiento")
            CampoTexto(valor = state.nombreTaller, onValorCambio = viewModel::actualizarNombreTaller, label = "Nombre del taller / emprendimiento", placeholder = "Ej: Taller Raíz", error = state.errores.nombreTaller)
            OutlinedTextField(
                value = state.descripcion,
                onValueChange = viewModel::actualizarDescripcion,
                label = { Text("¿Qué hace tu emprendimiento?") },
                placeholder = { Text("Breve descripción (máx. 3 líneas)") },
                minLines = 3, maxLines = 3,
                isError = state.errores.descripcion != null,
                supportingText = { Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) { Text(state.errores.descripcion ?: ""); Text("${state.descripcion.length}/300") } },
                modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp)
            )
            SelectorDesplegable(label = "Región", valor = state.region, opciones = ChileData.regiones, onSeleccionar = viewModel::actualizarRegion, error = state.errores.region)
            SelectorDesplegable(label = "Comuna", valor = state.comuna, opciones = state.comunasDisponibles, onSeleccionar = viewModel::actualizarComuna, habilitado = state.region.isNotBlank(), error = state.errores.comuna)

            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text("Rubros textiles (máx. 2)", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold, color = Negro)
                ChileData.rubrosTextiles.forEach { rubro ->
                    val marcado = rubro in state.rubros
                    Row(
                        modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(10.dp)).background(if (marcado) Color(0xFFE6F4F4) else Color.Transparent).padding(horizontal = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(checked = marcado, onCheckedChange = { viewModel.alternarRubro(rubro) }, colors = CheckboxDefaults.colors(checkedColor = Turquesa))
                        Text(rubro, style = MaterialTheme.typography.bodyMedium)
                    }
                }
                if (state.errores.rubros != null) Text(state.errores.rubros!!, color = MaterialTheme.colorScheme.error, fontSize = 12.sp)
            }

            SeccionTitulo("Canales de contacto")
            CampoTexto(valor = state.whatsapp, onValorCambio = viewModel::actualizarWhatsapp, label = "WhatsApp", placeholder = "+56912345678", error = state.errores.whatsapp, keyboardType = KeyboardType.Phone)
            CampoTexto(valor = state.email, onValorCambio = viewModel::actualizarEmail, label = "Correo electrónico", placeholder = "correo@dominio.cl", error = state.errores.email, keyboardType = KeyboardType.Email)

            SeccionTitulo("Consentimiento legal")
            Column {
                Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.Top) {
                    Checkbox(checked = state.consentimiento, onCheckedChange = viewModel::actualizarConsentimiento, colors = CheckboxDefaults.colors(checkedColor = Turquesa))
                    Text("Autorizo el tratamiento de mis datos personales conforme a las Leyes N.º 21.719 y 19.628 de Chile.", style = MaterialTheme.typography.bodySmall, modifier = Modifier.padding(top = 12.dp))
                }
                if (state.errores.consentimiento != null) Text(state.errores.consentimiento!!, color = MaterialTheme.colorScheme.error, fontSize = 12.sp)
            }

            Column {
                if (state.errorGeneral != null) {
                    Text(state.errorGeneral!!, color = MaterialTheme.colorScheme.error, fontSize = 13.sp)
                    Spacer(Modifier.height(8.dp))
                }
                Button(
                    onClick = { viewModel.guardar() },
                    enabled = !state.guardando,
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Turquesa, contentColor = Color.White)
                ) {
                    if (state.guardando) {
                        CircularProgressIndicator(color = Color.White, modifier = Modifier.size(22.dp), strokeWidth = 2.dp)
                        Spacer(Modifier.width(12.dp))
                        Text("Guardando...")
                    } else {
                        Text("Guardar ficha", fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
                    }
                }
                Spacer(Modifier.height(24.dp))
            }
        }
    }
}

private fun copiarUriAStorage(context: Context, origen: Uri): String? {
    return try {
        val dir = File(context.filesDir, "fotos").apply { mkdirs() }
        val destino = File(dir, "perfil_${System.currentTimeMillis()}.jpg")
        context.contentResolver.openInputStream(origen)?.use { input ->
            destino.outputStream().use { output -> input.copyTo(output) }
        }
        destino.absolutePath
    } catch (e: Exception) { null }
}