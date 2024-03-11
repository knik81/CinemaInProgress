package com.best.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Filters(
    val genres: List<Genre>,
    val countries: List<Country>
): Parcelable


