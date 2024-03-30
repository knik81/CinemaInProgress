package com.best.domain

import com.best.entity.FilmAndCollectionF
import com.best.entity.RepositoryROOMInterface
import com.best.entity.RoomUseCaseInterface
import com.best.entity.SearchText
import javax.inject.Inject

class RoomUseCase @Inject constructor(
    private val repositoryROOM: RepositoryROOMInterface
) : RoomUseCaseInterface {


    override fun getAllFilmAndCollectionF() =
        repositoryROOM.getAllFilmAndCollectionF()


    override fun insertFilmAndCollectionF(filmAndCollectionF: FilmAndCollectionF) =
        repositoryROOM.insertFilmAndCollectionF(filmAndCollectionF = filmAndCollectionF)

    override fun getFilmAndCollectionFByCollection(collection: String) =
        repositoryROOM.getFilmAndCollectionFByCollection(collection = collection)

    override fun getFilmAndCollectionFByFilmId(filmId: String) =
        repositoryROOM.getFilmAndCollectionFByFilmId(filmId = filmId)

    override fun deleteFilmAndCollectionF(filmAndCollectionFList: List<FilmAndCollectionF>) {
        repositoryROOM.deleteFilmAndCollectionF(filmAndCollectionFList)
    }

    override fun existFilmAndCollectionF(filmAndCollectionF: FilmAndCollectionF) =
        repositoryROOM.existFilmAndCollectionF(filmAndCollectionF = filmAndCollectionF)

    override fun getSearchText() = repositoryROOM.getSearchText()

    override fun insertSearchText(searchText: SearchText) = repositoryROOM.insertSearchText(searchText)

    override fun deleteSearchText(searchText: SearchText) = repositoryROOM.deleteSearchText(searchText)

    override fun isExistSearchTextByText(text: String) = repositoryROOM.isExistSearchTextByText(text)

}
