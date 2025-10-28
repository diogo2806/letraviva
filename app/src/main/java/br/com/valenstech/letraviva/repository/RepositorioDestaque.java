package br.com.valenstech.letraviva.repository;

import androidx.annotation.NonNull;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import br.com.valenstech.letraviva.model.DestaquePrincipal;
import br.com.valenstech.letraviva.util.ValidacaoConteudo;

public class RepositorioDestaque {

    @NonNull
    private final FirebaseFirestore firestore;

    public RepositorioDestaque() {
        this(FirebaseFirestore.getInstance());
    }

    public RepositorioDestaque(@NonNull FirebaseFirestore firestore) {
        this.firestore = firestore;
    }

    public void buscarDestaquePrincipal(@NonNull final CallbackResultado<DestaquePrincipal> callback) {
        firestore.collection("highlights")
                .limit(1)
                .get()
                .addOnSuccessListener(snapshot -> {
                    DocumentSnapshot documento = snapshot.getDocuments().isEmpty() ? null : snapshot.getDocuments().get(0);
                    if (documento == null) {
                        callback.aoSucesso(null);
                        return;
                    }

                    String descricaoOriginal = documento.getString("description");
                    if (!ValidacaoConteudo.validarTextoSeguroObrigatorio(descricaoOriginal)) {
                        callback.aoErro(new IllegalStateException("Descrição do destaque inválida ou insegura."));
                        return;
                    }

                    String tituloOriginal = documento.getString("title");
                    if (!ValidacaoConteudo.validarTextoSeguroOpcional(tituloOriginal)) {
                        callback.aoErro(new IllegalStateException("Título do destaque contém conteúdo inseguro."));
                        return;
                    }

                    String tituloSanitizado = ValidacaoConteudo.sanitizarBasico(tituloOriginal == null ? "" : tituloOriginal);
                    String descricaoSanitizada = ValidacaoConteudo.sanitizarBasico(descricaoOriginal);
                    if (descricaoSanitizada.isEmpty()) {
                        callback.aoErro(new IllegalStateException("Descrição do destaque ausente."));
                        return;
                    }

                    String idDocumento = documento.getId();
                    if (idDocumento == null || idDocumento.trim().isEmpty()) {
                        callback.aoErro(new IllegalStateException("Identificador do destaque inválido."));
                        return;
                    }

                    String idSanitizado = ValidacaoConteudo.sanitizarBasico(idDocumento);
                    if (idSanitizado.isEmpty()) {
                        callback.aoErro(new IllegalStateException("Identificador do destaque vazio."));
                        return;
                    }

                    DestaquePrincipal destaque = new DestaquePrincipal(
                            idSanitizado,
                            tituloSanitizado,
                            descricaoSanitizada
                    );
                    callback.aoSucesso(destaque);
                })
                .addOnFailureListener(callback::aoErro);
    }
}
