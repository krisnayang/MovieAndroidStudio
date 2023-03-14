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
    @NonNull @ColumnInfo(name = "image") val image: String,
)

//Mengubah Database menjadi model
fun List<MovieEntity>.asDomainModel(): List<Movie> {
    return map {
        Movie(
            id = it.id,
            title = it.title,
            image = it.image,
        )
    }
}