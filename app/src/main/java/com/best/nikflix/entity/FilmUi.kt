package com.best.nikflix.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class FilmUi(
    val ratingName: String,
    val yearGenre: String,
    val countryLength: String,
    val posterUrl: String,
    val description: String,
    val shortDescription: String,
    val description250: String,
    val type: String,
    val filmName: String,
    val webUrl: String,
    var alreadySaw: Boolean = false
) : Parcelable
