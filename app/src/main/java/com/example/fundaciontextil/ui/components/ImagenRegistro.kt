package com.example.fundaciontextil.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

@Composable
fun ImagenRegistro(
    fotoUri: String?,
    modifier: Modifier = Modifier
) {
    val baseModifier = modifier
        .size(150.dp)
        .clip(CircleShape)

    if (!fotoUri.isNullOrBlank()) {
        AsyncImage(
            model = fotoUri,
            contentDescription = "Foto de perfil",
            contentScale = ContentScale.Crop,
            modifier = baseModifier
        )
    } else {
        Icon(
            imageVector = Icons.Default.AccountCircle,
            contentDescription = "Icono de perfil por defecto",
            tint = Color.Gray,
            modifier = baseModifier
                .background(Color.LightGray.copy(alpha = 0.2f))
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ImagenRegistroPreview() {
    ImagenRegistro(fotoUri = null)
}
