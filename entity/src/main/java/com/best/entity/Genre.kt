package com.best.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Genre(
    val genre: String = "Genre",
    val id: String = ""
) : Parcelable {
}