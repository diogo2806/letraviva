package br.com.valenstech.letraviva.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.com.valenstech.letraviva.R
import br.com.valenstech.letraviva.model.ReadingPlan
import br.com.valenstech.letraviva.repository.PlanRepository
import br.com.valenstech.letraviva.util.UiState

class PlanosViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = PlanRepository()
    private val _state = MutableLiveData<UiState<List<ReadingPlan>>>()
    val state: LiveData<UiState<List<ReadingPlan>>> = _state

    fun loadPlans() {
        _state.value = UiState.Loading
        repository.fetchPlans { result ->
            result.onSuccess { plans ->
                if (plans.isEmpty()) {
                    _state.postValue(
                        UiState.Empty(
                            getApplication<Application>().getString(R.string.empty_plans_message)
                        )
                    )
                } else {
                    _state.postValue(UiState.Success(plans))
                }
            }.onFailure { exception ->
                _state.postValue(
                    UiState.Error(
                        exception.message
                            ?: getApplication<Application>().getString(R.string.error_loading_plans)
                    )
                )
            }
        }
    }
}
