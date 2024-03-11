package com.best.data

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.best.data.api.RetrofitApi
import com.best.data.api.paging.PagingSourceUniversal
import com.best.data.entity.Images
import com.best.data.entity.ImagesItem
import com.best.data.entity.PremiersSimilarsApi
import com.best.data.entity.Staffs

import com.best.data.room.DAOInterface
import com.best.entity.ApiParameters
import com.best.entity.ItemApiUniversalInterface

import com.best.entity.QueryParams
import com.best.entity.RepositoryAPIInterface
import com.best.entity.ResultFromApi
import com.best.entity.SEARCH
import java.time.LocalDateTime
import javax.inject.Inject


class RepositoryAPI @Inject constructor(
    private val dao: DAOInterface
) : RepositoryAPIInterface {


    val pagingSourceUniversal = PagingSourceUniversal()

    //получить данные с пагинацией для film_list
    override fun getPager(
        type: String,
        queryParams: QueryParams?,
        idFilm: String?,
        imageType: String?
    ): Pager<Int, ItemApiUniversalInterface> {
        pagingSourceUniversal.type = type
        pagingSourceUniversal.queryParams = queryParams
        pagingSourceUniversal.id = idFilm ?: ""
        pagingSourceUniversal.imageType = imageType ?: "STILL"
        pagingSourceUniversal.dao = dao

        return Pager(
            config = PagingConfig(pageSize = 20),
            pagingSourceFactory = {
                pagingSourceUniversal
            })
    }


    //получить данные из api для без пагинации
    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun getDataApi(
        type: String,
        queryParams: QueryParams?,
        id: String?
    ): ResultFromApi {


        //Log.d("Nik", "type = $type")
        return try {
            ResultFromApi.Success(
                when (type) {
                    //популярные и ТОП250 - обращение к апи
                    ApiParameters.POPULAR.type, ApiParameters.TOP250.type ->
                        RetrofitApi.api.getCollections(type, 1)

                    //премьеры - обращение к апи
                    ApiParameters.PREMIERS.type -> {
                        val year = LocalDateTime.now().year.toString()
                        val month = LocalDateTime.now().month.toString()
                        val test: PremiersSimilarsApi = RetrofitApi.api.getPremiers(year, month)
                        //Log.d("Nik", "ResultFromApi = ${RetrofitApi.api.getPremiers(year, month)}")
                        RetrofitApi.api.getPremiers(year, month)
                    }

                    //Сериалы - обращение к апи
                    ApiParameters.SERIES.type -> {
                        RetrofitApi.api.getFilms(
                            type = ApiParameters.SERIES.type,
                            order = SEARCH.ORDER.RATING.name,
                        )
                    }

                    //Сезоны для сериала - обращение к апи
                    ApiParameters.SEASONS.type -> {
                        RetrofitApi.api.getSeasons(ifFilm = id ?: "")
                    }

                    //Страны и жанры - обращение к апи
                    ApiParameters.FILTERS.type ->
                        RetrofitApi.api.getFilters()

                    //Cдучайный фильм - обращение к апи
                    ApiParameters.RANDOM_FILMS.type -> {
                        if (queryParams != null) {
                            RetrofitApi.api.getFilms(
                                countries = queryParams.countries.id,
                                genres = queryParams.genres.id,
                                order = SEARCH.ORDER.RATING.name,
                                type = SEARCH.TYPE.ALL.name
                            )
                        } else
                            throw Exception("RepositoryAPI.getDataApi ошибка. Нет страны и жанра для ${ApiParameters.RANDOM_FILMS.label}")
                    }

                    //получить картинки фильма из апи
                    ApiParameters.IMAGES.type -> {
                        if (id == null || id == "")
                            throw Exception("RepositoryAPI.getDataApi. Нет id фильма для ${ApiParameters.IMAGES.label}")

                        val types = ApiParameters.getImagesTypeParameters().keys

                        var total = 0
                        val items = mutableListOf<ImagesItem>()

                        //обращение к апи по всем типам картинок
                        types.forEach { it ->
                            val images = RetrofitApi.api.getImages(id, 1, it)
                            total += images.total
                            images.items.forEach {
                                items.add(it)
                            }
                        }
                        //создание экземпляра со всеми типами картинок
                        val images = Images(
                            total = total,
                            totalPages = 0,
                            items = items
                        )
                        images
                    }

                    //получить похожие фильмы из апи
                    ApiParameters.SIMILARS.type -> {
                        if (id == null || id == "")
                            throw Exception("RepositoryAPI.getDataApi. Нет id фильма для ${ApiParameters.SIMILARS.label}")
                        RetrofitApi.api.getSimilars(id)
                    }

                    //получить данные персонала из апи для фильма
                    ApiParameters.STAFF.type -> {
                        if (id == null || id == "")
                            throw Exception("RepositoryAPI.getDataApi. Нет id фильма для ${ApiParameters.STAFF.label} и ${ApiParameters.ACTOR.label}")

                        Staffs(RetrofitApi.api.getStaff(id, 1))
                    }

                    //получить данные из апи одного фильма
                    ApiParameters.FILM.type -> {
                        if (id == null || id == "")
                            throw Exception("RepositoryAPI.getDataApi. Нет id фильма для ${ApiParameters.FILM.label}")

                        RetrofitApi.api.getFilm(id)
                    }
                    //получить даыыне персоны
                    ApiParameters.PERSON.type -> {
                        if (id == null || id == "")
                            throw Exception("RepositoryAPI.getDataApi. Нет id для ${ApiParameters.PERSON.label}")
                        //Log.d("Nik", tt.films.toString())
                        RetrofitApi.api.getPerson(id)
                    }

                    //не найден тип
                    else -> {
                        throw Exception("RepositoryAPI.getDataApi ошибка типа")
                    }
                }
            )
            //обработка ошибок из апи
        } catch (e: Exception) {
            ResultFromApi.Error(
                "RepositoryAPI: ${
                    ApiParameters.getFilmTypesByType(
                        type
                    ).label
                } - ошибка: $e"
            )
            //FireBase
        }
    }

}