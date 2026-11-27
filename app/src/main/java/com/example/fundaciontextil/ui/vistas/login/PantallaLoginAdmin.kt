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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.fundaciontextil.ui.components.CampoTexto
import com.example.fundaciontextil.viewmodel.LoginAdminViewModel

private val Negro = Color(0xFF202020)
private val Fondo = Color(0xFFFFFEFA)

@Composable
fun PantallaLoginAdmin(
    modifier: Modifier = Modifier,
    alVolver: () -> Unit = {},
    alLoginExitoso: () -> Unit = {},
    viewModel: LoginAdminViewModel = viewModel()
) {
    val email by viewModel.email.collectAsState()
    val contrasenia by viewModel.contrasenia.collectAsState()
    val error by viewModel.error.collectAsState()
    val loginExitoso by viewModel.loginExitoso.collectAsState()

    LaunchedEffect(loginExitoso) {
        if (loginExitoso) {
            alLoginExitoso()
            viewModel.consumirLogin()
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Fondo)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Acceso Administrador",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = Negro
        )

        Spacer(Modifier.height(8.dp))

        Text(
            text = "Ingresa tus credenciales para gestionar la plataforma.",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray
        )

        Spacer(Modifier.height(32.dp))

        CampoTexto(
            valor = email,
            onValorCambio = viewModel::actualizarEmail,
            label = "Correo de administrador",
            placeholder = "admin@fundaciontextil.cl",
            error = null,
            keyboardType = KeyboardType.Email
        )

        Spacer(Modifier.height(16.dp))

        CampoTexto(
            valor = contrasenia,
            onValorCambio = viewModel::actualizarContrasenia,
            label = "Contraseña",
            placeholder = "••••••••",
            error = null,
            visualTransformation = PasswordVisualTransformation()
        )

        if (error != null) {
            Spacer(Modifier.height(8.dp))
            Text(
                text = error!!,
                color = MaterialTheme.colorScheme.error,
                fontSize = 13.sp
            )
        }

        Spacer(Modifier.height(32.dp))

        Button(
            onClick = { viewModel.iniciarSesion() },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Negro,
                contentColor = Color.White
            )
        ) {
            Text("Ingresar al panel", fontWeight = FontWeight.Bold, fontSize = 16.sp)
        }

        Spacer(Modifier.height(16.dp))

        TextButton(onClick = alVolver) {
            Text("Volver", color = Negro)
        }
    }
}