package com.example.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Country(
    val countryName: String,
    val id: Int?,
    val country: String?
): Parcelable
