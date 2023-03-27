package com.example.movieproject.data.local.localdatasource

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.PrimaryKey
import com.example.movieproject.data.local.model.Actors
import com.example.movieproject.data.local.model.FullCast
import com.example.movieproject.data.local.model.Movie
import com.example.movieproject.ui.state.UiState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

@Entity
data class FullCastEntity (
    @PrimaryKey val id: String,
    @ColumnInfo(name = "movieId") val movieId: String,
    @ColumnInfo(name = "image") val image: String,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "asCharacter") val asCharacter: String,
)