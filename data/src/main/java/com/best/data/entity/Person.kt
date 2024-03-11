package com.best.data.entity

import android.os.Parcelable
import com.best.entity.PersonInterface
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Person(
    override val personId: Int,
    override val webUrl: String,
    override val nameRu: String,
    override val nameEn: String,
    override val posterUrl: String,
    override val profession: String,
    override val films: List<ItemApiUniversal>
):PersonInterface,  Parcelable
