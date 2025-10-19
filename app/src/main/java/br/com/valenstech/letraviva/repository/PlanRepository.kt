package br.com.valenstech.letraviva.repository

import br.com.valenstech.letraviva.model.ReadingPlan
import br.com.valenstech.letraviva.util.ValidacaoConteudo
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.firestore.ktx.firestore

class PlanRepository(
    private val firestore: FirebaseFirestore = Firebase.firestore
) {

    fun fetchPlans(onComplete: (Result<List<ReadingPlan>>) -> Unit) {
        firestore.collection("plans")
            .get()
            .addOnSuccessListener { result ->
                val planosSeguros = mutableListOf<ReadingPlan>()
                result.documents.forEach { doc ->
                    val tituloOriginal = doc.getString("title")
                    if (!ValidacaoConteudo.validarTextoSeguroObrigatorio(tituloOriginal)) {
                        onComplete(Result.failure(IllegalStateException("Plano de leitura contém conteúdo inseguro.")))
                        return@addOnSuccessListener
                    }
                    val tituloSanitizado = ValidacaoConteudo.sanitizarBasico(tituloOriginal!!)
                    planosSeguros.add(ReadingPlan(tituloSanitizado))
                }
                onComplete(Result.success(planosSeguros))
            }
            .addOnFailureListener { exception ->
                onComplete(Result.failure(exception))
            }
    }
}
