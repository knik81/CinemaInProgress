package com.example.data.api

import com.example.data.entity.FilmApi
import com.example.data.entity.PremiersSimilarsApi
import com.example.data.entity.FilmsApiUniversal
import com.example.data.entity.Images
import com.example.data.entity.Person
import com.example.data.entity.StaffsItem
import com.example.entity.Filters
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query


//mail
const val HEADERS = "X-API-KEY: d01a4ab7-b4c9-4974-91e4-eb8f998a1db1"

interface ApiInterface {
    @Headers(HEADERS)
    @GET("/api/v2.2/films/premieres")
    suspend fun getPremiers(
        @Query("year") year: String,
        @Query("month") month: String
    ): PremiersSimilarsApi

    @Headers(HEADERS)
    @GET("/api/v2.2/films/collections")
    suspend fun getCollections(
        @Query("type") type: String,
        @Query("page") page: Int
    ): FilmsApiUniversal

    @Headers(HEADERS)
    @GET("/api/v2.2/films")
    suspend fun getFilms(
        @Query("countries") countries: Int = 1,
        @Query("genres") genres: Int = 1,
        @Query("order") sort: String = "RATING",
        @Query("type") type: String = "ALL",
        @Query("ratingFrom") ratingFrom: Int = 0,
        @Query("ratingTo") ratingTo: Int = 100,
        @Query("yearFrom") yearFrom: Int = 1000,
        @Query("yearTo") yearTo: Int = 3000,
        @Query("page") page: Int = 1
    ): FilmsApiUniversal

    @Headers(HEADERS)
    @GET("/api/v2.2/films/filters")
    suspend fun getFilters(): Filters

    @Headers(HEADERS)
    @GET("/api/v2.2/films/{id}")
    suspend fun getFilm(@Path("id") id: String): FilmApi

    @Headers(HEADERS)
    @GET("/api/v2.2/films/{id}/images")
    suspend fun getImages(
        @Path("id") id: String,
        @Query("page") page: Int,
        @Query("type") type: String = "STILL"
    ): Images

    @Headers(HEADERS)
    @GET("/api/v2.2/films/{id}/similars")
    suspend fun getSimilars(@Path("id") id: String): PremiersSimilarsApi

    @Headers(HEADERS)
    @GET("/api/v1/staff")
    suspend fun getStaff(
        @Query("filmId") filmId: String,
        @Query("page") page: Int
    ): List<StaffsItem>

    @Headers(HEADERS)
    @GET("/api/v1/staff/{idPerson}")
    suspend fun getPerson(@Path("idPerson") idPerson: String): Person

}