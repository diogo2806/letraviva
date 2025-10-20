package br.com.valenstech.letraviva.repository;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import br.com.valenstech.letraviva.model.ConteudoDevocional;
import br.com.valenstech.letraviva.util.ValidacaoConteudo;

public class RepositorioDevocional {

    @NonNull
    private final FirebaseFirestore firestore;

    public RepositorioDevocional() {
        this(FirebaseFirestore.getInstance());
    }

    public RepositorioDevocional(@NonNull FirebaseFirestore firestore) {
        this.firestore = firestore;
    }

    public void buscarDevocionalMaisRecente(@NonNull final CallbackResultado<ConteudoDevocional> callback) {
        firestore.collection("devotionals")
                .orderBy("date", Query.Direction.DESCENDING)
                .limit(1)
                .get()
                .addOnSuccessListener(snapshot -> {
                    DocumentSnapshot documento = snapshot.getDocuments().isEmpty() ? null : snapshot.getDocuments().get(0);
                    if (documento == null) {
                        callback.aoSucesso(null);
                        return;
                    }

                    String textoOriginal = documento.getString("text");
                    if (textoOriginal == null || textoOriginal.trim().isEmpty()) {
                        callback.aoSucesso(null);
                        return;
                    }
                    if (!ValidacaoConteudo.validarTextoSeguroObrigatorio(textoOriginal)) {
                        callback.aoErro(new IllegalStateException("Conteúdo inseguro detectado no devocional."));
                        return;
                    }

                    String tituloOriginal = documento.getString("title");
                    if (!ValidacaoConteudo.validarTextoSeguroOpcional(tituloOriginal)) {
                        callback.aoErro(new IllegalStateException("Título do devocional contém conteúdo inseguro."));
                        return;
                    }

                    String audioUrlOriginal = documento.getString("audioUrl");
                    @Nullable String audioUrlSanitizada = null;
                    if (audioUrlOriginal != null && !audioUrlOriginal.trim().isEmpty()) {
                        String urlPreparada = ValidacaoConteudo.sanitizarBasico(audioUrlOriginal);
                        if (ValidacaoConteudo.validarUrlSegura(urlPreparada)) {
                            audioUrlSanitizada = urlPreparada;
                        } else {
                            callback.aoErro(new IllegalStateException("Endereço de áudio inválido ou inseguro."));
                            return;
                        }
                    }

                    ConteudoDevocional conteudoDevocional = new ConteudoDevocional(
                            documento.getId(),
                            ValidacaoConteudo.sanitizarBasico(tituloOriginal == null ? "" : tituloOriginal),
                            ValidacaoConteudo.sanitizarBasico(textoOriginal),
                            audioUrlSanitizada
                    );
                    callback.aoSucesso(conteudoDevocional);
                })
                .addOnFailureListener(callback::aoErro);
    }
}
