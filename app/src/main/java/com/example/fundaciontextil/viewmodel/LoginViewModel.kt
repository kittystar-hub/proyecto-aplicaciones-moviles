package com.example.fundaciontextil.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.fundaciontextil.model.AppDatabase
import com.example.fundaciontextil.model.Emprendimiento
import com.example.fundaciontextil.model.EmprendimientoRepository
import com.example.fundaciontextil.model.RutValidator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    private val repositorio by lazy {
        EmprendimientoRepository(AppDatabase.get(application).emprendimientoDao())
    }

    private val _rut = MutableStateFlow("")
    val rut: StateFlow<String> = _rut.asStateFlow()
    private val _contrasenia = MutableStateFlow("")
    val contrasenia: StateFlow<String> = _contrasenia.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private val _cargando = MutableStateFlow(false)
    val cargando: StateFlow<Boolean> = _cargando.asStateFlow()

    private val _loginExitoso = MutableStateFlow<Emprendimiento?>(null)
    val loginExitoso: StateFlow<Emprendimiento?> = _loginExitoso.asStateFlow()

    fun actualizarRut(v: String) { _rut.value = v; _error.value = null }

    fun actualizarContrasenia(v: String) { _contrasenia.value = v; _error.value = null }

    fun iniciarSesion() {
        val rutLimpio = RutValidator.normalizar(_rut.value)
        val pass = _contrasenia.value // <-- CAMBIADO AQUÍ

        if (rutLimpio.isBlank() || pass.isBlank()) {
            _error.value = "Debes ingresar RUT y contraseña"
            return
        }

        viewModelScope.launch {
            _cargando.value = true
            try {
                val emprendimiento = repositorio.login(rutLimpio, pass)
                if (emprendimiento != null) {
                    _loginExitoso.value = emprendimiento
                } else {
                    _error.value = "RUT o contraseña incorrectos"
                }
            } catch (e: Exception) {
                _error.value = "Error de conexión"
            } finally {
                _cargando.value = false
            }
        }
    }

    fun consumirLogin() {
        _loginExitoso.value = null
    }
}