package com.example.movieproject.data.local.localdatasource

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.movieproject.data.local.model.Movie

//Movie Entity
@Entity
data class MovieEntity (
    @PrimaryKey val id: String,
    @NonNull @ColumnInfo(name = "title") val title: String,
    @NonNull @ColumnInfo(name = "year") val year: Int,
    @NonNull @ColumnInfo(name = "image") val image: String,
    @NonNull @ColumnInfo(name = "imDbRating") val imDbRating: String,
    @NonNull @ColumnInfo(name = "imDbRatingCount") val imDbRatingCount: String,
)

//Mengubah Database menjadi model
fun List<MovieEntity>.asDomainModel(): List<Movie> {
    return map {
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