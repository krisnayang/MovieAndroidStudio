package com.example.movieproject.data.remote.remotedatasource

import com.example.movieproject.data.local.localdatasource.FullCastEntity
import com.example.movieproject.data.local.localdatasource.MovieDetailEntity
import com.example.movieproject.data.local.localdatasource.MovieEntity
import com.example.movieproject.data.local.model.MovieLocal


fun MoviesResponse.asDatabaseMovie(): List<MovieEntity> {
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

fun SearchMovieResponse.asList(): List<MovieLocal>{
    return results.map {
        MovieLocal(
            id = it.id,
            title = it.title,
            image = it.image,
        )
    }
}