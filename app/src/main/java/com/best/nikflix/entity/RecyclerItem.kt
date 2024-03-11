package com.best.nikflix.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class RecyclerItem(
    val name: String,
    val iconUri: String,
    val genre: String,
    val rating: Double? = null,
    val id: Int,
    var alreadySaw: Boolean? = false
): Parcelable