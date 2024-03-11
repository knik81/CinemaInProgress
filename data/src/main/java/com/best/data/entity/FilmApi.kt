package com.best.data.entity

import com.best.entity.Country
import com.best.entity.FilmApiInterface
import com.best.entity.Genre


data class FilmApi(
    override val kinopoiskId: Int,
    override val kinopoiskHDId: String,
    override val imdbId: String,
    override val nameRu: String,
    override val nameEn: String?,
    override val nameOriginal: String,
    override val posterUrl: String,
    override val posterUrlPreview: String,
    override val coverUrl: String,
    override val logoUrl: String,
    override val reviewsCount: Int,
    override val ratingGoodReview: Double,
    override val ratingGoodReviewVoteCount: Int,
    override val ratingKinopoisk: Double?,
    override val ratingKinopoiskVoteCount: Int,
    override val ratingImdb: Double?,
    override val ratingImdbVoteCount: Int,
    override val ratingFilmCritics: Double,
    override val ratingFilmCriticsVoteCount: Int,
    override val ratingAwait: String?,
    override val ratingAwaitCount: Int,
    override val ratingRfCritics: String?,
    override val ratingRfCriticsVoteCount: String?,
    override val webUrl: String,
    override val year: Int,
    override val filmLength: Int,
    override val slogan: String,
    override val description: String,
    override val shortDescription: String,
    override val editorAnnotation: String?,
    override val isTicketsAvailable: Boolean,
    override val productionStatus: String?,
    override val type: String,
    override val ratingMpaa: String,
    override val ratingAgeLimits: String,
    override val countries: List<Country>,
    override val genres: List<Genre>,
    override val startYear: String?,
    override val endYear: String?,
    override val serial: Boolean,
    override val shortFilm: Boolean,
    override val completed: Boolean,
    override val hasImax: Boolean,
    override val has3D: Boolean,
    override val lastSync: String
): FilmApiInterface
