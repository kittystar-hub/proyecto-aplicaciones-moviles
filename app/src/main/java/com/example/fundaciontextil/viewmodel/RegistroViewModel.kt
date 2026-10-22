package com.example.fundaciontextil.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.fundaciontextil.model.AppDatabase
import com.example.fundaciontextil.model.Emprendimiento
import com.example.fundaciontextil.model.EmprendimientoRepository
import com.example.fundaciontextil.model.RutValidator
import com.example.fundaciontextil.model.Validators
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RegistroViewModel(application: Application) : AndroidViewModel(application) {

    private val repositorio = EmprendimientoRepository(
        AppDatabase.get(application).emprendimientoDao()
    )

    private val _uiState = MutableStateFlow(RegistroUiState())
    val uiState: StateFlow<RegistroUiState> = _uiState.asStateFlow()

    fun actualizarFoto(uri: String?) = _uiState.update { it.copy(fotoUri = uri) }

    fun actualizarRut(v: String) = _uiState.update { it.copy(rut = v, errores = it.errores.copy(rut = null)) }
    fun actualizarNombre(v: String) = _uiState.update { it.copy(nombre = v, errores = it.errores.copy(nombre = null)) }
    fun actualizarRangoEtario(v: String) = _uiState.update { it.copy(rangoEtario = v, errores = it.errores.copy(rangoEtario = null)) }
    fun actualizarGenero(v: String) = _uiState.update { it.copy(genero = v, errores = it.errores.copy(genero = null)) }
    fun actualizarNombreTaller(v: String) = _uiState.update { it.copy(nombreTaller = v, errores = it.errores.copy(nombreTaller = null)) }

    fun actualizarDescripcion(v: String) {
        if (v.length <= 300) _uiState.update { it.copy(descripcion = v, errores = it.errores.copy(descripcion = null)) }
    }

    fun actualizarRegion(region: String) = _uiState.update {
        it.copy(region = region, comuna = "", errores = it.errores.copy(region = null, comuna = null))
    }

    fun actualizarComuna(comuna: String) = _uiState.update { it.copy(comuna = comuna, errores = it.errores.copy(comuna = null)) }

    fun alternarRubro(rubro: String) {
        _uiState.update { estado ->
            val actuales = estado.rubros
            when {
                rubro in actuales -> estado.copy(rubros = actuales - rubro, errores = estado.errores.copy(rubros = null))
                actuales.size >= 2 -> estado.copy(errores = estado.errores.copy(rubros = "Máximo 2 rubros seleccionables"))
                else -> estado.copy(rubros = actuales + rubro, errores = estado.errores.copy(rubros = null))
            }
        }
    }

    fun actualizarWhatsapp(v: String) = _uiState.update { it.copy(whatsapp = v, errores = it.errores.copy(whatsapp = null)) }
    fun actualizarEmail(v: String) = _uiState.update { it.copy(email = v, errores = it.errores.copy(email = null)) }
    fun actualizarConsentimiento(v: Boolean) = _uiState.update { it.copy(consentimiento = v, errores = it.errores.copy(consentimiento = null)) }

    fun consumirGuardado() = _uiState.update { it.copy(guardadoExitoso = false, idGuardado = null) }

    fun guardar() {
        val s = _uiState.value

        val errores = RegistroErrores(
            rut = when {
                s.rut.isBlank() -> "El RUT es obligatorio"
                !RutValidator.esValido(s.rut) -> "RUT inválido (ej: 12345678-5)"
                else -> null
            },
            nombre = if (s.nombre.isBlank()) "El nombre es obligatorio" else null,
            rangoEtario = if (s.rangoEtario.isBlank()) "Selecciona tu rango etario" else null,
            genero = if (s.genero.isBlank()) "Selecciona tu género" else null,
            nombreTaller = if (s.nombreTaller.isBlank()) "El nombre del taller es obligatorio" else null,
            descripcion = if (s.descripcion.isBlank()) "Cuéntanos qué hace tu emprendimiento" else null,
            region = if (s.region.isBlank()) "Selecciona una región" else null,
            comuna = if (s.comuna.isBlank()) "Selecciona una comuna" else null,
            rubros = if (s.rubros.isEmpty()) "Selecciona al menos 1 rubro" else null,
            whatsapp = when {
                s.whatsapp.isBlank() -> "El WhatsApp es obligatorio"
                !Validators.esWhatsappChileno(s.whatsapp) -> "Formato inválido (ej: +56912345678 o 912345678)"
                else -> null
            },
            email = when {
                s.email.isBlank() -> "El correo es obligatorio"
                !Validators.esEmail(s.email) -> "Correo inválido"
                else -> null
            },
            consentimiento = if (!s.consentimiento) "Debes autorizar el tratamiento de datos" else null
        )

        // validacion explicita de los datos
        val hayErrores = listOfNotNull(
            errores.rut, errores.nombre, errores.rangoEtario, errores.genero,
            errores.nombreTaller, errores.descripcion, errores.region, errores.comuna,
            errores.rubros, errores.whatsapp, errores.email, errores.consentimiento
        ).isNotEmpty()

        _uiState.update {
            it.copy(
                errores = errores,
                // Si hay errores, mostramos un mensaje general para avisar al usuario
                errorGeneral = if (hayErrores) "Por favor, corrige los errores en rojo antes de guardar." else null
            )
        }

        if (hayErrores) return

        viewModelScope.launch {
            _uiState.update { it.copy(guardando = true, errorGeneral = null) }
            try {
                val id = repositorio.guardar(
                    Emprendimiento(
                        nombre = s.nombreTaller.trim(),
                        categoria = s.rubros.firstOrNull() ?: "Textil",
                        ubicacion = s.comuna,
                        descripcion = s.descripcion.trim(),

                        fotoUri = s.fotoUri,
                        rut = RutValidator.normalizar(s.rut),
                        rangoEtario = s.rangoEtario,
                        genero = s.genero,
                        nombreTaller = s.nombreTaller.trim(),
                        region = s.region,
                        comuna = s.comuna,
                        rubros = s.rubros.joinToString(","),
                        whatsapp = Validators.normalizarWhatsapp(s.whatsapp),
                        email = s.email.trim(),
                        consentimiento = s.consentimiento
                    )
                )
                _uiState.update { it.copy(guardando = false, guardadoExitoso = true, idGuardado = id) }
            } catch (e: Exception) {
                _uiState.update { it.copy(guardando = false, errorGeneral = "Error al guardar: ${e.localizedMessage}") }
            }
        }
    }

    companion object {
        fun factory(application: Application): ViewModelProvider.Factory = viewModelFactory {
            initializer { RegistroViewModel(application) }
        }
    }
}