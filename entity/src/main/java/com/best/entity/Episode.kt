package com.best.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Episode(
    override val seasonNumber: Int?,
    override val episodeNumber: Int?,
    override val nameRu: String?,
    override val nameEn: String?,
    override val synopsis: String?,
    override val releaseDate: String?
):EpisodeInterface, Parcelable
