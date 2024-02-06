package com.example.entity

import kotlinx.android.parcel.Parcelize


interface ItemApiUniversalInterface {
    val kinopoiskId: Int?
    val filmId: Int?
    val imdbID: String?
    val staffId: Int?

    val nameRu: String?
    val nameEn: String?
    val nameOriginal: String?
    val year: String?
    val posterUrl: String?
    val posterUrlPreview: String?
    val countries: List<Country>?
    val genres: List<Genre>?
   // val premiereRu: String?
    val rating: String?
    val ratingKinopoisk: Double?
   // val ratingImdb: String?
    val type: String?
    val professionKey: String?
    //val professionKeyNecessary: String?
}





