package com.example.fundaciontextil.ui.vistas.registro

import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.fundaciontextil.ui.components.CampoTexto
import com.example.fundaciontextil.ui.components.ImagenRegistro
import com.example.fundaciontextil.viewmodel.RegistroClienteViewModel

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
    val contexto = LocalContext.current

    // OpenDocument para URI persistente (sobrevive reinicios de la app)
    val selectorImagen = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri ->
        if (uri != null) {
            try {
                contexto.contentResolver.takePersistableUriPermission(
                    uri,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION
                )
            } catch (_: SecurityException) { /* Si proveedor no soporta persistencia */ }
            viewModel.actualizarFoto(uri.toString())
        }
    }

    LaunchedEffect(estado.guardadoExitoso) {
        estado.guardadoExitoso?.let { id ->
            alRegistroExitoso(id)
            viewModel.consumirGuardado()
        }
    }

    Surface(modifier = modifier.fillMaxSize(), color = Fondo) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TextButton(
                onClick = alVolver,
                modifier = Modifier.align(Alignment.Start)
            ) {
                Text("← Volver", color = Turquesa)
            }

            Text(
                text = "Crear cuenta de cliente",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = Negro
            )

            Spacer(Modifier.height(20.dp))

            // Foto de perfil opcional
            Box(
                modifier = Modifier
                    .size(150.dp)
                    .clickable { selectorImagen.launch(arrayOf("image/*")) },
                contentAlignment = Alignment.Center
            ) {
                ImagenRegistro(fotoUri = estado.fotoUri)
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(Turquesa),
                    contentAlignment = Alignment.Center
                ) {
                    Text("+", color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Bold)
                }
            }
            Spacer(Modifier.height(6.dp))
            Text(
                text = "Toca la foto para agregarla (opcional)",
                fontSize = 12.sp,
                color = Color.Gray
            )

            Spacer(Modifier.height(24.dp))

            CampoTexto(
                valor = estado.nombre,
                onValorCambio = viewModel::actualizarNombre,
                label = "Nombre",
                placeholder = "Tu nombre",
                error = estado.nombreError
            )
            Spacer(Modifier.height(12.dp))

            CampoTexto(
                valor = estado.apellido,
                onValorCambio = viewModel::actualizarApellido,
                label = "Apellido",
                placeholder = "Tu apellido",
                error = estado.apellidoError
            )
            Spacer(Modifier.height(12.dp))

            CampoTexto(
                valor = estado.email,
                onValorCambio = viewModel::actualizarEmail,
                label = "Correo electrónico",
                placeholder = "ejemplo@correo.com",
                error = estado.emailError,
                keyboardType = KeyboardType.Email
            )
            Spacer(Modifier.height(12.dp))

            CampoTexto(
                valor = estado.contrasenia,
                onValorCambio = viewModel::actualizarContrasenia,
                label = "Contraseña",
                placeholder = "Mínimo 6 caracteres",
                error = estado.contraseniaError,
                visualTransformation = PasswordVisualTransformation()
            )
            Spacer(Modifier.height(12.dp))

            CampoTexto(
                valor = estado.confirmarContrasenia,
                onValorCambio = viewModel::actualizarConfirmarContrasenia,
                label = "Confirmar contraseña",
                placeholder = "Repite tu contraseña",
                error = estado.confirmarContraseniaError,
                visualTransformation = PasswordVisualTransformation()
            )
            Spacer(Modifier.height(12.dp))

            CampoTexto(
                valor = estado.telefono,
                onValorCambio = viewModel::actualizarTelefono,
                label = "Teléfono (opcional)",
                placeholder = "Ej: +56 9 1234 5678",
                error = estado.telefonoError,
                keyboardType = KeyboardType.Phone
            )

            Spacer(Modifier.height(28.dp))

            Button(
                onClick = viewModel::guardar,
                enabled = !estado.guardando,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Amarillo,
                    contentColor = Negro
                )
            ) {
                if (estado.guardando) {
                    CircularProgressIndicator(
                        color = Negro,
                        modifier = Modifier.size(24.dp),
                        strokeWidth = 2.dp
                    )
                } else {
                    Text(
                        text = "Crear mi cuenta",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            Spacer(Modifier.height(24.dp))
        }
    }
}