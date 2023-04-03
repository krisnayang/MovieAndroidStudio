package com.example.movieproject.data.remote.remotedatasource

data class MoviesResponse(
    val items: List<NetworkMovie> = emptyList()
)

data class NetworkMovie (
    val id: String,
    val title: String,
    val image: String,
)

data class NetworkMovieById (
    val id: String? = "",
    val image: String? = "",
    val title: String? = "",
    val year: String? = "",
    val runtimeMins: Int? = 0,
    val plot: String? = "",
    val directors: String? = "",
    val genres: String? = "",
    val imDbRating: Double? = 0.0,
    val imDbRatingVotes: Int? = 0,
    val actorList: List<Actors> = emptyList()
)

data class Actors(
    val id: String? = "",
    val image: String? = "",
    val name: String? = "",
    val asCharacter: String? = ""
)
data class SearchMovieResponse(
    val results: List<NetworkSearch> = emptyList()
)

data class NetworkSearch (
    val id: String,
    val title: String,
    val image: String,
)