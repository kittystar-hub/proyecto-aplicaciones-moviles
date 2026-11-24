package com.example.fundaciontextil.viewmodel

import android.app.Application
import android.content.ContentResolver
import android.net.Uri
import com.example.fundaciontextil.model.AppDatabase
import com.example.fundaciontextil.model.FotoProducto
import com.example.fundaciontextil.model.FotoProductoDao
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
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import java.io.ByteArrayInputStream
import java.io.File

@OptIn(ExperimentalCoroutinesApi::class)
class GaleriaViewModelTest {

    @get:Rule
    val tempFolder = TemporaryFolder()

    private val dispatcher = StandardTestDispatcher()
    private lateinit var application: Application
    private lateinit var database: AppDatabase
    private lateinit var dao: FotoProductoDao
    private lateinit var resolver: ContentResolver

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
        application = mockk(relaxed = true)
        database = mockk(relaxed = true)
        dao = mockk(relaxed = true)
        resolver = mockk(relaxed = true)

        every { application.filesDir } returns tempFolder.root
        every { application.contentResolver } returns resolver

        every { database.fotoProductoDao() } returns dao
        mockkObject(AppDatabase.Companion)
        every { AppDatabase.get(any()) } returns database
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        unmockkAll()
    }

    @Test
    fun `cargarFotos actualiza la lista y limpia errores`() = runTest {
        val fotos = listOf(
            FotoProducto(id = 1L, idEmprendimiento = 10L, fotoUri = "/tmp/a.jpg")
        )
        coEvery { dao.listarPorEmprendimiento(10L) } returns fotos

        val vm = GaleriaViewModel(application)
        vm.cargarFotos(10L)
        advanceUntilIdle()

        assertEquals(fotos, vm.fotos.value)
        assertNull(vm.error.value)
    }

    @Test
    fun `cargarFotos con excepcion setea mensaje de error`() = runTest {
        coEvery { dao.listarPorEmprendimiento(any()) } throws RuntimeException()

        val vm = GaleriaViewModel(application)
        vm.cargarFotos(1L)
        advanceUntilIdle()

        assertNotNull(vm.error.value)
    }

    @Test
    fun `agregarFoto exitoso copia archivo y llama al dao`() = runTest {
        val uri = mockk<Uri>(relaxed = true)
        every { resolver.openInputStream(uri) } returns
                ByteArrayInputStream("contenido".toByteArray())
        coEvery { dao.insertar(any()) } returns 1L
        coEvery { dao.listarPorEmprendimiento(any()) } returns emptyList()

        val vm = GaleriaViewModel(application)
        vm.agregarFoto(5L, uri)
        advanceUntilIdle()

        assertNull(vm.error.value)
        coVerify { dao.insertar(any()) }
        val carpeta = File(tempFolder.root, "fotos_productos")
        assertTrue(carpeta.exists())
        assertTrue(carpeta.listFiles()!!.isNotEmpty())
    }

    @Test
    fun `agregarFoto con input null setea error`() = runTest {
        val uri = mockk<Uri>(relaxed = true)
        every { resolver.openInputStream(uri) } returns null

        val vm = GaleriaViewModel(application)
        vm.agregarFoto(1L, uri)
        advanceUntilIdle()

        assertNotNull(vm.error.value)
    }

    @Test
    fun `eliminarFoto borra archivo del disco y llama al dao`() = runTest {
        val archivo = File(tempFolder.root, "foto_test.jpg").apply { writeText("x") }
        val foto = FotoProducto(id = 1L, idEmprendimiento = 2L, fotoUri = archivo.absolutePath)
        coEvery { dao.listarPorEmprendimiento(any()) } returns emptyList()

        val vm = GaleriaViewModel(application)
        vm.eliminarFoto(foto)
        advanceUntilIdle()

        assertFalse(archivo.exists())
        assertNull(vm.error.value)
        coVerify { dao.eliminar(foto) }
    }

    @Test
    fun `eliminarFoto con excepcion setea mensaje`() = runTest {
        coEvery { dao.eliminar(any()) } throws RuntimeException()

        val vm = GaleriaViewModel(application)
        vm.eliminarFoto(
            FotoProducto(id = 1L, idEmprendimiento = 1L, fotoUri = "/tmp/no_existe.jpg")
        )
        advanceUntilIdle()

        assertNotNull(vm.error.value)
    }
}