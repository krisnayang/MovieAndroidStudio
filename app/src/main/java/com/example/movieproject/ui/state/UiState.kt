package com.example.movieproject.ui.state

data class UiState<T> (
    val isLoading: Boolean = false,
    val value: T
        )

sealed class NewUiState
object Loading : NewUiState()
class Error(val errorMessage: String) : NewUiState()
class Success<T>(val value: T) : NewUiState()
