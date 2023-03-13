package com.example.movieproject.data.local.model

data class FullCast (
    val id: String,
    val actors: List<Actors>
)

data class Actors(
    val id: String,
    val image: String,
    val name: String,
    val asCharacter: String
)