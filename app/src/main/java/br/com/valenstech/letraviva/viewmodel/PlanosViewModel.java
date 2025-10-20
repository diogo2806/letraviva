package br.com.valenstech.letraviva.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

import br.com.valenstech.letraviva.R;
import br.com.valenstech.letraviva.model.PlanoLeitura;
import br.com.valenstech.letraviva.repository.CallbackResultado;
import br.com.valenstech.letraviva.repository.RepositorioPlano;
import br.com.valenstech.letraviva.util.EstadoUi;

public class PlanosViewModel extends AndroidViewModel {

    @NonNull
    private final RepositorioPlano repositorioPlano;
    @NonNull
    private final MutableLiveData<EstadoUi<List<PlanoLeitura>>> estado;

    public PlanosViewModel(@NonNull Application aplicacao) {
        super(aplicacao);
        repositorioPlano = new RepositorioPlano();
        estado = new MutableLiveData<>();
    }

    @NonNull
    public LiveData<EstadoUi<List<PlanoLeitura>>> getEstado() {
        return estado;
    }

    public void carregarPlanos() {
        estado.setValue(new EstadoUi.Carregando<>());
        repositorioPlano.buscarPlanos(new CallbackResultado<List<PlanoLeitura>>() {
            @Override
            public void aoSucesso(List<PlanoLeitura> resultado) {
                if (resultado == null || resultado.isEmpty()) {
                    String mensagem = getApplication().getString(R.string.empty_plans_message);
                    estado.postValue(new EstadoUi.Vazio<>(mensagem));
                } else {
                    estado.postValue(new EstadoUi.Sucesso<>(resultado));
                }
            }

            @Override
            public void aoErro(@NonNull Exception excecao) {
                String mensagemErro = excecao.getMessage();
                if (mensagemErro == null || mensagemErro.trim().isEmpty()) {
                    mensagemErro = getApplication().getString(R.string.error_loading_plans);
                }
                estado.postValue(new EstadoUi.Erro<>(mensagemErro));
            }
        });
    }
}
