package br.com.valenstech.letraviva.model;

import androidx.annotation.NonNull;

import java.util.Objects;

public class PlanoLeitura {
    @NonNull
    private final String titulo;

    public PlanoLeitura(@NonNull String titulo) {
        this.titulo = titulo;
    }

    @NonNull
    public String getTitulo() {
        return titulo;
    }

    @Override
    public boolean equals(Object outro) {
        if (this == outro) {
            return true;
        }
        if (outro == null || getClass() != outro.getClass()) {
            return false;
        }
        PlanoLeitura planoLeitura = (PlanoLeitura) outro;
        return titulo.equals(planoLeitura.titulo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(titulo);
    }

    @NonNull
    @Override
    public String toString() {
        return "PlanoLeitura{" +
                "titulo='" + titulo + '\'' +
                '}';
    }
}
