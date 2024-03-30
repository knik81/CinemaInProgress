package com.best.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Seasons(
    override val total: Int?,
    override val items: List<SeasonsItem>
): SeasonsInterface, Parcelable
