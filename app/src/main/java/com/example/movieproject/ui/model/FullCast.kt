package com.example.movieproject.ui.model

data class FullCast (
    val id: String,
    val title: String,
    val runtimeMins: Int,
    val actors: List<Actors>
)

data class Actors(
    val id: String,
    val image: String,
    val name: String,
    val asCharacter: String
)