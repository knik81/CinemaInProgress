package com.best.entity

interface QueryParamsInterface {
    val genres: Genre
    val countries: Country
    val order: String
    val type: String
    val ratingFrom: Int
    val ratingTo: Int
    val yearFrom: Int
    val yearTo: Int
    val imdbId: String
    val keyword: String
}