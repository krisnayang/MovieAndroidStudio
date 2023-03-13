package com.example.movieproject.data.remote.remotedatasource

import com.example.movieproject.data.local.localdatasource.FullCastEntity
import com.example.movieproject.data.local.localdatasource.MovieDetailEntity
import com.example.movieproject.data.local.localdatasource.MovieEntity
import com.example.movieproject.data.local.model.Movie
import com.example.movieproject.data.remote.model.Actors
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
data class NetworkMovieById (
    val id: String,
    val image: String,
    val title: String,
    val year: String,
    val runtimeMins: Int,
    val plot: String,
    val directors: String,
    val genres: String,
    val imDbRating: String,
    val imDbRatingVotes: Int,
    val actorList: List<Actors>
)

@JsonClass(generateAdapter = true)
data class SearchMovieResponse(
    val results: List<NetworkSearch>
)

@JsonClass(generateAdapter = true)
data class NetworkSearch (
    val id: String,
    val title: String,
    val image: String,
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

fun NetworkMovieById.asDatabaseModel(): List<FullCastEntity> {
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

fun NetworkMovieById.asDatabaseMovieDetail(): List<MovieDetailEntity> {
    return listOf(
            MovieDetailEntity(
            id = id,
            image = image,
            title = title,
            year = year,
            runtimeMins = runtimeMins,
            plot = plot,
            directors = directors,
            genres = genres,
            imDbRating = imDbRating,
            imDbRatingVotes = imDbRatingVotes
        )
    )
}

fun SearchMovieResponse.asList(): List<Movie>{
    return results.map {
        Movie(
            id = it.id,
            title = it.title,
            image = it.image,
            year = 0,
            imDbRatingCount = "",
            imDbRating = ""
        )
    }
}
