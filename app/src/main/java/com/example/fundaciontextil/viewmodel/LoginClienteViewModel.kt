package com.example.fundaciontextil.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.fundaciontextil.model.AppDatabase
import com.example.fundaciontextil.model.Cliente
import com.example.fundaciontextil.model.ClienteRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LoginClienteViewModel(app: Application) : AndroidViewModel(app) {

    private val repository = ClienteRepository(AppDatabase.get(app).clienteDao())

    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email.asStateFlow()

    private val _contrasenia = MutableStateFlow("")
    val contrasenia: StateFlow<String> = _contrasenia.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private val _cargando = MutableStateFlow(false)
    val cargando: StateFlow<Boolean> = _cargando.asStateFlow()

    private val _loginExitoso = MutableStateFlow<Cliente?>(null)
    val loginExitoso: StateFlow<Cliente?> = _loginExitoso.asStateFlow()

    fun actualizarEmail(valor: String) {
        _email.value = valor
        _error.value = null
    }

    fun actualizarContrasenia(valor: String) {
        _contrasenia.value = valor
        _error.value = null
    }

    fun iniciarSesion() {
        val email = _email.value.trim().lowercase()
        val contrasenia = _contrasenia.value

        if (email.isBlank() || contrasenia.isBlank()) {
            _error.value = "Debes ingresar correo y contraseña"
            return
        }

        _cargando.value = true
        viewModelScope.launch {
            val cliente = repository.login(email, contrasenia)
            _cargando.value = false
            if (cliente != null) {
                _loginExitoso.value = cliente
            } else {
                _error.value = "Correo o contraseña incorrectos"
            }
        }
    }

    fun consumirLogin() {
        _loginExitoso.value = null
    }
}