package br.com.valenstech.letraviva.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

import br.com.valenstech.letraviva.R;
import br.com.valenstech.letraviva.model.ItemAudio;
import br.com.valenstech.letraviva.repository.CallbackResultado;
import br.com.valenstech.letraviva.repository.RepositorioAudio;
import br.com.valenstech.letraviva.util.EstadoUi;

public class AudiosViewModel extends AndroidViewModel {

    @NonNull
    private final RepositorioAudio repositorioAudio;
    @NonNull
    private final MutableLiveData<EstadoUi<List<ItemAudio>>> estado;

    public AudiosViewModel(@NonNull Application aplicacao) {
        super(aplicacao);
        repositorioAudio = new RepositorioAudio();
        estado = new MutableLiveData<>();
    }

    @NonNull
    public LiveData<EstadoUi<List<ItemAudio>>> getEstado() {
        return estado;
    }

    public void carregarAudios() {
        estado.setValue(new EstadoUi.Carregando<>());
        repositorioAudio.buscarAudios(new CallbackResultado<List<ItemAudio>>() {
            @Override
            public void aoSucesso(List<ItemAudio> resultado) {
                if (resultado == null || resultado.isEmpty()) {
                    String mensagemVazia = getApplication().getString(R.string.empty_audios_message);
                    estado.postValue(new EstadoUi.Vazio<>(mensagemVazia));
                } else {
                    estado.postValue(new EstadoUi.Sucesso<>(resultado));
                }
            }

            @Override
            public void aoErro(@NonNull Exception excecao) {
                String mensagemErro = excecao.getMessage();
                if (mensagemErro == null || mensagemErro.trim().isEmpty()) {
                    mensagemErro = getApplication().getString(R.string.error_loading_audios);
                }
                estado.postValue(new EstadoUi.Erro<>(mensagemErro));
            }
        });
    }
}
