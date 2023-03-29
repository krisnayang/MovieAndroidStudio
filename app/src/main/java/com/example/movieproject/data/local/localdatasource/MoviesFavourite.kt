package com.example.movieproject.data.local.localdatasource

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.movieproject.data.local.model.MovieLocal

@Entity
data class MoviesFavourite (
    @PrimaryKey val id: String,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "image") val image: String,
)
fun List<MoviesFavourite>.asDomainModel(): List<MovieLocal> {
    return map {
        MovieLocal(
            id = it.id,
            title = it.title,
            image = it.image,
        )
    }
}