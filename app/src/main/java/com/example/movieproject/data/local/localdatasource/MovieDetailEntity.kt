package com.example.movieproject.data.local.localdatasource

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class MovieDetailEntity (
    @PrimaryKey var id: String = "",
    @ColumnInfo(name = "image") var image: String = "",
    @ColumnInfo(name = "title") var title: String = "",
    @ColumnInfo(name = "year") var year: String = "",
    @ColumnInfo(name = "runtime") var runtimeMins: Int? = 0,
    @ColumnInfo(name = "plot")var plot: String = "",
    @ColumnInfo(name = "directors")var directors: String = "",
    @ColumnInfo(name = "genres")var genres: String = "",
    @ColumnInfo(name = "imDbRating")var imDbRating: Double? = 0.0,
    @ColumnInfo(name = "imDbRatingVotes")var imDbRatingVotes: Int? = 0,
)