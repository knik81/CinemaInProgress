package com.example.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Genre(
    val genre: String,
    val id: Int?
) : Parcelable {
}