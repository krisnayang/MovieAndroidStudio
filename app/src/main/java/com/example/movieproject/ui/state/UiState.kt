package com.example.movieproject.ui.state

data class UiState<T> (
    val isLoading: Boolean = false,
    val value: T
        )