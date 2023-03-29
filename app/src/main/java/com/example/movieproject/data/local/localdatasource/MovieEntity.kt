package com.example.movieproject.data.local.localdatasource

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.movieproject.data.local.model.MovieLocal

//Movie Entity
@Entity
data class MovieEntity (
    @PrimaryKey val id: String,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "image") val image: String,
)

fun List<MovieEntity>.asDomainModel(): List<MovieLocal> {
    return map {
        MovieLocal(
            id = it.id,
            title = it.title,
            image = it.image,
        )
    }
}