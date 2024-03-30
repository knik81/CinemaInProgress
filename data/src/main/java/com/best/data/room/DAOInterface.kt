package com.best.data.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.best.entity.FilmAndCollectionF
import com.best.entity.SearchText
import kotlinx.coroutines.flow.Flow

@Dao
interface DAOInterface {

    @Query("SELECT * FROM FilmAndCollectionF")
    fun getAllFilmAndCollectionF(): Flow<List<FilmAndCollectionF>?>

    @Query("SELECT * FROM FilmAndCollectionF WHERE collection = :collection")
    fun getFilmAndCollectionFByCollection(collection: String): Flow<List<FilmAndCollectionF>?>

    @Query("SELECT * FROM FilmAndCollectionF WHERE filmId = :filmId")
    fun getFilmAndCollectionFByFilmId(filmId: String): Flow<List<FilmAndCollectionF>?>

    @Query("SELECT * FROM FilmAndCollectionF WHERE filmId = :filmId AND collection = :collection")
    fun checkFilmAndCollectionF(filmId: String, collection: String): List<FilmAndCollectionF>?

    @Insert
    fun insertFilmAndCollectionF(filmAndCollectionF: FilmAndCollectionF)

    @Delete
    fun deleteFilmAndCollectionF(filmAndCollectionFList: List<FilmAndCollectionF>)

    @Insert
    fun insertSearchText(searchText: SearchText)

    @Delete
    fun deleteSearchText(searchText: SearchText)

    @Query("SELECT * FROM SearchText")
    fun getSearchText(): Flow<List<SearchText>?>

    @Query("SELECT * FROM SearchText WHERE text = :text")
    fun getSearchTextByText(text: String): SearchText?

    /*
    @Query("SELECT * FROM CollectionF")
    fun getAllCollections(): Flow<List<CollectionF>?>

    @Query("SELECT * FROM CollectionF WHERE collection = :collection")
    fun getCollection(collection: String): Flow<List<CollectionF>?>

    @Insert
    fun insertCollectionF(collectionF: CollectionF)

    @Delete
    fun deleteCollectionF(collectionF: CollectionF)
     */


}