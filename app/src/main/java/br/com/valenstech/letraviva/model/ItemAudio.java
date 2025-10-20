package br.com.valenstech.letraviva.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Objects;

public class ItemAudio {
    @NonNull
    private final String id;
    @NonNull
    private final String titulo;
    @NonNull
    private final String urlTransmissao;

    public ItemAudio(@NonNull String id, @NonNull String titulo, @NonNull String urlTransmissao) {
        this.id = id;
        this.titulo = titulo;
        this.urlTransmissao = urlTransmissao;
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
    public String getUrlTransmissao() {
        return urlTransmissao;
    }

    @Override
    public boolean equals(@Nullable Object outro) {
        if (this == outro) {
            return true;
        }
        if (outro == null || getClass() != outro.getClass()) {
            return false;
        }
        ItemAudio itemAudio = (ItemAudio) outro;
        return id.equals(itemAudio.id)
                && titulo.equals(itemAudio.titulo)
                && urlTransmissao.equals(itemAudio.urlTransmissao);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, titulo, urlTransmissao);
    }

    @NonNull
    @Override
    public String toString() {
        return "ItemAudio{" +
                "id='" + id + '\'' +
                ", titulo='" + titulo + '\'' +
                ", urlTransmissao='" + urlTransmissao + '\'' +
                '}';
    }
}
