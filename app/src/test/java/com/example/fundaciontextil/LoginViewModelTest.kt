package com.example.fundaciontextil.viewmodel

import android.app.Application
import com.example.fundaciontextil.model.AppDatabase
import com.example.fundaciontextil.model.Emprendimiento
import com.example.fundaciontextil.model.EmprendimientoDao
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.unmockkAll
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class LoginViewModelTest {

    private val dispatcher = StandardTestDispatcher()
    private lateinit var application: Application
    private lateinit var database: AppDatabase
    private lateinit var dao: EmprendimientoDao

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
        application = mockk(relaxed = true)
        database = mockk(relaxed = true)
        dao = mockk(relaxed = true)

        every { database.emprendimientoDao() } returns dao
        mockkObject(AppDatabase.Companion)
        every { AppDatabase.get(any()) } returns database
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        unmockkAll()
    }

    @Test
    fun `estado inicial vacio`() {
        val vm = LoginViewModel(application)
        assertEquals("", vm.rut.value)
        assertEquals("", vm.contrasenia.value)
        assertNull(vm.error.value)
        assertFalse(vm.cargando.value)
        assertNull(vm.loginExitoso.value)
    }

    @Test
    fun `actualizarRut actualiza y limpia el error`() {
        val vm = LoginViewModel(application)
        vm.actualizarRut("12345678-5")
        assertEquals("12345678-5", vm.rut.value)
        assertNull(vm.error.value)
    }

    @Test
    fun `actualizarContrasenia actualiza y limpia el error`() {
        val vm = LoginViewModel(application)
        vm.actualizarContrasenia("secret123")
        assertEquals("secret123", vm.contrasenia.value)
        assertNull(vm.error.value)
    }

    @Test
    fun `iniciarSesion con campos vacios da error`() = runTest {
        val vm = LoginViewModel(application)
        vm.iniciarSesion()
        assertEquals("Debes ingresar RUT y contraseña", vm.error.value)
        assertFalse(vm.cargando.value)
    }

    @Test
    fun `iniciarSesion con credenciales validas setea loginExitoso`() = runTest {
        val emprendimiento = Emprendimiento(id = 1L, rut = "12345678-5", contrasenia = "secret")
        coEvery { dao.login(any(), any()) } returns emprendimiento

        val vm = LoginViewModel(application)
        vm.actualizarRut("12345678-5")
        vm.actualizarContrasenia("secret")
        vm.iniciarSesion()
        advanceUntilIdle()

        assertEquals(emprendimiento, vm.loginExitoso.value)
        assertNull(vm.error.value)
        assertFalse(vm.cargando.value)
    }

    @Test
    fun `iniciarSesion con credenciales invalidas da error`() = runTest {
        coEvery { dao.login(any(), any()) } returns null

        val vm = LoginViewModel(application)
        vm.actualizarRut("12345678-5")
        vm.actualizarContrasenia("wrong")
        vm.iniciarSesion()
        advanceUntilIdle()

        assertEquals("RUT o contraseña incorrectos", vm.error.value)
        assertNull(vm.loginExitoso.value)
    }

    @Test
    fun `iniciarSesion con excepcion da error de conexion`() = runTest {
        coEvery { dao.login(any(), any()) } throws RuntimeException("DB fail")

        val vm = LoginViewModel(application)
        vm.actualizarRut("12345678-5")
        vm.actualizarContrasenia("secret")
        vm.iniciarSesion()
        advanceUntilIdle()

        assertEquals("Error de conexión", vm.error.value)
        assertFalse(vm.cargando.value)
    }

    @Test
    fun `consumirLogin limpia el estado de exito`() {
        val vm = LoginViewModel(application)
        vm.consumirLogin()
        assertNull(vm.loginExitoso.value)
    }
}