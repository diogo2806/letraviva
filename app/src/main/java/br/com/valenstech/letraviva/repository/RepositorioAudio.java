package br.com.valenstech.letraviva.repository;

import androidx.annotation.NonNull;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

import br.com.valenstech.letraviva.model.ItemAudio;
import br.com.valenstech.letraviva.util.ValidacaoConteudo;

public class RepositorioAudio {

    @NonNull
    private final FirebaseFirestore firestore;

    public RepositorioAudio() {
        this(FirebaseFirestore.getInstance());
    }

    public RepositorioAudio(@NonNull FirebaseFirestore firestore) {
        this.firestore = firestore;
    }

    public void buscarAudios(@NonNull final CallbackResultado<List<ItemAudio>> callback) {
        firestore.collection("audios")
                .get()
                .addOnSuccessListener(snapshot -> {
                    List<ItemAudio> itensSeguros = new ArrayList<>();
                    for (DocumentSnapshot documento : snapshot.getDocuments()) {
                        String tituloOriginal = documento.getString("title");
                        if (!ValidacaoConteudo.validarTextoSeguroObrigatorio(tituloOriginal)) {
                            callback.aoErro(new IllegalStateException("Título de áudio inválido ou inseguro."));
                            return;
                        }

                        String urlOriginal = documento.getString("audioUrl");
                        if (!ValidacaoConteudo.validarUrlSegura(urlOriginal)) {
                            callback.aoErro(new IllegalStateException("Endereço de áudio inválido ou inseguro."));
                            return;
                        }

                        String urlSegura = ValidacaoConteudo.sanitizarBasico(urlOriginal);
                        ItemAudio itemAudio = new ItemAudio(
                                documento.getId(),
                                ValidacaoConteudo.sanitizarBasico(tituloOriginal),
                                urlSegura
                        );
                        itensSeguros.add(itemAudio);
                    }
                    callback.aoSucesso(itensSeguros);
                })
                .addOnFailureListener(callback::aoErro);
    }
}
