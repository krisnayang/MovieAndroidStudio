package com.example.movieproject.data.remote.model

data class Movie (
    val id: String,
    val title: String,
    val year: Int,
    val image: String,
    val imDbRating: String,
    val imDbRatingCount: String,
)