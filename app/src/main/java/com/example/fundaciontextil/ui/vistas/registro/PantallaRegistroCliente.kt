package com.example.fundaciontextil.ui.vistas.registro

import android.Manifest
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.fundaciontextil.ui.components.CampoTexto
import com.example.fundaciontextil.ui.components.ImagenRegistro
import com.example.fundaciontextil.ui.components.SeccionTitulo
import com.example.fundaciontextil.viewmodel.RegistroClienteViewModel
import java.io.File

private val Turquesa = Color(0xFF168C8C)
private val Amarillo = Color(0xFFFFD84D)
private val Fondo = Color(0xFFFFFEFA)
private val Negro = Color(0xFF202020)

@Composable
fun PantallaRegistroCliente(
    modifier: Modifier = Modifier,
    alVolver: () -> Unit = {},
    alRegistroExitoso: (Long) -> Unit = {},
    viewModel: RegistroClienteViewModel = viewModel()
) {
    val estado by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(estado.guardadoExitoso) {
        estado.guardadoExitoso?.let { id ->
            alRegistroExitoso(id)
            viewModel.consumirGuardado()
        }
    }

    val archivoFotoTemporal = rememberSaveable(
        saver = Saver<MutableState<File?>, String>(
            save = { it.value?.absolutePath ?: "" },
            restore = { mutableStateOf(if (it.isNotEmpty()) File(it) else null) }
        )
    ) { mutableStateOf<File?>(null) }

    fun crearUriParaCamara(): Uri? {
        val dir = File(context.filesDir, "fotos").apply { mkdirs() }
        val archivo = File(dir, "perfil_cliente_${System.currentTimeMillis()}.jpg")
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
                text = "Crear cuenta de cliente",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = Negro,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.End
            )
            Spacer(Modifier.width(8.dp))
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            SeccionTitulo("Foto de perfil")
            Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                Box(
                    modifier = Modifier.size(160.dp).clip(CircleShape).background(Color(0xFFE6F4F4)).border(3.dp, Turquesa, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    ImagenRegistro(fotoUri = estado.fotoUri, modifier = Modifier.size(150.dp))
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
            CampoTexto(
                valor = estado.nombre,
                onValorCambio = viewModel::actualizarNombre,
                label = "Nombre",
                placeholder = "Tu nombre",
                error = estado.nombreError
            )
            CampoTexto(
                valor = estado.apellido,
                onValorCambio = viewModel::actualizarApellido,
                label = "Apellido",
                placeholder = "Tu apellido",
                error = estado.apellidoError
            )
            CampoTexto(
                valor = estado.email,
                onValorCambio = viewModel::actualizarEmail,
                label = "Correo electrónico",
                placeholder = "ejemplo@correo.com",
                error = estado.emailError,
                keyboardType = KeyboardType.Email
            )
            CampoTexto(
                valor = estado.telefono,
                onValorCambio = viewModel::actualizarTelefono,
                label = "Teléfono (opcional)",
                placeholder = "Ej: +56 9 1234 5678",
                error = estado.telefonoError,
                keyboardType = KeyboardType.Phone
            )

            SeccionTitulo("Seguridad")
            CampoTexto(
                valor = estado.contrasenia,
                onValorCambio = viewModel::actualizarContrasenia,
                label = "Contraseña",
                placeholder = "Mínimo 6 caracteres",
                error = estado.contraseniaError,
                visualTransformation = PasswordVisualTransformation()
            )
            CampoTexto(
                valor = estado.confirmarContrasenia,
                onValorCambio = viewModel::actualizarConfirmarContrasenia,
                label = "Confirmar contraseña",
                placeholder = "Repite tu contraseña",
                error = estado.confirmarContraseniaError,
                visualTransformation = PasswordVisualTransformation()
            )

            Spacer(Modifier.height(8.dp))

            Button(
                onClick = { viewModel.guardar() },
                enabled = !estado.guardando,
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Amarillo, contentColor = Negro)
            ) {
                if (estado.guardando) {
                    CircularProgressIndicator(color = Negro, modifier = Modifier.size(22.dp), strokeWidth = 2.dp)
                    Spacer(Modifier.width(12.dp))
                    Text("Guardando...")
                } else {
                    Text("Crear mi cuenta", fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
                }
            }

            Spacer(Modifier.height(24.dp))
        }
    }
}

// Funcion para copiar el archivo de la galeria al almacenamiento interno
private fun copiarUriAStorage(context: Context, origen: Uri): String? {
    return try {
        val dir = File(context.filesDir, "fotos").apply { mkdirs() }
        val destino = File(dir, "perfil_cliente_${System.currentTimeMillis()}.jpg")
        context.contentResolver.openInputStream(origen)?.use { input ->
            destino.outputStream().use { output -> input.copyTo(output) }
        }
        destino.absolutePath
    } catch (e: Exception) { null }
}