package com.example.data

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.example.data.api.RetrofitApi
import com.example.data.api.paging.PagingSourceUniversal
import com.example.data.entity.Images
import com.example.data.entity.ImagesItem
import com.example.data.entity.Staffs
import com.example.entity.ApiParameters
import com.example.entity.Filters
import com.example.entity.ItemApiUniversalInterface
import com.example.entity.RepositoryAPIInterface
import com.example.entity.ResultFromApi
import java.time.LocalDateTime
import javax.inject.Inject


class RepositoryAPI @Inject constructor(
) : RepositoryAPIInterface {

    val pagingSourceUniversal = PagingSourceUniversal()

    //получить данные с пагинацией для film_list
    override suspend fun getPagingData(
        type: String,
        filters: Filters?,
        idFilm: String?,
        imageType: String?
    ): Pager<Int, ItemApiUniversalInterface> {
        pagingSourceUniversal.type = type
        pagingSourceUniversal.filters = filters
        pagingSourceUniversal.id = idFilm ?: ""
        pagingSourceUniversal.imageType = imageType ?: "STILL"
        val tt = Pager(
            config = PagingConfig(pageSize = 20),
            pagingSourceFactory = {
                pagingSourceUniversal
            })
        return tt
    }

    //получить данные из api для без пагинации
    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun getDataApi(
        type: String,
        filters: Filters?,
        id: String?
    ): ResultFromApi {
        //Log.d("Nik", "type = $type")
        return try {
            when (type) {
                //популярные и ТОП250 - обращение к апи
                ApiParameters.POPULAR.type, ApiParameters.TOP250.type ->
                    ResultFromApi.Success(RetrofitApi.api.getCollections(type, 1))

                //премьеры - обращение к апи
                ApiParameters.PREMIERS.type -> {
                    val year = LocalDateTime.now().year.toString()
                    val month = LocalDateTime.now().month.toString()
                    ResultFromApi.Success(RetrofitApi.api.getPremiers(year, month))
                }

                //Сериалы - обращение к апи
                ApiParameters.SERIES.type -> {
                    ResultFromApi.Success(
                        RetrofitApi.api.getFilms(
                            type = ApiParameters.SERIES.type
                        )
                    )
                }

                //Страны и жанры - обращение к апи
                ApiParameters.FILTERS.type ->
                    ResultFromApi.Success(RetrofitApi.api.getFilters())

                //Cдучайный фильм - обращение к апи
                ApiParameters.RANDOM_FILMS.type -> {
                    if (filters != null) {
                        val coutry = filters.countries[0]?.id
                        val genre = filters.genres[0]?.id
                        ResultFromApi.Success(
                            RetrofitApi.api.getFilms(
                                countries = coutry ?: 1,
                                genres = genre ?: 1
                            )
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
                    val items = mutableListOf <ImagesItem>()

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

                    ResultFromApi.Success(images)
                }

                //получить похожие фильмы из апи
                ApiParameters.SIMILARS.type -> {
                    if (id == null || id == "")
                        throw Exception("RepositoryAPI.getDataApi. Нет id фильма для ${ApiParameters.SIMILARS.label}")
                    ResultFromApi.Success(RetrofitApi.api.getSimilars(id))

                }

                //получить данные персонала из апи для фильма
                ApiParameters.STAFF.type -> {
                    if (id == null || id == "")
                        throw Exception("RepositoryAPI.getDataApi. Нет id фильма для ${ApiParameters.STAFF.label} и ${ApiParameters.ACTOR.label}")

                    ResultFromApi.Success(
                        Staffs(RetrofitApi.api.getStaff(id, 1))
                    )
                }

                //получить данные из апи одного фильма
                ApiParameters.FILM.type -> {
                    if (id == null || id == "")
                        throw Exception("RepositoryAPI.getDataApi. Нет id фильма для ${ApiParameters.FILM.label}")
                    ResultFromApi.Success(
                        RetrofitApi.api.getFilm(id)
                    )
                }
                //получить даыыне персоны
                ApiParameters.PERSON.type -> {
                    if (id == null || id == "")
                        throw Exception("RepositoryAPI.getDataApi. Нет id для ${ApiParameters.PERSON.label}")

                    //Log.d("Nik", tt.films.toString())
                    ResultFromApi.Success(
                        RetrofitApi.api.getPerson(id)
                    )
                }

                //не найден тип
                else -> {
                    throw Exception("RepositoryAPI.getDataApi ошибка типа")
                }
            }
            //обработка ошибок из апи
        } catch (e: Exception) {
            ResultFromApi.Error(
                "RepositoryAPI: ${
                    ApiParameters.getFilmTypesByType(
                        type
                    ).label
                } - ошибка: $e"
            )
        }
    }

}