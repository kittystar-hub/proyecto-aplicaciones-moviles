package com.example.fundaciontextil.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val Turquesa = Color(0xFF168C8C)

@Composable
fun SeccionTitulo(texto: String) {
    Column {
        Text(
            text = texto.uppercase(),
            fontSize = 12.sp,
            color = Turquesa,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp
        )
        Spacer(Modifier.height(4.dp))
        HorizontalDivider(color = Turquesa.copy(alpha = 0.2f))
    }
}