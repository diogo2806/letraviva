package br.com.valenstech.letraviva.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.com.valenstech.letraviva.R
import br.com.valenstech.letraviva.model.AudioItem
import br.com.valenstech.letraviva.repository.AudioRepository
import br.com.valenstech.letraviva.util.UiState

class AudiosViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = AudioRepository()
    private val _state = MutableLiveData<UiState<List<AudioItem>>>()
    val state: LiveData<UiState<List<AudioItem>>> = _state

    fun loadAudios() {
        _state.value = UiState.Loading
        repository.fetchAudios { result ->
            result.onSuccess { audios ->
                if (audios.isEmpty()) {
                    _state.postValue(
                        UiState.Empty(
                            getApplication<Application>().getString(R.string.empty_audios_message)
                        )
                    )
                } else {
                    _state.postValue(UiState.Success(audios))
                }
            }.onFailure { exception ->
                _state.postValue(
                    UiState.Error(
                        exception.message
                            ?: getApplication<Application>().getString(R.string.error_loading_audios)
                    )
                )
            }
        }
    }
}
