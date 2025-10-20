package br.com.valenstech.letraviva.repository;

import androidx.annotation.NonNull;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

import br.com.valenstech.letraviva.model.PlanoLeitura;
import br.com.valenstech.letraviva.util.ValidacaoConteudo;

public class RepositorioPlano {

    @NonNull
    private final FirebaseFirestore firestore;

    public RepositorioPlano() {
        this(FirebaseFirestore.getInstance());
    }

    public RepositorioPlano(@NonNull FirebaseFirestore firestore) {
        this.firestore = firestore;
    }

    public void buscarPlanos(@NonNull final CallbackResultado<List<PlanoLeitura>> callback) {
        firestore.collection("plans")
                .get()
                .addOnSuccessListener(resultado -> {
                    List<PlanoLeitura> planosSeguros = new ArrayList<>();
                    for (DocumentSnapshot documento : resultado.getDocuments()) {
                        String tituloOriginal = documento.getString("title");
                        if (!ValidacaoConteudo.validarTextoSeguroObrigatorio(tituloOriginal)) {
                            callback.aoErro(new IllegalStateException("Plano de leitura contém conteúdo inseguro."));
                            return;
                        }
                        String tituloSanitizado = ValidacaoConteudo.sanitizarBasico(tituloOriginal);
                        planosSeguros.add(new PlanoLeitura(tituloSanitizado));
                    }
                    callback.aoSucesso(planosSeguros);
                })
                .addOnFailureListener(callback::aoErro);
    }
}
