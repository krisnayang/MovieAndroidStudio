package com.example.movieproject.data.remote.remotedatasource

import com.example.movieproject.data.local.localdatasource.FullCastEntity
import com.example.movieproject.data.local.localdatasource.MovieEntity
import com.example.movieproject.data.remote.model.Actors
import com.example.movieproject.data.remote.model.FullCast
import com.example.movieproject.data.remote.model.Movie
import com.squareup.moshi.JsonClass


@JsonClass(generateAdapter = true)
data class MoviesResponse(
    val items: List<NetworkMovie>
)

@JsonClass(generateAdapter = true)
data class NetworkMovie (
    val id: String,
    val title: String,
    val year: Int,
    val image: String,
    val imDbRating: String,
    val imDbRatingCount: String,
)

@JsonClass(generateAdapter = true)
data class NetworkFullCast (
    val id: String,
    val actorList: List<Actors>
)

fun MoviesResponse.asDomainModel(): List<Movie> {
    return items.map {
        Movie(
            id = it.id,
            title = it.title,
            year = it.year,
            image = it.image,
            imDbRating = it.imDbRating,
            imDbRatingCount = it.imDbRatingCount
        )
    }
}

fun MoviesResponse.asDatabaseModel(): List<MovieEntity> {
    return items.map {
        MovieEntity(
            id = it.id,
            title = it.title,
            year = it.year,
            image = it.image,
            imDbRating = it.imDbRating,
            imDbRatingCount = it.imDbRatingCount
        )
    }
}

fun NetworkFullCast.asDomainModel(): List<FullCast> {
    return listOf(FullCast(id = id, actors = actorList))
}

fun NetworkFullCast.asDatabaseModel(): List<FullCastEntity> {
    return actorList.map {
        FullCastEntity(
            id = it.id,
            movieId = id,
            image = it.image,
            name = it.name,
            asCharacter = it.asCharacter
        )
    }
}
