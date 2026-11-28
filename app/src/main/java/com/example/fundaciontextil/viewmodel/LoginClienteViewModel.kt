package com.example.fundaciontextil.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fundaciontextil.data.repository.AuthRepositoryRemote
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LoginClienteViewModel : ViewModel() {

    private val authRepository = AuthRepositoryRemote()

    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email.asStateFlow()

    private val _contrasenia = MutableStateFlow("")
    val contrasenia: StateFlow<String> = _contrasenia.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private val _cargando = MutableStateFlow(false)
    val cargando: StateFlow<Boolean> = _cargando.asStateFlow()

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
        val emailIngresado = _email.value.trim().lowercase()
        val contraseniaIngresada = _contrasenia.value

        if (emailIngresado.isBlank() || contraseniaIngresada.isBlank()) {
            _error.value = "Debes ingresar correo y contraseña"
            return
        }

        viewModelScope.launch {
            _cargando.value = true
            _error.value = null

            try {
                authRepository.iniciarSesion(
                    emailIngresado,
                    contraseniaIngresada
                )

                _loginExitoso.value = true

            } catch (e: Exception) {
                _error.value = when {
                    e.message?.contains("Invalid login credentials", true) == true ->
                        "Correo o contraseña incorrectos"

                    e.message?.contains("Email not confirmed", true) == true ->
                        "Debes confirmar tu correo electrónico"

                    else ->
                        "No se pudo iniciar sesión. Verifica tu conexión e inténtalo nuevamente."
                }
            } finally {
                _cargando.value = false
            }
        }
    }

    fun consumirLogin() {
        _loginExitoso.value = false
    }
}