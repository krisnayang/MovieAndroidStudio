package com.example.movieproject.data.local.localdatasource

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.movieproject.data.local.model.MovieLocal

@Entity
data class MoviesFavourite (
    @PrimaryKey var id: String = "",
    @ColumnInfo(name = "title") var title: String = "",
    @ColumnInfo(name = "image") var image: String = "",
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