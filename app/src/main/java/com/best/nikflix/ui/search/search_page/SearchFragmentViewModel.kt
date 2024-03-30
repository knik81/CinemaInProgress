package com.best.nikflix.ui.search.search_page

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.best.entity.ApiDataUseCaseInterface
import com.best.entity.ApiParameters
import com.best.entity.CollectionParameters
import com.best.entity.Country
import com.best.entity.FilmAndCollectionF
import com.best.entity.Filters
import com.best.entity.Genre
import com.best.entity.ItemApiUniversalInterface
import com.best.entity.QueryParams
import com.best.entity.ResultFromApi
import com.best.entity.RoomUseCaseInterface
import com.best.entity.SearchText
import com.best.nikflix.ui.search.entity.QueryParamsObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class SearchFragmentViewModel @Inject constructor(
    private val apiDataUseCaseInterface: ApiDataUseCaseInterface,
    private val roomUseCase: RoomUseCaseInterface
) : ViewModel() {


    //переменная для загрузки ошибки
    private val _errorStateFlow = MutableStateFlow<String?>(null)
    val errorStateFlow = _errorStateFlow.asStateFlow()


    //страны и жанры
    private lateinit var filters: Filters
    val filtersStateFlow = MutableStateFlow<Filters?>(null)


    init {
        //разовое получение списка стран и жанров
        viewModelScope.launch {
            when (val resultFilters =
                apiDataUseCaseInterface.getDataApi(
                    ApiParameters.FILTERS.type,
                    null,
                    null
                )) {
                is ResultFromApi.Success<*> -> {
                   // val resultFilter = resultFilters.successData as Filters

                    //создание пустышной страны для удобства поиска
                    val countries = mutableListOf(
                        Country(
                            countryName = "не задано",
                            id = "",
                            country = "не задано"
                        )
                    )
                    countries.addAll((resultFilters.successData as Filters).countries)

                    //создание пустышного жанра для удобства поиска
                    val genre = mutableListOf(
                        Genre(
                            genre = "не задано",
                            id = "",
                        )
                    )

                    //удалил жанр для взрослых
                    genre.addAll((resultFilters.successData as Filters).genres.filter {
                        it.id != "28"
                    })


                    //сборка фильтра с пустышками
                    filters = Filters(
                        genres = genre,
                        countries = countries
                    )
                   // filters. = resultFilters.successData as Filters

                    filtersStateFlow.value = filters
                    //Log.d("Nik", "${filters.countries}")

                    //жанр по умолчанию
                    val genres = filters.genres.filter { it.genre == "не задано" }
                    if (genres.isNotEmpty())
                        QueryParamsObject.genres = genres.first()
                    else
                        QueryParamsObject.genres = filters.genres.first()


                    //Страна по умолчанию
                    val country = filters.countries.filter { it.country == "не задано" }
                    if (country.isNotEmpty())
                        QueryParamsObject.countries = country.first()
                    else
                        QueryParamsObject.countries = filters.countries.first()
                    //Log.d("Nik", "${country}")

                }

                is ResultFromApi.Error<*> -> {
                    Log.d("Nik", "${resultFilters.ErrorMessage}")
                    _errorStateFlow.value = resultFilters.ErrorMessage.toString()
                }
            }
        }
    }

    private var _resultStateflowPager =
        MutableStateFlow<PagingData<ItemApiUniversalInterface>>(PagingData.empty())
    var resultStateflowPager = _resultStateflowPager.asStateFlow()


    fun getSearchFilms(queryParams: QueryParams?) {
        viewModelScope.launch {
            //Log.d("Nik", "queryParams $queryParams")
            val temp = apiDataUseCaseInterface.getPagingData(
                type = ApiParameters.SEARCH.type,
                queryParams = queryParams,
                id = null,
                imageType = null
            )
            temp.flow.cachedIn(viewModelScope)
                .collect {
                    _resultStateflowPager.value = it
                }

        }
    }

    fun getSearchTextFlow() = roomUseCase.getSearchText()


    private var _alreadySawStateFlow = MutableStateFlow<List<FilmAndCollectionF>?>(null)
    var alreadySawStateFlow = _alreadySawStateFlow.asStateFlow()

    fun getAlreadySaw() {
        viewModelScope.launch(Dispatchers.IO) {
            roomUseCase.getFilmAndCollectionFByCollection(
                CollectionParameters.ALREADYSAW.label
            ).collect {
                _alreadySawStateFlow.value = it
            }
        }
    }

    fun insertSearchText(text: String) {
        viewModelScope.launch(Dispatchers.IO) {
            //добавить текст поиска в базу данных
            if (!roomUseCase.isExistSearchTextByText(text))
                roomUseCase.insertSearchText(SearchText((text)))
            delay(100)
            //удалить первую созданный текст поиска
            roomUseCase.getSearchText().collect {
                if (!it.isNullOrEmpty())
                    if (it.size > 10)
                        roomUseCase.deleteSearchText(SearchText(it.first()))
            }
        }

    }

}