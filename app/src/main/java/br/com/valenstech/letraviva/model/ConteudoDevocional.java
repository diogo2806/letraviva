package br.com.valenstech.letraviva.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Objects;

public class ConteudoDevocional {
    @NonNull
    private final String id;
    @NonNull
    private final String titulo;
    @NonNull
    private final String texto;
    @Nullable
    private final String urlAudio;

    public ConteudoDevocional(@NonNull String id,
                              @NonNull String titulo,
                              @NonNull String texto,
                              @Nullable String urlAudio) {
        this.id = id;
        this.titulo = titulo;
        this.texto = texto;
        this.urlAudio = urlAudio;
    }

    @NonNull
    public String getId() {
        return id;
    }

    @NonNull
    public String getTitulo() {
        return titulo;
    }

    @NonNull
    public String getTexto() {
        return texto;
    }

    @Nullable
    public String getUrlAudio() {
        return urlAudio;
    }

    @Override
    public boolean equals(@Nullable Object outro) {
        if (this == outro) {
            return true;
        }
        if (outro == null || getClass() != outro.getClass()) {
            return false;
        }
        ConteudoDevocional that = (ConteudoDevocional) outro;
        return id.equals(that.id)
                && titulo.equals(that.titulo)
                && texto.equals(that.texto)
                && Objects.equals(urlAudio, that.urlAudio);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, titulo, texto, urlAudio);
    }

    @NonNull
    @Override
    public String toString() {
        return "ConteudoDevocional{" +
                "id='" + id + '\'' +
                ", titulo='" + titulo + '\'' +
                ", texto='" + texto + '\'' +
                ", urlAudio='" + urlAudio + '\'' +
                '}';
    }
}
