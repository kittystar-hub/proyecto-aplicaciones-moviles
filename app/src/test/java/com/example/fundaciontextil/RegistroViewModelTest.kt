package com.example.fundaciontextil.viewmodel

import android.app.Application
import com.example.fundaciontextil.model.AppDatabase
import com.example.fundaciontextil.model.EmprendimientoDao
import io.mockk.coEvery
import io.mockk.coVerify
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
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class RegistroViewModelTest {

    private val dispatcher = StandardTestDispatcher()
    private lateinit var application: Application
    private lateinit var dao: EmprendimientoDao

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
        application = mockk(relaxed = true)
        dao = mockk(relaxed = true)

        val database = mockk<AppDatabase>(relaxed = true)
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
    fun `todos los setters actualizan el estado`() {
        val vm = RegistroViewModel(application)

        vm.actualizarFoto("/x.jpg")
        vm.actualizarRut("12345678-5")
        vm.actualizarNombre("María")
        vm.actualizarRangoEtario("25 - 34")
        vm.actualizarGenero("Femenino")
        vm.actualizarNombreTaller("Taller Raíz")
        vm.actualizarDescripcion("Textil circular")
        vm.actualizarRegion("Metropolitana de Santiago")
        vm.actualizarComuna("Santiago")
        vm.actualizarWhatsapp("+56912345678")
        vm.actualizarEmail("a@b.cl")
        vm.actualizarConsentimiento(true)
        vm.actualizarContrasenia("secreto1")
        vm.actualizarConfirmarContrasenia("secreto1")

        with(vm.uiState.value) {
            assertEquals("/x.jpg", fotoUri)
            assertEquals("12345678-5", rut)
            assertEquals("María", nombre)
            assertEquals("25 - 34", rangoEtario)
            assertEquals("Femenino", genero)
            assertEquals("Taller Raíz", nombreTaller)
            assertEquals("Textil circular", descripcion)
            assertEquals("Metropolitana de Santiago", region)
            assertEquals("Santiago", comuna)
            assertEquals("+56912345678", whatsapp)
            assertEquals("a@b.cl", email)
            assertTrue(consentimiento)
            assertEquals("secreto1", contrasenia)
            assertEquals("secreto1", confirmarContrasenia)
            assertTrue(comunasDisponibles.contains("Santiago"))
        }
    }

    @Test
    fun `actualizarDescripcion bloquea mas de 300 caracteres`() {
        val vm = RegistroViewModel(application)
        vm.actualizarDescripcion("a".repeat(300))
        assertEquals(300, vm.uiState.value.descripcion.length)
        vm.actualizarDescripcion("b".repeat(301))
        assertEquals(300, vm.uiState.value.descripcion.length)
    }

    @Test
    fun `alternarRubro agrega quita y limita a 2`() {
        val vm = RegistroViewModel(application)

        vm.alternarRubro("Tejido y bordado")
        assertTrue("Tejido y bordado" in vm.uiState.value.rubros)

        vm.alternarRubro("Tejido y bordado")
        assertTrue(vm.uiState.value.rubros.isEmpty())

        vm.alternarRubro("Tejido y bordado")
        vm.alternarRubro("Reparación textil")
        vm.alternarRubro("Confección sostenible")
        assertEquals(2, vm.uiState.value.rubros.size)
        assertEquals("Máximo 2 rubros seleccionables", vm.uiState.value.errores.rubros)
    }

    @Test
    fun `guardar con datos invalidos setea todos los errores`() = runTest {
        val vm = RegistroViewModel(application)
        vm.actualizarRut("12345678-9")           // DV incorrecto
        vm.actualizarContrasenia("123")          // corta
        vm.actualizarConfirmarContrasenia("456") // distinta
        vm.actualizarEmail("no-email")
        vm.actualizarWhatsapp("123")
        vm.guardar()

        val e = vm.uiState.value.errores
        assertEquals("RUT inválido (ej: 12345678-5)", e.rut)
        assertEquals("Debe tener al menos 6 caracteres", e.contrasenia)
        assertEquals("Las contraseñas no coinciden", e.confirmarContrasenia)
        assertEquals("Correo inválido", e.email)
        assertTrue(e.whatsapp!!.startsWith("Formato inválido"))
        assertNotNull(e.nombre)
        assertNotNull(e.rubros)
        assertNotNull(e.consentimiento)
        assertNotNull(vm.uiState.value.errorGeneral)
        coVerify(exactly = 0) { dao.insertar(any()) }
    }

    @Test
    fun `guardar con datos validos llama al repositorio y setea exito`() = runTest {
        coEvery { dao.insertar(any()) } returns 42L

        val vm = registroValido()
        vm.guardar()
        advanceUntilIdle()

        assertTrue(vm.uiState.value.guardadoExitoso)
        assertEquals(42L, vm.uiState.value.idGuardado)
        assertFalse(vm.uiState.value.guardando)
        coVerify(exactly = 1) { dao.insertar(any()) }
    }

    @Test
    fun `guardar con excepcion de BD setea errorGeneral`() = runTest {
        coEvery { dao.insertar(any()) } throws RuntimeException("DB fail")

        val vm = registroValido()
        vm.guardar()
        advanceUntilIdle()

        assertFalse(vm.uiState.value.guardadoExitoso)
        assertTrue(vm.uiState.value.errorGeneral!!.startsWith("Error al guardar"))
    }

    @Test
    fun `consumirGuardado limpia el estado de exito`() = runTest {
        coEvery { dao.insertar(any()) } returns 7L
        val vm = registroValido()
        vm.guardar()
        advanceUntilIdle()
        assertTrue(vm.uiState.value.guardadoExitoso)

        vm.consumirGuardado()
        assertFalse(vm.uiState.value.guardadoExitoso)
        assertNull(vm.uiState.value.idGuardado)
    }

    @Test
    fun `factory retorna un ViewModelProvider Factory`() {
        assertNotNull(RegistroViewModel.factory(application))
    }

    private fun registroValido() = RegistroViewModel(application).apply {
        actualizarRut("12345678-5")
        actualizarNombre("María González")
        actualizarRangoEtario("25 - 34")
        actualizarGenero("Femenino")
        actualizarNombreTaller("Taller Raíz")
        actualizarDescripcion("Emprendimiento textil circular")
        actualizarRegion("Metropolitana de Santiago")
        actualizarComuna("Santiago")
        alternarRubro("Tejido y bordado")
        actualizarWhatsapp("+56912345678")
        actualizarEmail("maria@example.com")
        actualizarContrasenia("secreto1")
        actualizarConfirmarContrasenia("secreto1")
        actualizarConsentimiento(true)
    }
}