package com.example.movieproject.data.local.localdatasource

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class MovieDetailEntity (
    @PrimaryKey val id: String,
    @NonNull @ColumnInfo(name = "image") val image: String,
    @NonNull @ColumnInfo(name = "title") val title: String,
    @NonNull @ColumnInfo(name = "year") val year: String,
    @NonNull @ColumnInfo(name = "runtime") val runtimeMins: Int,
    @NonNull @ColumnInfo(name = "plot")val plot: String,
    @NonNull @ColumnInfo(name = "directors")val directors: String,
    @NonNull @ColumnInfo(name = "genres")val genres: String,
    @NonNull @ColumnInfo(name = "imDbRating")val imDbRating: String,
    @NonNull @ColumnInfo(name = "imDbRatingVotes")val imDbRatingVotes: Int,
)