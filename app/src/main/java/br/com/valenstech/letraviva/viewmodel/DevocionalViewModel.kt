package br.com.valenstech.letraviva.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.com.valenstech.letraviva.R
import br.com.valenstech.letraviva.model.DevotionalContent
import br.com.valenstech.letraviva.repository.DevotionalRepository
import br.com.valenstech.letraviva.util.UiState

class DevocionalViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = DevotionalRepository()
    private val _state = MutableLiveData<UiState<DevotionalContent>>()
    val state: LiveData<UiState<DevotionalContent>> = _state

    fun loadDevotional() {
        _state.value = UiState.Loading
        repository.fetchLatestDevotional { result ->
            result.onSuccess { content ->
                if (content == null) {
                    _state.postValue(
                        UiState.Empty(
                            getApplication<Application>().getString(R.string.empty_devotional_message)
                        )
                    )
                } else {
                    _state.postValue(UiState.Success(content))
                }
            }.onFailure { exception ->
                _state.postValue(
                    UiState.Error(
                        exception.message
                            ?: getApplication<Application>().getString(R.string.error_loading_devotional)
                    )
                )
            }
        }
    }
}
