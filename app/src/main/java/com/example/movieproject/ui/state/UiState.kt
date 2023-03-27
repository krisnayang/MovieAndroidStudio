package com.example.movieproject.ui.state


// dont forget to remove this
data class UiState<T> (
    val isLoading: Boolean = false,
    val value: T
)

// please rename this
sealed class NewUiState
object Loading : NewUiState()
// you can use the error message for elaborate error
class Error(val errorMessage: String) : NewUiState()
class Success<T>(val value: T) : NewUiState()
