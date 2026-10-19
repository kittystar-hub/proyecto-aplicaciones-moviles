package com.example.fundaciontextil

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.fundaciontextil.ui.theme.FundacionTextilTheme
import com.example.fundaciontextil.ui.vistas.bienvenida.PantallaBienvenida
import com.example.fundaciontextil.ui.vistas.catalogo.PantallaCatalogo
import com.example.fundaciontextil.viewmodel.CatalogoViewModel

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            FundacionTextilTheme {
                val catalogoViewModel: CatalogoViewModel = viewModel()
                var pantallaActual by remember {
                    mutableStateOf("bienvenida")
                }

                Scaffold(
                    modifier = Modifier.fillMaxSize()
                ) { espacioInterior ->

                    when (pantallaActual) {
                        "bienvenida" -> {
                            PantallaBienvenida(
                                modifier = Modifier.padding(espacioInterior),
                                alExplorarCatalogo = {
                                    pantallaActual = "catalogo"
                                }
                            )
                        }

                        "catalogo" -> {
                            PantallaCatalogo(
                                modifier = Modifier.padding(espacioInterior),
                                viewModel = catalogoViewModel,
                                alVolver = {
                                    pantallaActual = "bienvenida"
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}
