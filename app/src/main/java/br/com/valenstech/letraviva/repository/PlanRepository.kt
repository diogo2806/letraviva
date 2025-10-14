package br.com.valenstech.letraviva.repository

import br.com.valenstech.letraviva.model.ReadingPlan
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
                val plans = result.documents.mapNotNull { doc ->
                    doc.getString("title")?.takeIf { it.isNotBlank() }?.let { ReadingPlan(it) }
                }
                onComplete(Result.success(plans))
            }
            .addOnFailureListener { exception ->
                onComplete(Result.failure(exception))
            }
    }
}
