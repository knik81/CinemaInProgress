package com.best.data

import com.best.data.room.DAOInterface
import com.best.entity.FilmAndCollectionF
import com.best.entity.RepositoryROOMInterface
import com.best.entity.SearchText
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class RepositoryROOM @Inject constructor(
    private val dao: DAOInterface
) : RepositoryROOMInterface {

   // val dao = DAOInterface

    override fun getAllFilmAndCollectionF() = dao.getAllFilmAndCollectionF()

    override fun insertFilmAndCollectionF(filmAndCollectionF: FilmAndCollectionF) {
        dao.insertFilmAndCollectionF(filmAndCollectionF = filmAndCollectionF)
    }

    override fun getFilmAndCollectionFByCollection(collection: String) =
        dao.getFilmAndCollectionFByCollection(collection)

    override fun getFilmAndCollectionFByFilmId(filmId: String) =
        dao.getFilmAndCollectionFByFilmId(filmId)


    override fun deleteFilmAndCollectionF(filmAndCollectionFList: List<FilmAndCollectionF>) {
        dao.deleteFilmAndCollectionF(filmAndCollectionFList)
    }

    override fun existFilmAndCollectionF(filmAndCollectionF: FilmAndCollectionF): Boolean {
        return !dao.checkFilmAndCollectionF(
            filmId = filmAndCollectionF.filmId,
            collection = filmAndCollectionF.collection
        ).isNullOrEmpty()
    }

    override fun getSearchText() = flow {
        val textList = mutableListOf<String>()
        dao.getSearchText().collect { searchTextList ->
            searchTextList?.forEach { searchText ->
                textList.add(searchText.text)
            }
            emit(textList)
        }
    }

    override fun insertSearchText(searchText: SearchText) = dao.insertSearchText(searchText)

    override fun deleteSearchText(searchText: SearchText) = dao.deleteSearchText(searchText)

    override fun isExistSearchTextByText(text: String): Boolean {
        return dao.getSearchTextByText(text) != null
    }


}