package com.example.fundaciontextil.model


//Validador para datos generales ingresados al formulario

object Validators {

    private val REGEX_EMAIL = Regex(
        "^[A-Za-z0-9._%+\\-]+@[A-Za-z0-9.\\-]+\\.[A-Za-z]{2,}$"
    )
    private fun limpiarTelefono(input: String): String {
        return input.replace(" ", "")
            .replace("-", "")
            .replace("(", "")
            .replace(")", "")
    }

      //Numero chileno par whatsapp

    fun esWhatsappChileno(input: String): Boolean {
        val limpio = limpiarTelefono(input)

        val soloNumeros = if (limpio.startsWith("+")) limpio.drop(1) else limpio
        if (!soloNumeros.all { it.isDigit() }) return false

        return when {
            limpio.startsWith("+569") && limpio.length == 12 -> true
            limpio.startsWith("569") && limpio.length == 11 -> true
            limpio.length == 9 && limpio.startsWith("9") -> true
            else -> false
        }
    }

    //Transformar numero al estandar de WhatsApp (+56 9)

    fun normalizarWhatsapp(input: String): String {
        val limpio = limpiarTelefono(input)
        return when {
            limpio.startsWith("+569") && limpio.length == 12 -> limpio
            limpio.startsWith("569") && limpio.length == 11 -> "+$limpio"
            limpio.length == 9 && limpio.startsWith("9") -> "+56$limpio"
            else -> limpio // Si no es un formato identificable, lo devuelve limpio
        }
    }

    //Validacion de estructura basica de correo

    fun esEmail(input: String): Boolean = REGEX_EMAIL.matches(input.trim())
}