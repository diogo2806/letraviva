package br.com.valenstech.letraviva.repository

import br.com.valenstech.letraviva.model.AudioItem
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
                val audios = snapshot.documents.mapNotNull { doc ->
                    val title = doc.getString("title")
                    val url = doc.getString("audioUrl")
                    if (!title.isNullOrBlank() && !url.isNullOrBlank()) {
                        AudioItem(doc.id, title, url)
                    } else {
                        null
                    }
                }
                onComplete(Result.success(audios))
            }
            .addOnFailureListener { exception ->
                onComplete(Result.failure(exception))
            }
    }
}
