package com.best.entity

import kotlinx.coroutines.flow.Flow

interface RepositoryROOMInterface {

    fun getAllFilmAndCollectionF() : Flow<List<FilmAndCollectionF>?>
    fun insertFilmAndCollectionF(filmAndCollectionF: FilmAndCollectionF)
    fun getFilmAndCollectionFByCollection(collection: String): Flow<List<FilmAndCollectionF>?>
    fun getFilmAndCollectionFByFilmId(filmId: String): Flow<List<FilmAndCollectionF>?>
    fun deleteFilmAndCollectionF(filmAndCollectionFList: List<FilmAndCollectionF>)
    fun existFilmAndCollectionF(filmAndCollectionF: FilmAndCollectionF): Boolean

    fun getSearchText(): Flow<List<String>>
    fun insertSearchText(searchText: SearchText)
    fun deleteSearchText(searchText: SearchText)
    fun isExistSearchTextByText(text: String): Boolean
}