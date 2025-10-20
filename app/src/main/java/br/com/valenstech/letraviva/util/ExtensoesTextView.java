package br.com.valenstech.letraviva.util;

import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public final class ExtensoesTextView {
    private ExtensoesTextView() {
        // utilitário
    }

    public static void definirTextoSeguro(@NonNull TextView textView, @Nullable String texto) {
        textView.setText(ValidacaoConteudo.aplicarEscapeSeguro(texto == null ? "" : texto));
    }
}
