package br.com.valenstech.letraviva.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import br.com.valenstech.letraviva.R;
import br.com.valenstech.letraviva.model.DestaquePrincipal;
import br.com.valenstech.letraviva.repository.CallbackResultado;
import br.com.valenstech.letraviva.repository.RepositorioDestaque;
import br.com.valenstech.letraviva.util.EstadoUi;

public class HomeViewModel extends AndroidViewModel {

    @NonNull
    private final RepositorioDestaque repositorioDestaque;
    @NonNull
    private final MutableLiveData<EstadoUi<DestaquePrincipal>> estadoDestaque;

    public HomeViewModel(@NonNull Application aplicacao) {
        super(aplicacao);
        repositorioDestaque = new RepositorioDestaque();
        estadoDestaque = new MutableLiveData<>();
    }

    @NonNull
    public LiveData<EstadoUi<DestaquePrincipal>> getEstadoDestaque() {
        return estadoDestaque;
    }

    public void carregarDestaque() {
        estadoDestaque.setValue(new EstadoUi.Carregando<>());
        repositorioDestaque.buscarDestaquePrincipal(new CallbackResultado<DestaquePrincipal>() {
            @Override
            public void aoSucesso(DestaquePrincipal resultado) {
                if (resultado == null) {
                    String mensagem = getApplication().getString(R.string.empty_highlight_message);
                    estadoDestaque.postValue(new EstadoUi.Vazio<>(mensagem));
                } else {
                    estadoDestaque.postValue(new EstadoUi.Sucesso<>(resultado));
                }
            }

            @Override
            public void aoErro(@NonNull Exception excecao) {
                String mensagemErro = excecao.getMessage();
                if (mensagemErro == null || mensagemErro.trim().isEmpty()) {
                    mensagemErro = getApplication().getString(R.string.error_loading_highlight);
                }
                estadoDestaque.postValue(new EstadoUi.Erro<>(mensagemErro));
            }
        });
    }
}
