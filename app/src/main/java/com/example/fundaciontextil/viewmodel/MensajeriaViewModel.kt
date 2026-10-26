package com.example.fundaciontextil.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fundaciontextil.model.Mensaje
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * ViewModel encargado de la lógica y gestión del estado reactivo del chat
 * entre el usuario y la emprendedora textil, cumpliendo con la arquitectura MVVM.
 */
class MensajeriaViewModel : ViewModel() {

    // Lista reactiva de mensajes
    private val _mensajes = MutableStateFlow<List<Mensaje>>(emptyList())
    val mensajes: StateFlow<List<Mensaje>> = _mensajes.asStateFlow()

    // Texto actual del campo de entrada
    private val _textoMensaje = MutableStateFlow("")
    val textoMensaje: StateFlow<String> = _textoMensaje.asStateFlow()

    // Estado animado de "Escribiendo..." para la respuesta de la emprendedora
    private val _estaEscribiendo = MutableStateFlow(false)
    val estaEscribiendo: StateFlow<Boolean> = _estaEscribiendo.asStateFlow()

    // Sugerencias rápidas basadas en la entrevista con el cliente
    val sugerenciasRapidas: List<String> = listOf(
        "Hola, me gustaría saber más del producto",
        "¿Hacen envíos a mi comuna?",
        "¿Qué materiales textiles reciclados utilizan?",
        "¿Tienen stock disponible?"
    )

    init {
        // Mensaje inicial de bienvenida de la emprendedora
        val horaActual = obtenerHoraActual()
        _mensajes.value = listOf(
            Mensaje(
                contenido = "¡Hola! Gracias por contactarnos en Fundación Textil Circular 🌿. ¿En qué te puedo ayudar hoy?",
                esUsuario = false,
                hora = horaActual
            )
        )
    }

    /**
     * Actualiza el contenido del texto que escribe el usuario.
     */
    fun actualizarTexto(nuevoTexto: String) {
        _textoMensaje.value = nuevoTexto
    }

    /**
     * Llena el campo de texto con la sugerencia seleccionada por el usuario.
     */
    fun seleccionarSugerencia(sugerencia: String) {
        _textoMensaje.value = sugerencia
    }

    /**
     * Envía el mensaje del usuario y simula la respuesta automática
     * de la emprendedora de forma reactiva y con corrutinas.
     */
    fun enviarMensaje(textoAEnviar: String? = null) {
        val textoFinal = (textoAEnviar ?: _textoMensaje.value).trim()
        if (textoFinal.isBlank()) return

        val horaActual = obtenerHoraActual()

        // 1. Agregar el mensaje del cliente
        val nuevoMensajeUsuario = Mensaje(
            contenido = textoFinal,
            esUsuario = true,
            hora = horaActual
        )
        _mensajes.value = _mensajes.value + nuevoMensajeUsuario
        _textoMensaje.value = ""

        // 2. Simular respuesta diferida de la emprendedora con animación de "Escribiendo..."
        viewModelScope.launch {
            _estaEscribiendo.value = true
            delay(1600) // Pausa realista para la animación
            _estaEscribiendo.value = false

            val respuestaEmprendedora = generarRespuestaContextual(textoFinal)
            val nuevoMensajeEmprendedora = Mensaje(
                contenido = respuestaEmprendedora,
                esUsuario = false,
                hora = obtenerHoraActual()
            )
            _mensajes.value = _mensajes.value + nuevoMensajeEmprendedora
        }
    }

    /**
     * Genera una respuesta coherente según las inquietudes del cliente.
     */
    private fun generarRespuestaContextual(mensajeUsuario: String): String {
        val consulta = mensajeUsuario.lowercase(Locale.ROOT)
        return when {
            consulta.contains("envío") || consulta.contains("envio") || consulta.contains("comuna") || consulta.contains("region") -> {
                "¡Por supuesto! Coordinamos envíos sustentables a todas las regiones de Chile mediante paquetería ecológica. ¿A qué comuna necesitarías el despacho?"
            }
            consulta.contains("material") || consulta.contains("recicl") || consulta.contains("tela") -> {
                "Utilizamos 100% retazos de telas recicladas, denim recuperado y descartes textiles donados a la Fundación para crear piezas únicas sin generar residuos."
            }
            consulta.contains("producto") || consulta.contains("saber más") || consulta.contains("detalle") -> {
                "Nuestros productos son hechos a mano con técnicas de suprarreciclaje (upcycling). Cada diseño es exclusivo y promueve la economía circular. ¿Te gustaría conocer medidas o colores disponibles?"
            }
            consulta.contains("stock") || consulta.contains("precio") || consulta.contains("disponible") -> {
                "Tenemos piezas disponibles listas para entrega y también elaboramos pedidos personalizados según la tela que tengamos en stock. ¡Escríbeme lo que buscas!"
            }
            else -> {
                "¡Muchas gracias por tu mensaje! Con gusto te oriento en todo lo que necesites sobre este emprendimiento textil circular."
            }
        }
    }

    private fun obtenerHoraActual(): String {
        val formato = SimpleDateFormat("HH:mm", Locale.getDefault())
        return formato.format(Date())
    }
}
