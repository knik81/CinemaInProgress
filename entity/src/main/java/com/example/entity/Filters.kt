package com.example.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Filters(
    val genres: List<Genre?>,
    val countries: List<Country?>
): Parcelable
