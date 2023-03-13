package com.example.movieproject.data.local.localdatasource

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.movieproject.data.local.model.Actors
import com.example.movieproject.data.local.model.FullCast
import com.example.movieproject.data.local.model.Movie

@Entity
data class FullCastEntity (
    @PrimaryKey val id: String,
    @NonNull @ColumnInfo(name = "title") val title: String,
    @NonNull @ColumnInfo(name = "time") val runtimeMins: Int,
    @NonNull @ColumnInfo(name = "image") val image: String,
    @NonNull @ColumnInfo(name = "name") val name: String,
    @NonNull @ColumnInfo(name = "asCharacter") val asCharacter: String,
)

//Mengubah Database menjadi model
fun List<FullCastEntity>.asDomainModel(): List<FullCast> {
    return map {
        FullCast(
            id = it.id,
            title = it.title,
            runtimeMins = it.runtimeMins,
            listOf(Actors(it.id, it.image, it.name, it.asCharacter))
        )
    }
}