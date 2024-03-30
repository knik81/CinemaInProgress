package com.best.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "SearchText")
data class SearchText(

    @PrimaryKey
    @ColumnInfo(name = "text")
    val text: String,
)
