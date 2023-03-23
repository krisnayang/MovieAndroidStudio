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
    val image: String,
)

@JsonClass(generateAdapter = true)
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
            image = it.image,
        )
    }
}

fun MoviesResponse.asDatabaseFullCast(): List<MovieEntity> {
    return items.map {
        MovieEntity(
            id = it.id,
            title = it.title,
            image = it.image,
        )
    }
}

fun NetworkMovieById.asDatabaseFullCast(): List<FullCastEntity> {
    return actorList.map {
        FullCastEntity(
            id = it.id.toString(),
            movieId = id.toString(),
            image = it.image.toString(),
            name = it.name.toString(),
            asCharacter = it.asCharacter.toString()
        )
    }
}

fun NetworkMovieById.asDatabaseMovieDetail(): List<MovieDetailEntity> {
    return listOf(
            MovieDetailEntity(
            id = id.toString(),
            image = image.toString(),
            title = title.toString(),
            year = year.toString(),
            runtimeMins = runtimeMins?:0,
            plot = plot.toString(),
            directors = directors.toString(),
            genres = genres.toString(),
            imDbRating = imDbRating?: 0.0,
            imDbRatingVotes = imDbRatingVotes?:0
        )
    )
}

fun NetworkMovieById.asMovieDetailEntity(): MovieDetailEntity {
    return MovieDetailEntity(
            id = id.toString(),
            image = image.toString(),
            title = title.toString(),
            year = year.toString(),
            runtimeMins = runtimeMins?:0,
            plot = plot.toString(),
            directors = directors.toString(),
            genres = genres.toString(),
            imDbRating = imDbRating?: 0.0,
            imDbRatingVotes = imDbRatingVotes?:0
        )

}

fun SearchMovieResponse.asList(): List<Movie>{
    return results.map {
        Movie(
            id = it.id,
            title = it.title,
            image = it.image,
        )
    }
}