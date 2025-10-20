package br.com.valenstech.letraviva.repository;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public interface CallbackResultado<T> {
    void aoSucesso(@Nullable T resultado);

    void aoErro(@NonNull Exception excecao);
}
