package com.best.nikflix.ui.home.common

import android.util.Log
import com.best.entity.FilmsApiUniversalInterface
import com.best.entity.FilmApiInterface
import com.best.entity.ImagesInterface
import com.best.entity.ImagesItemInterface
import com.best.entity.ItemApiUniversalInterface
import com.best.entity.PremiersSimilarsApiInterface
import com.best.entity.StaffsInterface
import com.best.entity.StaffsItemInterface
import com.best.nikflix.entity.FilmUi
import com.best.nikflix.entity.RecyclerItem

object MapperApiToUi {

    //возвращает список данных для горизонтального рекулера на 20 элементов + элемент с переходом
    fun <T, E> mapperRecyclerHorizontalLimit(successData: T): MutableList<E> {
        return mapperUiUniversal(
            successData,
            true,
            20
        )
    }

    //возвращает список данных для горизонтального рекулера на 20 элементов + элемент с переходом
    fun <T, E> mapperRecyclerHorizontalLimitStaff(
        successData: T,
        professionKeyIsActor: Boolean
    ): MutableList<E> {
        return mapperUiUniversal(
            successData,
            true,
            20,
            professionKeyIsActor
        )
    }


    //возвращает один элемент для вертикального постраничного рекуклера
    fun <T, E> mapperRecyclerVerticalUi(item: T): E? {
        val valueList: MutableList<E> =
            mapperUiUniversal(
                item,
                false,
                9999999
            )
        return if (valueList.isEmpty())
            null
        else valueList[0]
    }


    //определяет вид входящих данных и возвращает готовый список в нужного типа
    private fun <T, E> mapperUiUniversal(
        successData: T,
        extraElement: Boolean,
        maxIndex: Int,
        professionKeyIsActor: Boolean = false
    ): MutableList<E> {
        val itemRecyclerList = mutableListOf<E>()
        var indexItem = 0
        when (successData) {

            is PremiersSimilarsApiInterface -> {
                successData.items.forEachIndexed { index, item ->
                    if (index < maxIndex)
                        itemRecyclerList.add(mapperItem(item) as E)
                    indexItem = index
                }

            }

            is FilmsApiUniversalInterface -> {
                successData.items.forEachIndexed { index, item ->
                    if (index < maxIndex)
                        itemRecyclerList.add(mapperItem(item) as E)
                    indexItem = index
                }
            }

            is ItemApiUniversalInterface -> {
                itemRecyclerList.add(mapperItem(successData) as E)
            }

            is StaffsInterface -> {
                val items = successData.item.distinctBy { it.staffId }
                var index = -1
                items.forEach { item ->
                    if ((mapperItem(item, professionKeyIsActor) as E) != null) {
                        if (index < maxIndex)
                            itemRecyclerList.add(mapperItem(item, professionKeyIsActor) as E)
                        index++
                    }
                    indexItem = index
                }
            }

            is ImagesInterface -> {
                successData.items.forEachIndexed { index, item ->
                    if (index < maxIndex)
                        itemRecyclerList.add(mapperItem(item) as E)
                    indexItem = index
                }
            }

            is List<*> -> {
                successData.forEachIndexed { index, item ->
                    if (item is FilmApiInterface) {
                        if (index < maxIndex) {
                            val temp: RecyclerItem =
                                mapperFilmApiToUI(successData = item, forRecycler = true)
                            itemRecyclerList.add(temp as E)
                        }
                        indexItem = index
                    } else {
                        Log.d(
                            "Nik",
                            "object MapperApiToUi.mapperUiUniversal is List<*>: Неизвестный тип для маппинга $successData"
                        )
                    }
                }


            }

            else -> Log.d(
                "Nik",
                "object MapperApiToUi.mapperUiUniversal: Неизвестный тип для маппинга ${successData}"
            )
        }
        if (extraElement && indexItem + 1 >= maxIndex)
            itemRecyclerList.add(addLastElement() as E)

        return itemRecyclerList
    }

    //маппит данные из АПИ в формат элемента
    fun <I> mapperItem(
        item: I,
        professionKeyIsActor: Boolean = false
    ): RecyclerItem? {

        return when (item) {
            is ItemApiUniversalInterface -> {

                var genres = ""
                item.genres?.forEach { genre ->
                    if (genres != "")
                        genres += ", "
                    genres += genre.genre
                    //отключил для взрослых
                    if (genre.genre == "для взрослых")
                        return null
                }
                //Log.d("Nik","genres $genres")

                val id = item.kinopoiskId ?: item.filmId ?: item.staffId ?: 1


                val name: String =
                    if (item.nameRu != null && item.nameRu != "")
                        item.nameRu ?: ""
                    else
                        if (item.nameEn != null && item.nameEn != "")
                            item.nameEn ?: ""
                        else
                            if (item.nameOriginal != null && item.nameOriginal != "")
                                item.nameOriginal ?: ""
                            else ""


                val rating
                        : Double
                ? =
                    if (item.ratingKinopoisk != null && item.ratingKinopoisk != 0.0)
                        item.ratingKinopoisk
                    else null

                //Log.d("Nik","item $item")
                RecyclerItem(
                    name = name,
                    iconUri = item.posterUrl
                        ?: "https://kinopoiskapiunofficial.tech/images/posters/kp_small/696742.jpg",
                    genre = genres,
                    rating = rating,
                    id = id,
                    alreadySaw = item.alreadySaw
                )
            }

            is StaffsItemInterface -> {
                val name: String =
                    if (item.nameRu != null && item.nameRu != "")
                        item.nameRu ?: ""
                    else
                        if (item.nameEn != null && item.nameEn != "")
                            item.nameEn ?: ""
                        else
                            ""
                if (professionKeyIsActor && item.professionKey == "ACTOR") {
                    RecyclerItem(
                        name = name,
                        iconUri = item.posterUrl,
                        genre = "",
                        rating = null,
                        id = item.staffId
                    )
                } else if (!professionKeyIsActor && item.professionKey != "ACTOR") {
                    RecyclerItem(
                        name = name,
                        iconUri = item.posterUrl,
                        genre = "",
                        rating = null,
                        id = item.staffId
                    )
                } else null


            }

            is ImagesItemInterface -> {
                RecyclerItem(
                    name = "",
                    iconUri = item.imageUrl,
                    genre = "",
                    rating = null,
                    id = 1
                )
            }

            else -> {
                Log.d("Nik", "object MapperApiToUi.mapperItem: Неизвестный тип для маппинга")
                null
            }
        }
    }

    //маппит данные фильма в формат экрана
    fun <T, E> mapperFilmApiToUI(successData: T, forRecycler: Boolean = false): E {
        var film: E? = null
        when (successData) {
            is FilmApiInterface -> {
                val rating: String =
                    if (successData.ratingKinopoisk != null && successData.ratingKinopoisk != 0.0)
                        "${successData.ratingKinopoisk}  "
                    else ""

                val name: String =
                    if (successData.nameRu != null && successData.nameRu != "")
                        successData.nameRu ?: "ошибка 1 в mapperFilmApiToUI"
                    else {
                        if (successData.nameEn != null && successData.nameEn != "")
                            successData.nameEn ?: "ошибка 2 в mapperFilmApiToUI"
                        else
                            successData.nameOriginal ?: "-"
                    }

                //Log.d("Nik","name  ${name}")
                var stop = false
                var genres = ""
                successData.genres.forEach { genre ->
                    if (genres != "")
                        genres += ", "
                    genres += genre.genre

                    //Log.d("Nik", "stop ${genre}")
                    if (genre.genre == "для взрослых")
                        stop = true
                }

                var countries = ""
                successData.countries.forEach { country ->
                    if (countries != "")
                        countries += ", "
                    countries += country.country
                }

                val filmLength = if (successData.filmLength > 0)
                    ",  " + successData.filmLength.toString() + "мин"
                else ""

                var description250 = ""
                if (successData.description != null) {
                    if (successData.description?.length!! > 150)
                        description250 = successData.description?.take(150) + "..."
                    else
                        successData.description?.take(150)
                }


                if (!stop)
                    if (!forRecycler) {
                        //для экрана film_page
                        film = FilmUi(
                            ratingName = rating + name,
                            yearGenre = successData.year.toString() + "  " + genres,
                            countryLength = countries + filmLength,
                            posterUrl = successData.posterUrl ?: "",
                            description = successData.description ?: "",
                            shortDescription = successData.shortDescription ?: "",
                            description250 = description250,
                            type = successData.type,
                            filmName = name,
                            webUrl = successData.webUrl,
                        ) as E
                    } else {
                        //для горизонтального рекуклера

                        film = RecyclerItem(
                            name = name,
                            iconUri = successData.posterUrl ?: "",
                            genre = genres,
                            rating = rating.toDoubleOrNull(),
                            id = successData.kinopoiskId
                        ) as E
                    }

                //Log.d("Nik", "film ${film}")
            }

            else -> Log.d("Nik", "object MapperApiToUi.mapperFilmUi: Неизвестный тип для мэппинга")
        }
        return film as E
    }


    //добавляет последний фрагмент в ленту для прехода
    private fun addLastElement(): RecyclerItem {
        return RecyclerItem(
            name = "",
            //nameEn = "",
            iconUri = "R.drawable.image_go_listpage",
            genre = "",
            id = 1
        )
    }
}