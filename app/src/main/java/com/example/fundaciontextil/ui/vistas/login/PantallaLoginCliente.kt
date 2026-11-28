package com.example.fundaciontextil.ui.vistas.login

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import com.example.fundaciontextil.viewmodel.LoginClienteViewModel

private val Turquesa = Color(0xFF168C8C)
private val Amarillo = Color(0xFFFFD84D)
private val Fondo = Color(0xFFFFFEFA)
private val Negro = Color(0xFF202020)

@Composable
fun PantallaLoginCliente(
    modifier: Modifier = Modifier,
    alVolver: () -> Unit = {},
    alLoginExitoso: () -> Unit = {},
    viewModel: LoginClienteViewModel = viewModel()
) {
    val email by viewModel.email.collectAsState()
    val contrasenia by viewModel.contrasenia.collectAsState()
    val error by viewModel.error.collectAsState()
    val cargando by viewModel.cargando.collectAsState()
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
            text = "Iniciar Sesión - Cliente",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = Negro
        )
        Spacer(Modifier.height(32.dp))

        CampoTexto(
            valor = email,
            onValorCambio = viewModel::actualizarEmail,
            label = "Correo electrónico",
            placeholder = "ejemplo@correo.com",
            error = null,
            keyboardType = KeyboardType.Email
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
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Amarillo,
                contentColor = Negro
            )
        ) {
            if (cargando) {
                CircularProgressIndicator(
                    color = Negro,
                    modifier = Modifier.size(24.dp),
                    strokeWidth = 2.dp
                )
            } else {
                Text("Ingresar", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }
        }

        Spacer(Modifier.height(16.dp))

        TextButton(onClick = alVolver) {
            Text("Volver", color = Turquesa)
        }
    }
}