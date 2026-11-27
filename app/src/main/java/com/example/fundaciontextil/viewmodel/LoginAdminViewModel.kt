package com.example.fundaciontextil.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class LoginAdminViewModel : ViewModel() {

    // CREDENCIALES PREDETERMINADAS DE PRUEBA
    private val ADMIN_EMAIL = "admin@fundaciontextil.cl"
    private val ADMIN_PASSWORD = "admin123"

    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email.asStateFlow()

    private val _contrasenia = MutableStateFlow("")
    val contrasenia: StateFlow<String> = _contrasenia.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private val _loginExitoso = MutableStateFlow(false)
    val loginExitoso: StateFlow<Boolean> = _loginExitoso.asStateFlow()

    fun actualizarEmail(valor: String) {
        _email.value = valor
        _error.value = null
    }

    fun actualizarContrasenia(valor: String) {
        _contrasenia.value = valor
        _error.value = null
    }

    fun iniciarSesion() {
        val inputEmail = _email.value.trim()
        val inputPass = _contrasenia.value

        if (inputEmail == ADMIN_EMAIL && inputPass == ADMIN_PASSWORD) {
            _loginExitoso.value = true
            _error.value = null
        } else {
            _error.value = "Correo o contraseña de administrador incorrectos"
        }
    }

    fun consumirLogin() {
        _loginExitoso.value = false
    }
}