package com.example.fundaciontextil.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun PantallaConfirmacion(id: Long?, modifier: Modifier = Modifier, alVolverAlInicio: () -> Unit = {}) {
    Column(
        modifier = modifier.fillMaxSize().background(Color(0xFFFFFEFA)).padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(modifier = Modifier.size(96.dp).background(Color(0xFFFFD84D), CircleShape), contentAlignment = Alignment.Center) {
            Text("✓", fontSize = 48.sp, fontWeight = FontWeight.Bold, color = Color(0xFF202020))
        }
        Spacer(Modifier.height(24.dp))
        Text("¡Ficha guardada!", fontSize = 26.sp, fontWeight = FontWeight.Bold, color = Color(0xFF202020), textAlign = TextAlign.Center)
        Spacer(Modifier.height(12.dp))
        Text(if (id != null) "Tu emprendimiento ya es parte de la fundación (#$id)." else "Tu emprendimiento ya es parte de la fundacion.", fontSize = 16.sp, color = Color(0xFF202020).copy(alpha = 0.8f), textAlign = TextAlign.Center)
        Spacer(Modifier.height(32.dp))
        Button(
            onClick = alVolverAlInicio,
            modifier = Modifier.fillMaxWidth().height(56.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF168C8C), contentColor = Color.White)
        ) { Text("Volver al inicio", fontWeight = FontWeight.SemiBold) }
    }
}