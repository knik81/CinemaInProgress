package com.example.data.entity

import com.example.entity.ItemApiUniversalInterface


data class ItemApiUniversal(
    override val kinopoiskId: Int? = null,
    override val filmId: Int? = null,
    override val staffId: Int? = null,

    override val nameRu: String? = null,
    override val nameEn: String? = null,
    override val year: String? = null,
    override val posterUrl: String? = null,
    override val posterUrlPreview: String? = null,
    override val countries: List<com.example.entity.Country>? = null,
    override val genres: List<com.example.entity.Genre>? = null,
   // override val premiereRu: String? = null,
    override val rating: String? = null,
    override val imdbID: String? = null,
    override val nameOriginal: String? = null,
    override val ratingKinopoisk: Double? = null,
   // override val ratingImdb: String? = null,
    override val type: String? = null,
    override val professionKey: String? = null,

    //override val professionKeyNecessary: String? = null
) : ItemApiUniversalInterface
