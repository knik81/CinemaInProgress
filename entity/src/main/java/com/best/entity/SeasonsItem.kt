package com.best.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SeasonsItem(
    override val number: Int?,
    override val episodes: List<Episode>
) : SeasonsItemInterface, Parcelable
