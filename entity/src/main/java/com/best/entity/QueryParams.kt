package com.best.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class QueryParams(
    override val genres: Genre = Genre(),
    override val countries: Country = Country(),
    override val order: String = SEARCH.ORDER.RATING.name,
    override val type: String = SEARCH.TYPE.ALL.name,
    override val ratingFrom: Int = 0,
    override val ratingTo: Int = 10,
    override val yearFrom: Int = 1000,
    override val yearTo: Int = 3000,
    override val imdbId: String = "",
    override val keyword: String = ""
): Parcelable,QueryParamsInterface
