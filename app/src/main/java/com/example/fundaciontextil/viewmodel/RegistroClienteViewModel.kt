package com.example.fundaciontextil.viewmodel
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.fundaciontextil.model.AppDatabase
import com.example.fundaciontextil.model.Cliente
import com.example.fundaciontextil.model.ClienteRepository
import com.example.fundaciontextil.model.ClienteDao
import com.example.fundaciontextil.model.Validators
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RegistroClienteViewModel(app: Application) : AndroidViewModel(app) {

    private val repository = ClienteRepository(AppDatabase.get(app).clienteDao())

    private val _uiState = MutableStateFlow(RegistroClienteUiState())
    val uiState: StateFlow<RegistroClienteUiState> = _uiState.asStateFlow()

    fun actualizarNombre(valor: String) =
        _uiState.update { it.copy(nombre = valor, nombreError = null) }

    fun actualizarApellido(valor: String) =
        _uiState.update { it.copy(apellido = valor, apellidoError = null) }

    fun actualizarEmail(valor: String) =
        _uiState.update { it.copy(email = valor, emailError = null) }

    fun actualizarContrasenia(valor: String) =
        _uiState.update {
            it.copy(contrasenia = valor, contraseniaError = null, confirmarContraseniaError = null)
        }

    fun actualizarConfirmarContrasenia(valor: String) =
        _uiState.update { it.copy(confirmarContrasenia = valor, confirmarContraseniaError = null) }

    fun actualizarTelefono(valor: String) =
        _uiState.update { it.copy(telefono = valor, telefonoError = null) }

    fun actualizarFoto(uri: String?) =
        _uiState.update { it.copy(fotoUri = uri) }

    fun guardar() {
        val estado = _uiState.value

        val nombreError = if (estado.nombre.isBlank()) "El nombre es obligatorio" else null
        val apellidoError = if (estado.apellido.isBlank()) "El apellido es obligatorio" else null
        val emailError = when {
            estado.email.isBlank() -> "El correo es obligatorio"
            !Validators.esEmail(estado.email) -> "Formato de correo inválido"
            else -> null
        }
        val contraseniaError = when {
            estado.contrasenia.isBlank() -> "La contraseña es obligatoria"
            estado.contrasenia.length < 6 -> "Debe tener al menos 6 caracteres"
            else -> null
        }
        val confirmarError = when {
            estado.confirmarContrasenia.isBlank() -> "Debes confirmar la contraseña"
            estado.confirmarContrasenia != estado.contrasenia -> "Las contraseñas no coinciden"
            else -> null
        }
        val telefonoError = when {
            estado.telefono.isBlank() -> null
            !Validators.esWhatsappChileno(estado.telefono) -> "Teléfono inválido (ej: +56 9 1234 5678)"
            else -> null
        }

        if (listOfNotNull(
                nombreError, apellidoError, emailError,
                contraseniaError, confirmarError, telefonoError
            ).isNotEmpty()
        ) {
            _uiState.update {
                it.copy(
                    nombreError = nombreError,
                    apellidoError = apellidoError,
                    emailError = emailError,
                    contraseniaError = contraseniaError,
                    confirmarContraseniaError = confirmarError,
                    telefonoError = telefonoError
                )
            }
            return
        }

        _uiState.update { it.copy(guardando = true) }

        viewModelScope.launch {
            try {
                val emailNormalizado = estado.email.trim().lowercase()
                val existente = repository.obtenerPorEmail(emailNormalizado)
                if (existente != null) {
                    _uiState.update {
                        it.copy(
                            guardando = false,
                            emailError = "Ya existe una cuenta con este correo"
                        )
                    }
                    return@launch
                }

                val cliente = Cliente(
                    nombre = estado.nombre.trim(),
                    apellido = estado.apellido.trim(),
                    email = emailNormalizado,
                    contrasenia = estado.contrasenia,
                    telefono = estado.telefono.trim(),
                    fotoUri = estado.fotoUri
                )

                val id = repository.guardar(cliente)
                _uiState.update { it.copy(guardando = false, guardadoExitoso = id) }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        guardando = false,
                        emailError = "Error al guardar: ${e.message}"
                    )
                }
            }
        }
    }

    fun consumirGuardado() {
        _uiState.update { it.copy(guardadoExitoso = null) }
    }
}