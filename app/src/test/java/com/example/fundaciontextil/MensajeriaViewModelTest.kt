package com.example.fundaciontextil.viewmodel

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class MensajeriaViewModelTest {

    private val dispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `init carga mensaje de bienvenida de la emprendedora`() {
        val vm = MensajeriaViewModel()
        val mensajes = vm.mensajes.value
        assertEquals(1, mensajes.size)
        assertFalse(mensajes[0].esUsuario)
        assertTrue(mensajes[0].contenido.contains("Hola"))
        assertTrue(mensajes[0].hora.matches(Regex("\\d{2}:\\d{2}")))
    }

    @Test
    fun `actualizarTexto cambia el texto del campo`() {
        val vm = MensajeriaViewModel()
        vm.actualizarTexto("Hola mundo")
        assertEquals("Hola mundo", vm.textoMensaje.value)
    }

    @Test
    fun `seleccionarSugerencia copia la sugerencia al campo`() {
        val vm = MensajeriaViewModel()
        vm.seleccionarSugerencia("¿Hacen envíos?")
        assertEquals("¿Hacen envíos?", vm.textoMensaje.value)
    }

    @Test
    fun `enviarMensaje con texto vacio no altera la lista`() {
        val vm = MensajeriaViewModel()
        val antes = vm.mensajes.value.size
        vm.actualizarTexto("   ")
        vm.enviarMensaje()
        assertEquals(antes, vm.mensajes.value.size)
    }

    @Test
    fun `enviarMensaje agrega mensaje del usuario y limpia el campo`() = runTest {
        val vm = MensajeriaViewModel()
        vm.actualizarTexto("Hola")
        vm.enviarMensaje()

        runCurrent()

        assertEquals(2, vm.mensajes.value.size)
        assertTrue(vm.mensajes.value[1].esUsuario)
        assertEquals("", vm.textoMensaje.value)
        assertTrue(vm.estaEscribiendo.value)
    }

    @Test
    fun `enviarMensaje dispara respuesta automatica tras el delay`() = runTest {
        val vm = MensajeriaViewModel()
        vm.enviarMensaje("Hola")
        advanceUntilIdle()
        assertEquals(3, vm.mensajes.value.size)
        assertFalse(vm.mensajes.value.last().esUsuario)
        assertFalse(vm.estaEscribiendo.value)
    }

    @Test
    fun `respuesta contextual para envios`() = runTest {
        val vm = MensajeriaViewModel()
        vm.enviarMensaje("Quiero saber de envíos")
        advanceUntilIdle()
        assertTrue(vm.mensajes.value.last().contenido.contains("envíos", ignoreCase = true))
    }

    @Test
    fun `respuesta contextual para materiales`() = runTest {
        val vm = MensajeriaViewModel()
        vm.enviarMensaje("Qué materiales usan")
        advanceUntilIdle()
        assertTrue(vm.mensajes.value.last().contenido.contains("retazos", ignoreCase = true))
    }

    @Test
    fun `respuesta contextual para productos`() = runTest {
        val vm = MensajeriaViewModel()
        vm.enviarMensaje("Cuéntame del producto")
        advanceUntilIdle()
        assertTrue(vm.mensajes.value.last().contenido.contains("upcycling", ignoreCase = true))
    }

    @Test
    fun `respuesta contextual para stock`() = runTest {
        val vm = MensajeriaViewModel()
        vm.enviarMensaje("Hay stock disponible?")
        advanceUntilIdle()
        assertNotNull(vm.mensajes.value.last().contenido)
        assertTrue(vm.mensajes.value.last().contenido.contains("piezas", ignoreCase = true))
    }

    @Test
    fun `respuesta contextual generica`() = runTest {
        val vm = MensajeriaViewModel()
        vm.enviarMensaje("Hola, cómo estás?")
        advanceUntilIdle()
        assertTrue(vm.mensajes.value.last().contenido.contains("Muchas gracias", ignoreCase = true))
    }
}