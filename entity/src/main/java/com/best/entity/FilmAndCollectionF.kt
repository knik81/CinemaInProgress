package com.best.entity

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(tableName = "FilmAndCollectionF", primaryKeys = ["filmId", "collection"])
data class FilmAndCollectionF(
    @ColumnInfo(name = "filmId")
    override val filmId: String,

    @ColumnInfo(name = "collection")
    override val collection: String
):FilmAndCollectionInterface
