package br.com.valenstech.letraviva.util

sealed class UiState<out T> {
    object Loading : UiState<Nothing>()
    data class Success<T>(val data: T) : UiState<T>()
    data class Empty(val message: String) : UiState<Nothing>()
    data class Error(val message: String) : UiState<Nothing>()
}
