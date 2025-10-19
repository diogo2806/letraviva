package br.com.valenstech.letraviva.util

import android.webkit.URLUtil

object ValidacaoConteudo {

    private val padraoTagHtml = Regex("<[^>]+>", RegexOption.IGNORE_CASE)
    private val padraoScriptOuEvento = Regex("(?i)(<\\s*/?\\s*script|&lt;\\s*/?\\s*script|&#x3c;\\s*/?\\s*script|javascript:|data:text/html|on[a-z]+\\s*=)")
    private val padraoCaracterControle = Regex("[\\p{C}&&[^\\n\\r\\t]]")
    private val padraoUrlHttpHttps = Regex("^https?://.+", RegexOption.IGNORE_CASE)

    fun possuiHtmlSuspeito(texto: String): Boolean {
        val analisado = texto.trim()
        return padraoTagHtml.containsMatchIn(analisado) || padraoScriptOuEvento.containsMatchIn(analisado)
    }

    fun validarTextoSeguroObrigatorio(texto: String?): Boolean {
        if (texto.isNullOrBlank()) {
            return false
        }
        return !possuiHtmlSuspeito(texto)
    }

    fun validarTextoSeguroOpcional(texto: String?): Boolean {
        if (texto.isNullOrBlank()) {
            return true
        }
        return !possuiHtmlSuspeito(texto)
    }

    fun validarUrlSegura(url: String?): Boolean {
        if (url.isNullOrBlank()) {
            return false
        }
        val limpa = sanitizarBasico(url)
        if (!padraoUrlHttpHttps.matches(limpa)) {
            return false
        }
        if (!URLUtil.isValidUrl(limpa)) {
            return false
        }
        if (possuiHtmlSuspeito(limpa)) {
            return false
        }
        return true
    }

    fun sanitizarBasico(texto: String): String {
        return texto.replace(padraoCaracterControle, "").trim()
    }

    fun aplicarEscapeSeguro(texto: String): String {
        if (texto.isEmpty()) {
            return ""
        }
        val resultado = StringBuilder(texto.length)
        texto.forEach { caractere ->
            when (caractere) {
                '<' -> resultado.append('\uFF1C')
                '>' -> resultado.append('\uFF1E')
                '&' -> resultado.append('\uFF06')
                '\"' -> resultado.append('\uFF02')
                '\'' -> resultado.append('\uFF07')
                else -> resultado.append(caractere)
            }
        }
        return resultado.toString()
    }
}
