package br.com.valenstech.letraviva.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import br.com.valenstech.letraviva.R;
import br.com.valenstech.letraviva.model.ConteudoDevocional;
import br.com.valenstech.letraviva.repository.CallbackResultado;
import br.com.valenstech.letraviva.repository.RepositorioDevocional;
import br.com.valenstech.letraviva.util.EstadoUi;

public class DevocionalViewModel extends AndroidViewModel {

    @NonNull
    private final RepositorioDevocional repositorioDevocional;
    @NonNull
    private final MutableLiveData<EstadoUi<ConteudoDevocional>> estado;

    public DevocionalViewModel(@NonNull Application aplicacao) {
        super(aplicacao);
        repositorioDevocional = new RepositorioDevocional();
        estado = new MutableLiveData<>();
    }

    @NonNull
    public LiveData<EstadoUi<ConteudoDevocional>> getEstado() {
        return estado;
    }

    public void carregarDevocional() {
        estado.setValue(new EstadoUi.Carregando<>());
        repositorioDevocional.buscarDevocionalMaisRecente(new CallbackResultado<ConteudoDevocional>() {
            @Override
            public void aoSucesso(ConteudoDevocional resultado) {
                if (resultado == null) {
                    String mensagem = getApplication().getString(R.string.empty_devotional_message);
                    estado.postValue(new EstadoUi.Vazio<>(mensagem));
                } else {
                    estado.postValue(new EstadoUi.Sucesso<>(resultado));
                }
            }

            @Override
            public void aoErro(@NonNull Exception excecao) {
                String mensagemErro = excecao.getMessage();
                if (mensagemErro == null || mensagemErro.trim().isEmpty()) {
                    mensagemErro = getApplication().getString(R.string.error_loading_devotional);
                }
                estado.postValue(new EstadoUi.Erro<>(mensagemErro));
            }
        });
    }
}
