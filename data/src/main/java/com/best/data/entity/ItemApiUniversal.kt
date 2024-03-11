package com.best.data.entity

import android.os.Parcelable
import com.best.entity.ItemApiUniversalInterface
import kotlinx.parcelize.Parcelize


@Parcelize
data class ItemApiUniversal(
    override val kinopoiskId: Int? = null,
    override val filmId: Int? = null,
    override val staffId: Int? = null,

    override val nameRu: String? = null,
    override val nameEn: String? = null,
    override val year: String? = null,
    override val posterUrl: String? = null,
    override val posterUrlPreview: String? = null,
    override val countries: List<com.best.entity.Country>? = null,
    override val genres: List<com.best.entity.Genre>? = null,
   // override val premiereRu: String? = null,
    override val rating: String? = null,
    override val imdbID: String? = null,
    override val nameOriginal: String? = null,
    override val ratingKinopoisk: Double? = null,
   // override val ratingImdb: String? = null,
    override val type: String? = null,
    override val professionKey: String? = null,
    override var alreadySaw: Boolean = false,


    //override val professionKeyNecessary: String? = null
) : ItemApiUniversalInterface, Parcelable
