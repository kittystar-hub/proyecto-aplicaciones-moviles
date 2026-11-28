package com.example.fundaciontextil.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.fundaciontextil.data.repository.AuthRepositoryRemote
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LoginViewModel(application: Application) : AndroidViewModel(application) {

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

    fun actualizarEmail(v: String) {
        _email.value = v
        _error.value = null
    }

    fun actualizarContrasenia(v: String) {
        _contrasenia.value = v
        _error.value = null
    }

    fun iniciarSesion() {
        val emailIngresado = _email.value.trim()
        val pass = _contrasenia.value

        if (emailIngresado.isBlank() || pass.isBlank()) {
            _error.value = "Debes ingresar correo y contraseña"
            return
        }

        viewModelScope.launch {
            _cargando.value = true
            _error.value = null

            try {
                authRepository.iniciarSesionConRol(
                    emailIngresado,
                    pass,
                    "emprendedora"
                )

                _loginExitoso.value = true

            } catch (e: Exception) {
                _error.value = when {
                    e.message?.contains(
                        "Invalid login credentials", true
                    ) == true ->
                        "Correo o contraseña incorrectos"

                    e.message?.contains(
                        "Email not confirmed", true
                    ) == true ->
                        "Debes confirmar tu correo electrónico"

                    e.message?.contains(
                        "La cuenta no corresponde", true
                    ) == true ->
                        "Esta cuenta no corresponde a una emprendedora"

                    else ->
                        "No se pudo iniciar sesión. Inténtalo nuevamente."
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