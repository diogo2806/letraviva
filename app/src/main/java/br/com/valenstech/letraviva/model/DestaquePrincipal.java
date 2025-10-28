package br.com.valenstech.letraviva.model;

import androidx.annotation.NonNull;

import java.util.Objects;

public class DestaquePrincipal {

    @NonNull
    private final String id;
    @NonNull
    private final String titulo;
    @NonNull
    private final String descricao;

    public DestaquePrincipal(@NonNull String id,
                             @NonNull String titulo,
                             @NonNull String descricao) {
        this.id = id;
        this.titulo = titulo;
        this.descricao = descricao;
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
    public String getDescricao() {
        return descricao;
    }

    @Override
    public boolean equals(Object outro) {
        if (this == outro) {
            return true;
        }
        if (outro == null || getClass() != outro.getClass()) {
            return false;
        }
        DestaquePrincipal que = (DestaquePrincipal) outro;
        return id.equals(que.id)
                && titulo.equals(que.titulo)
                && descricao.equals(que.descricao);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, titulo, descricao);
    }

    @NonNull
    @Override
    public String toString() {
        return "DestaquePrincipal{" +
                "id='" + id + '\'' +
                ", titulo='" + titulo + '\'' +
                ", descricao='" + descricao + '\'' +
                '}';
    }
}
