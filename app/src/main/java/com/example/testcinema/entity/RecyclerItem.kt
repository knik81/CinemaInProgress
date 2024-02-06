package com.example.testcinema.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class RecyclerItem(
    val name: String,
    val iconUri: String,
    val genre: String,
    val rating: Double? = null,
    val id: Int
): Parcelable