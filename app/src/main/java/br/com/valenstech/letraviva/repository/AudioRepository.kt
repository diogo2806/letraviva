package br.com.valenstech.letraviva.repository

import br.com.valenstech.letraviva.model.AudioItem
import br.com.valenstech.letraviva.util.ValidacaoConteudo
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class AudioRepository(
    private val firestore: FirebaseFirestore = Firebase.firestore
) {

    fun fetchAudios(onComplete: (Result<List<AudioItem>>) -> Unit) {
        firestore.collection("audios")
            .get()
            .addOnSuccessListener { snapshot ->
                val itensSeguros = mutableListOf<AudioItem>()
                snapshot.documents.forEach { doc ->
                    val tituloOriginal = doc.getString("title")
                    if (!ValidacaoConteudo.validarTextoSeguroObrigatorio(tituloOriginal)) {
                        onComplete(Result.failure(IllegalStateException("Título de áudio inválido ou inseguro.")))
                        return@addOnSuccessListener
                    }

                    val urlOriginal = doc.getString("audioUrl")
                    val urlSegura = if (ValidacaoConteudo.validarUrlSegura(urlOriginal)) {
                        ValidacaoConteudo.sanitizarBasico(urlOriginal!!)
                    } else {
                        onComplete(Result.failure(IllegalStateException("Endereço de áudio inválido ou inseguro.")))
                        return@addOnSuccessListener
                    }

                    val item = AudioItem(
                        id = doc.id,
                        title = ValidacaoConteudo.sanitizarBasico(tituloOriginal!!),
                        streamUrl = urlSegura,
                    )
                    itensSeguros.add(item)
                }
                onComplete(Result.success(itensSeguros))
            }
            .addOnFailureListener { exception ->
                onComplete(Result.failure(exception))
            }
    }
}
