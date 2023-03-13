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
    val title: String,
    val runtimeMins: Int,
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
    return listOf(FullCast(id = id, title = title, runtimeMins = runtimeMins, actors = actorList))
}

fun NetworkFullCast.asDatabaseModel(): List<FullCastEntity> {
    return listOf(FullCastEntity(id = id,
        title = title,
        runtimeMins = runtimeMins,
    image = actorList[0].image,
    name = actorList[0].name,
    asCharacter = actorList[0].asCharacter))
}
