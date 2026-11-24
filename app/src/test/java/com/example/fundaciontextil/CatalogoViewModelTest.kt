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
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CatalogoViewModelTest {

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
    fun `init mezcla ejemplos con registros guardados`() = runTest {
        coEvery { dao.listarTodos() } returns listOf(
            Emprendimiento(id = 1L, nombre = "Guardado", categoria = "Textil")
        )

        val vm = CatalogoViewModel(application)
        advanceUntilIdle()

        val lista = vm.emprendimientos.value
        assertEquals(5, lista.size) // 4 ejemplos + 1 guardado
        assertTrue(lista.any { it.nombre == "Taller Upcycling Valparaíso" })
        assertTrue(lista.any { it.nombre == "Guardado" })
    }

    @Test
    fun `init sin registros guardados solo deja ejemplos`() = runTest {
        coEvery { dao.listarTodos() } returns emptyList()

        val vm = CatalogoViewModel(application)
        advanceUntilIdle()

        assertEquals(4, vm.emprendimientos.value.size)
    }

    @Test
    fun `init con excepcion en el DAO conserva los ejemplos`() = runTest {
        coEvery { dao.listarTodos() } throws RuntimeException("DB error")

        val vm = CatalogoViewModel(application)
        advanceUntilIdle()

        assertEquals(4, vm.emprendimientos.value.size)
        assertTrue(vm.emprendimientos.value.all { it.id < 0 })
    }

    @Test
    fun `cargarEmprendimientos refresca la lista`() = runTest {
        coEvery { dao.listarTodos() } returns emptyList()
        val vm = CatalogoViewModel(application)
        advanceUntilIdle()
        assertEquals(4, vm.emprendimientos.value.size)

        coEvery { dao.listarTodos() } returns listOf(
            Emprendimiento(id = 2L, nombre = "Nuevo")
        )
        vm.cargarEmprendimientos()
        advanceUntilIdle()

        assertEquals(5, vm.emprendimientos.value.size)
        assertTrue(vm.emprendimientos.value.any { it.nombre == "Nuevo" })
    }
}