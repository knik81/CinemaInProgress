package com.best.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Country(
    val countryName: String = "Country",
    val id: String = "",
    val country: String = "Country"
): Parcelable
