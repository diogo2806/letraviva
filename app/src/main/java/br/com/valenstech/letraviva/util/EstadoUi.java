package br.com.valenstech.letraviva.util;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public abstract class EstadoUi<T> {

    private EstadoUi() {
        // classe base selada
    }

    public static final class Carregando<T> extends EstadoUi<T> {
        public Carregando() {
        }
    }

    public static final class Sucesso<T> extends EstadoUi<T> {
        @NonNull
        private final T dados;

        public Sucesso(@NonNull T dados) {
            this.dados = dados;
        }

        @NonNull
        public T getDados() {
            return dados;
        }
    }

    public static final class Vazio<T> extends EstadoUi<T> {
        @NonNull
        private final String mensagem;

        public Vazio(@NonNull String mensagem) {
            this.mensagem = mensagem;
        }

        @NonNull
        public String getMensagem() {
            return mensagem;
        }
    }

    public static final class Erro<T> extends EstadoUi<T> {
        @Nullable
        private final String mensagem;

        public Erro(@Nullable String mensagem) {
            this.mensagem = mensagem;
        }

        @Nullable
        public String getMensagem() {
            return mensagem;
        }
    }
}
