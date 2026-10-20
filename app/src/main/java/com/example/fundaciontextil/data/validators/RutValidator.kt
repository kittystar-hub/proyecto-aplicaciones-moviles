package com.example.fundaciontextil.data.validators

//Validador de formato de RUT, faltaria integrar conexión a BD de emprendedoras
//Para poder verificar que existe

object RutValidator {
    private fun limpiar(rut: String): String {
        return rut.replace(".", "")
            .replace("-", "")
            .replace(" ", "")
            .uppercase()
    }

        //Formato estandar para rut

    fun normalizar(rut: String): String {
        val clean = limpiar(rut)
        if (clean.length < 2) return clean
        return "${clean.dropLast(1)}-${clean.last()}"
    }

    //Validar si el RUT ingresado es matemáticamente correcto
    fun esValido(rut: String): Boolean {
        val clean = limpiar(rut)

        if (clean.length < 2) return false

        val cuerpo = clean.dropLast(1)
        val dv = clean.last()

        // Validaciones rápidas de formato
        if (!cuerpo.all { it.isDigit() }) return false
        if (!dv.isDigit() && dv != 'K') return false

        var multiplicador = 2
        val suma = cuerpo.reversed().sumOf { char ->
            val digito = char - '0'
            val resultado = digito * multiplicador
            multiplicador = if (multiplicador == 7) 2 else multiplicador + 1
            resultado
        }

        val resto = suma % 11
        val esperado: Char = when (val v = 11 - resto) {
            11 -> '0'
            10 -> 'K'
            else -> (v + '0'.code).toChar()
        }

        return dv == esperado
    }
}