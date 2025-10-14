package br.com.valenstech.letraviva.repository

import br.com.valenstech.letraviva.model.DevotionalContent
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
                    val text = document.getString("text")
                    if (text.isNullOrBlank()) {
                        onComplete(Result.success(null))
                        return@addOnSuccessListener
                    }
                    val content = DevotionalContent(
                        id = document.id,
                        title = document.getString("title") ?: "",
                        text = text,
                        audioUrl = document.getString("audioUrl")
                    )
                    onComplete(Result.success(content))
                } else {
                    onComplete(Result.success(null))
                }
            }
            .addOnFailureListener { exception ->
                onComplete(Result.failure(exception))
            }
    }
}
