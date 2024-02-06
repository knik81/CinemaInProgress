package com.example.data.api.paging

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.data.api.RetrofitApi
import com.example.data.entity.ItemApiUniversal
import com.example.entity.ApiParameters
import com.example.entity.Filters
import com.example.entity.ItemApiUniversalInterface
import kotlinx.coroutines.delay
import java.time.LocalDateTime

class PagingSourceUniversal(
) : PagingSource<Int, ItemApiUniversalInterface>() {

    var type: String = ""
    var filters: Filters? = null
    var id: String = ""
    var imageType: String = "STILL"

    override fun getRefreshKey(state: PagingState<Int, ItemApiUniversalInterface>): Int? {
        return 1
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ItemApiUniversalInterface> {
        val page = params.key ?: 1
        val result: List<ItemApiUniversal>

        try {
            return when (type) {
                //популярные фильмы
                ApiParameters.POPULAR.type -> {
                    result =
                        RetrofitApi.api.getCollections(
                            ApiParameters.POPULAR.type,
                            page
                        ).items
                    getPageFormat(result, page)
                }

                //ТОП 250
                ApiParameters.TOP250.type -> {
                    result =
                        RetrofitApi.api.getCollections(ApiParameters.TOP250.type, page)
                            .items
                    getPageFormat(result, page)
                }

                //премьеры
                ApiParameters.PREMIERS.type -> {
                    val year = LocalDateTime.now().year.toString()
                    val month = LocalDateTime.now().month.toString()
                    result =
                        RetrofitApi.api.getPremiers(year, month)
                            .items
                    getPageFormat(result, page)
                }

                //Сериалы
                ApiParameters.SERIES.type -> {
                    result =
                        RetrofitApi.api.getFilms(type = ApiParameters.SERIES.type, page = page)
                            .items
                    getPageFormat(result, page)
                }

                //Случайный фильм
                ApiParameters.RANDOM_FILMS.type -> {
                    if (filters != null) {
                        val country = filters!!.countries[0]?.id
                        val genre = filters!!.genres[0]?.id
                        result =
                            RetrofitApi.api.getFilms(
                                countries = country ?: 1,
                                genres = genre ?: 1,
                                page = page

                            ).items
                        getPageFormat(result, page)
                    } else
                        throw Exception("RANDOM_FILMS - Нет страны/жанра")
                }

                //Картинки
                ApiParameters.IMAGES.type -> {
                    val temp = mutableListOf<ItemApiUniversal?>()
                    Log.d("Nik",imageType)
                    Log.d("Nik",page.toString())
                    RetrofitApi.api.getImages(id = id, page = page, imageType).items.forEach {
                        //переложить из Images в ItemApiUniversal
                        temp.add(
                            ItemApiUniversal(posterUrl = it.imageUrl)
                        )
                    }
                    result = temp as List<ItemApiUniversal>
                    getPageFormat(result, page)
                }

                //Весь персонал
                ApiParameters.ACTOR.type, ApiParameters.STAFF.type -> {
                    val temp = mutableListOf<ItemApiUniversal?>()
                    if (page == 1) {
                        val staff = RetrofitApi.api.getStaff(filmId = id, page = page)
                        staff.forEach {
                            if (type == ApiParameters.ACTOR.type && it.professionKey == ApiParameters.ACTOR.type) {
                                //переложить из StaffsItem в ItemApiUniversal
                                temp.add(
                                    ItemApiUniversal(
                                        filmId = it.staffId,
                                        staffId = it.staffId,
                                        kinopoiskId = it.staffId,
                                        nameRu = it.nameRu,
                                        nameEn = it.nameEn,
                                        posterUrl = it.posterUrl,
                                        professionKey = it.professionKey
                                    )
                                )
                            }

                            if (type == ApiParameters.STAFF.type && it.professionKey != ApiParameters.ACTOR.type) {
                                //переложить из StaffsItem в ItemApiUniversal
                                temp.add(
                                    ItemApiUniversal(
                                        filmId = it.staffId,
                                        staffId = it.staffId,
                                        kinopoiskId = it.staffId,
                                        nameRu = it.nameRu,
                                        nameEn = it.nameEn,
                                        posterUrl = it.posterUrl
                                    )
                                )
                            }
                        }
                    }
                    result = temp as List<ItemApiUniversal>
                    getPageFormat(result, page)
                }

                //Фильмы персонала
                ApiParameters.PERSON_FILMS.type -> {
                    val itemApiUniversalList = mutableListOf<ItemApiUniversalInterface>()

                    //получить вильмы персоны и удалю дубли и сортирну по рейтингу
                    val films = RetrofitApi.api.getPerson(idPerson = id).films
                        .distinctBy { it.filmId }
                        .sortedByDescending { it.rating }

                    //ниже полусамодельная пагинация.
                    //Чтобы не грузить все фильмы разом, а помере прокрутки
                    val max = films.size
                    if ((page - 1) * 3 > max)
                        getPageFormat(listOf(), page)
                    else
                        for (i in (page - 1) * 3..page * 3 - 1)
                            if (i < max) {
                                //Log.d("Nik", "i $i")
                                //delay(500)

                                //обратится в апи по i-му фильму персоны
                                val film = RetrofitApi.api.getFilm(films[i].filmId.toString())

                                var ratingKinopoisk: String? = null
                                if (film.ratingKinopoisk != null)
                                    ratingKinopoisk = film.ratingKinopoisk.toString()

                                //переложить в itemApiUniversalList
                                itemApiUniversalList.add(
                                    ItemApiUniversal(
                                        kinopoiskId = film.kinopoiskId,
                                        filmId = film.kinopoiskId,
                                        nameRu = film.nameRu,
                                        nameEn = film.nameEn,
                                        year = film.year.toString(),
                                        posterUrl = film.posterUrl,
                                        posterUrlPreview = film.posterUrlPreview,
                                        countries = film.countries,
                                        genres = film.genres,
                                        //premiereRu = null,
                                        rating = ratingKinopoisk,
                                        imdbID = film.imdbId,
                                        nameOriginal = film.nameOriginal,
                                        ratingKinopoisk = film.ratingKinopoisk,
                                        //ratingImdb = ratingImdb,
                                        type = null,
                                        professionKey = null,
                                    )
                                )
                            }
                    getPageFormat(itemApiUniversalList, page)
                }

                else -> {
                    //исключение, если не найден тип
                    throw Exception("PagingSourceFilmUniversal: Не определен тип")
                }
            }
        } catch (e: Exception) {
            //перехват исключения
            return LoadResult.Error(e)
        }
    }

    //перевод данных в LoadResult.Page
    private fun getPageFormat(
        result: List<ItemApiUniversalInterface>,
        page: Int
    ): LoadResult<Int, ItemApiUniversalInterface> {
        return LoadResult.Page(
            data = result,
            prevKey = null,
            nextKey = if (result.isEmpty()) null else page + 1
        )
    }
}



