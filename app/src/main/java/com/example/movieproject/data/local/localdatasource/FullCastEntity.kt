package com.example.movieproject.data.local.localdatasource

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class FullCastEntity (
    @PrimaryKey val id: String,
    @ColumnInfo(name = "movieId") val movieId: String,
    @ColumnInfo(name = "image") val image: String,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "asCharacter") val asCharacter: String,
)