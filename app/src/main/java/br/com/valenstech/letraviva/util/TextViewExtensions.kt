package br.com.valenstech.letraviva.util

import android.widget.TextView

fun TextView.definirTextoSeguro(texto: String?) {
    this.text = ValidacaoConteudo.aplicarEscapeSeguro(texto.orEmpty())
}
