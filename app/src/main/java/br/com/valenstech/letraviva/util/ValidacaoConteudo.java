package br.com.valenstech.letraviva.util;

import android.webkit.URLUtil;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.regex.Pattern;

public final class ValidacaoConteudo {

    private static final Pattern PADRAO_TAG_HTML = Pattern.compile("<[^>]+>", Pattern.CASE_INSENSITIVE);
    private static final Pattern PADRAO_SCRIPT_OU_EVENTO = Pattern.compile(
            "(?i)(<\\s*/?\\s*script|&lt;\\s*/?\\s*script|&#x3c;\\s*/?\\s*script|javascript:|data:text/html|on[a-z]+\\s*=)"
    );
    private static final Pattern PADRAO_CARACTERE_CONTROLE = Pattern.compile("[\\p{C}&&[^\\n\\r\\t]]");
    private static final Pattern PADRAO_URL_HTTP_HTTPS = Pattern.compile("^https?://.+", Pattern.CASE_INSENSITIVE);

    private ValidacaoConteudo() {
        // Utilitário
    }

    public static boolean possuiHtmlSuspeito(@NonNull String texto) {
        String analisado = texto.trim();
        return PADRAO_TAG_HTML.matcher(analisado).find() || PADRAO_SCRIPT_OU_EVENTO.matcher(analisado).find();
    }

    public static boolean validarTextoSeguroObrigatorio(@Nullable String texto) {
        if (texto == null || texto.trim().isEmpty()) {
            return false;
        }
        return !possuiHtmlSuspeito(texto);
    }

    public static boolean validarTextoSeguroOpcional(@Nullable String texto) {
        if (texto == null || texto.trim().isEmpty()) {
            return true;
        }
        return !possuiHtmlSuspeito(texto);
    }

    public static boolean validarUrlSegura(@Nullable String url) {
        if (url == null || url.trim().isEmpty()) {
            return false;
        }
        String limpa = sanitizarBasico(url);
        if (!PADRAO_URL_HTTP_HTTPS.matcher(limpa).matches()) {
            return false;
        }
        if (!URLUtil.isValidUrl(limpa)) {
            return false;
        }
        return !possuiHtmlSuspeito(limpa);
    }

    @NonNull
    public static String sanitizarBasico(@NonNull String texto) {
        return PADRAO_CARACTERE_CONTROLE.matcher(texto).replaceAll("").trim();
    }

    @NonNull
    public static String aplicarEscapeSeguro(@NonNull String texto) {
        if (texto.isEmpty()) {
            return "";
        }
        StringBuilder resultado = new StringBuilder(texto.length());
        for (int indice = 0; indice < texto.length(); indice++) {
            char caractere = texto.charAt(indice);
            switch (caractere) {
                case '<':
                    resultado.append('\uFF1C');
                    break;
                case '>':
                    resultado.append('\uFF1E');
                    break;
                case '&':
                    resultado.append('\uFF06');
                    break;
                case '\"':
                    resultado.append('\uFF02');
                    break;
                case '\'':
                    resultado.append('\uFF07');
                    break;
                default:
                    resultado.append(caractere);
                    break;
            }
        }
        return resultado.toString();
    }
}
