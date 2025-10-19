package br.com.valenstech.letraviva.repository

import br.com.valenstech.letraviva.model.DevotionalContent
import br.com.valenstech.letraviva.util.ValidacaoConteudo
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class DevotionalRepository(
    private val firestore: FirebaseFirestore = Firebase.firestore
) {

    fun fetchLatestDevotional(onComplete: (Result<DevotionalContent?>) -> Unit) {
        firestore.collection("devotionals")
            .orderBy("date", Query.Direction.DESCENDING)
            .limit(1)
            .get()
            .addOnSuccessListener { snapshot ->
                val document = snapshot.documents.firstOrNull()
                if (document != null) {
                    val textoOriginal = document.getString("text")
                    if (textoOriginal.isNullOrBlank()) {
                        onComplete(Result.success(null))
                        return@addOnSuccessListener
                    }
                    if (!ValidacaoConteudo.validarTextoSeguroObrigatorio(textoOriginal)) {
                        onComplete(Result.failure(IllegalStateException("Conteúdo inseguro detectado no devocional.")))
                        return@addOnSuccessListener
                    }

                    val tituloOriginal = document.getString("title")
                    if (!ValidacaoConteudo.validarTextoSeguroOpcional(tituloOriginal)) {
                        onComplete(Result.failure(IllegalStateException("Título do devocional contém conteúdo inseguro.")))
                        return@addOnSuccessListener
                    }

                    val audioUrlOriginal = document.getString("audioUrl")
                    val audioUrlSanitizada = if (audioUrlOriginal.isNullOrBlank()) {
                        null
                    } else {
                        val urlPreparada = ValidacaoConteudo.sanitizarBasico(audioUrlOriginal)
                        if (ValidacaoConteudo.validarUrlSegura(urlPreparada)) {
                            urlPreparada
                        } else {
                            onComplete(Result.failure(IllegalStateException("Endereço de áudio inválido ou inseguro.")))
                            return@addOnSuccessListener
                        }
                    }

                    val conteudo = DevotionalContent(
                        id = document.id,
                        title = ValidacaoConteudo.sanitizarBasico(tituloOriginal.orEmpty()),
                        text = ValidacaoConteudo.sanitizarBasico(textoOriginal!!),
                        audioUrl = audioUrlSanitizada,
                    )
                    onComplete(Result.success(conteudo))
                } else {
                    onComplete(Result.success(null))
                }
            }
            .addOnFailureListener { exception ->
                onComplete(Result.failure(exception))
            }
    }
}
