package com.example.fundaciontextil.ui.vistas.login

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.fundaciontextil.ui.components.CampoTexto
import com.example.fundaciontextil.viewmodel.LoginViewModel

private val Turquesa = Color(0xFF168C8C)
private val Fondo = Color(0xFFFFFEFA)
private val Negro = Color(0xFF202020)

@Composable
fun PantallaLogin(
    alVolver: () -> Unit,
    alLoginExitoso: () -> Unit,
    viewModel: LoginViewModel = viewModel()
) {
    val rut by viewModel.rut.collectAsState()
    val contrasenia by viewModel.contrasenia.collectAsState()
    val error by viewModel.error.collectAsState()
    val cargando by viewModel.cargando.collectAsState()
    val loginExitoso by viewModel.loginExitoso.collectAsState()

    LaunchedEffect(loginExitoso) {
        if (loginExitoso != null) {
            alLoginExitoso()
            viewModel.consumirLogin()
        }
    }

    Column(
        modifier = Modifier.fillMaxSize().background(Fondo).padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Iniciar Sesión",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = Negro
        )
        Spacer(Modifier.height(32.dp))

        CampoTexto(
            valor = rut,
            onValorCambio = viewModel::actualizarRut,
            label = "RUT",
            placeholder = "Ej: 12345678-9",
            error = null
        )
        Spacer(Modifier.height(16.dp))

        CampoTexto(
            valor = contrasenia,
            onValorCambio = viewModel::actualizarContrasenia,
            label = "Contraseña",
            placeholder = "Tu contraseña",
            error = null,
            visualTransformation = PasswordVisualTransformation()
        )

        if (error != null) {
            Spacer(Modifier.height(8.dp))
            Text(text = error!!, color = Color.Red, fontSize = 13.sp)
        }

        Spacer(Modifier.height(32.dp))

        Button(
            onClick = { viewModel.iniciarSesion() },
            enabled = !cargando,
            modifier = Modifier.fillMaxWidth().height(56.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Turquesa)
        ) {
            if (cargando) {
                CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp), strokeWidth = 2.dp)
            } else {
                Text("Ingresar", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }
        }

        Spacer(Modifier.height(16.dp))

        TextButton(onClick = alVolver) {
            Text("Volver al inicio", color = Turquesa)
        }
    }
}