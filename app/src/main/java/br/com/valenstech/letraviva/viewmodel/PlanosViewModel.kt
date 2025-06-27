package br.com.valenstech.letraviva.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.com.valenstech.letraviva.model.ReadingPlan
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class PlanosViewModel(application: Application) : AndroidViewModel(application) {

    private val _plans = MutableLiveData<List<ReadingPlan>>()
    val plans: LiveData<List<ReadingPlan>> = _plans

    private val db = Firebase.firestore

    fun loadPlans() {
        db.collection("plans").get()
            .addOnSuccessListener { result ->
                val list = result.documents.mapNotNull { doc ->
                    doc.getString("title")?.let { ReadingPlan(it) }
                }
                _plans.value = list
            }
            .addOnFailureListener {
                _plans.value = emptyList()
            }
    }
}
