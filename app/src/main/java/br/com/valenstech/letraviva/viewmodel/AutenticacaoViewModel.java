package br.com.valenstech.letraviva.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class AutenticacaoViewModel extends AndroidViewModel {

    @NonNull
    private final FirebaseAuth autenticacao;
    @NonNull
    private final MutableLiveData<FirebaseUser> usuario;

    public AutenticacaoViewModel(@NonNull Application aplicacao) {
        super(aplicacao);
        autenticacao = FirebaseAuth.getInstance();
        usuario = new MutableLiveData<>();
    }

    @NonNull
    public LiveData<FirebaseUser> getUsuario() {
        return usuario;
    }

    public void verificarUsuario() {
        usuario.setValue(autenticacao.getCurrentUser());
    }

    public void autenticarComGoogle(@NonNull String idToken) {
        autenticacao.signInWithCredential(GoogleAuthProvider.getCredential(idToken, null))
                .addOnCompleteListener(tarefa -> {
                    if (tarefa.isSuccessful()) {
                        usuario.setValue(autenticacao.getCurrentUser());
                    } else {
                        usuario.setValue(null);
                    }
                });
    }

    public void sair() {
        autenticacao.signOut();
        usuario.setValue(null);
    }
}
