package com.best.nikflix.ui.home.home_page

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.best.data.entity.PremiersSimilarsApi
import com.best.entity.ApiDataUseCaseInterface
import com.best.entity.ApiParameters
import com.best.entity.CollectionParameters
import com.best.entity.Country
import com.best.entity.FilmAndCollectionF
import com.best.entity.Filters
import com.best.entity.Genre
import com.best.entity.PremiersSimilarsApiInterface
import com.best.entity.QueryParams
import com.best.entity.ResultFromApi
import com.best.entity.RoomUseCaseInterface
import com.best.nikflix.entity.RecyclerItem
import com.best.nikflix.ui.home.common.MapperApiToUi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class HomeFragmentViewModel @Inject constructor(
    private val apiDataUseCaseInterface: ApiDataUseCaseInterface,
    private val roomUseCase: RoomUseCaseInterface
) : ViewModel() {


    //переменная для загрузки ошибки
    private val _errorStateFlow = MutableStateFlow<String?>(null)
    val errorStateFlow = _errorStateFlow.asStateFlow()

    //страны и жанры
    private var getFiltersStarted = false
    private var filters: Filters? = null

    private val _stateStateFlow = MutableStateFlow(false)
    val stateLoadStateFlow = _stateStateFlow.asStateFlow()

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
                    filters = resultFilters.successData as Filters
                    //индикатор обращения к АПИ
                    getFiltersStarted = true


                }

                is ResultFromApi.Error<*> -> {
                    Log.d("Nik", "${resultFilters.ErrorMessage}")
                    _errorStateFlow.value = resultFilters.ErrorMessage.toString()
                }
            }
        }
    }

    //индикатор первого запуска. Чтобы не делать повторный запрос к АПИ
    var firstStart = true

    // Пермьеры
    private val _premieresStateFlow = MutableStateFlow<List<RecyclerItem>?>(null)
    val premieresStateFlow = _premieresStateFlow.asStateFlow()
    private val _premieresRecyclerItemStateFlow = MutableStateFlow<List<RecyclerItem>?>(null)
    val premieresRecyclerItemStateFlow = _premieresRecyclerItemStateFlow.asStateFlow()
    fun getPremiers() {
        _stateStateFlow.value = false
        viewModelScope.launch {
            when (val result = apiDataUseCaseInterface.getDataApi(
                ApiParameters.PREMIERS.type,
                null,
                null
            )) {
                is ResultFromApi.Success<*> -> {
                    _premieresStateFlow.value =
                        MapperApiToUi.mapperRecyclerHorizontalLimit(result.successData)
                    _stateStateFlow.value = true

                   // val test2 = test as PremiersSimilarsApi_4
                }

                is ResultFromApi.Error<*> -> {
                    Log.d("Nik", "${result.ErrorMessage}")
                    _errorStateFlow.value = result.ErrorMessage.toString()
                    _stateStateFlow.value = true
                }
            }
        }
    }

    //Популярные
    private val _popularStateFlow = MutableStateFlow<List<RecyclerItem>?>(null)
    val popularStateFlow = _popularStateFlow.asStateFlow()
    private val _popularRecyclerItemStateFlow = MutableStateFlow<List<RecyclerItem>?>(null)
    val popularRecyclerItemStateFlow = _popularRecyclerItemStateFlow.asStateFlow()
    fun getPopular() {
        _stateStateFlow.value = false
        viewModelScope.launch {
            when (val result = apiDataUseCaseInterface.getDataApi(
                ApiParameters.POPULAR.type,
                null,
                null
            )) {
                is ResultFromApi.Success<*> -> {
                    _popularStateFlow.value =
                        MapperApiToUi.mapperRecyclerHorizontalLimit(
                            result.successData
                        )
                    _stateStateFlow.value = true
                }

                is ResultFromApi.Error<*> -> {
                    Log.d("Nik", "${result.ErrorMessage}")
                    _errorStateFlow.value = result.ErrorMessage.toString()
                    _stateStateFlow.value = true
                }
            }
        }
    }


    //Топ250
    private val _top250StateFlow = MutableStateFlow<List<RecyclerItem>?>(null)
    val top250StateFlow = _top250StateFlow.asStateFlow()
    private val _top250RecyclerItemStateFlow = MutableStateFlow<List<RecyclerItem>?>(null)
    val top250RecyclerItemStateFlow = _top250RecyclerItemStateFlow.asStateFlow()
    fun getTop250() {
        _stateStateFlow.value = false
        viewModelScope.launch {
            when (val result = apiDataUseCaseInterface.getDataApi(
                ApiParameters.TOP250.type,
                null,
                null
            )) {
                is ResultFromApi.Success<*> -> {
                    //Log.d("Nik", "collect ")
                    val data: List<RecyclerItem> =
                        MapperApiToUi.mapperRecyclerHorizontalLimit(result.successData)
                    _top250StateFlow.value = data
                    _stateStateFlow.value = true
                }


                is ResultFromApi.Error<*> -> {
                    Log.d("Nik", "${result.ErrorMessage}")
                    _errorStateFlow.value = result.ErrorMessage.toString()
                    _stateStateFlow.value = true
                }
            }
        }
    }


    //Случайные фильмы
    private val _randomFilms1StateFlow = MutableStateFlow<List<RecyclerItem>?>(null)
    val randomFilm1StateFlow = _randomFilms1StateFlow.asStateFlow()
    private val _randomFilms2StateFlow = MutableStateFlow<List<RecyclerItem>?>(null)
    val randomFilm2StateFlow = _randomFilms2StateFlow.asStateFlow()
    private val _randomFilms1RecyclerItemStateFlow = MutableStateFlow<List<RecyclerItem>?>(null)
    val randomFilm1RecyclerItemStateFlow = _randomFilms1RecyclerItemStateFlow.asStateFlow()
    private val _randomFilms2RecyclerItemStateFlow = MutableStateFlow<List<RecyclerItem>?>(null)
    val randomFilm2RecyclerItemStateFlow = _randomFilms2RecyclerItemStateFlow.asStateFlow()

    var labelRandomFilm1 = ""
        private set
    var labelRandomFilm2 = ""
        private set
    var randomFilters1: Pair<Genre, Country>? = null
        private set
    var randomFilters2: Pair<Genre, Country>? = null
        private set

    //Случайный фильм - обращение к АПИ
    fun getRandom(first: Boolean) {
        _stateStateFlow.value = false

        viewModelScope.launch {
            //ждун данных из АПИ со списком стран и жанров.
            var count = 0
            while (!getFiltersStarted || count < 11) {
                delay(200)
                count++
            }

            val randomFilter = getRandomFilter()
            val queryParams = QueryParams(
                genres = randomFilter.first,
                countries = randomFilter.second
            )
            val label = "${queryParams.genres.genre}, ${randomFilter.second.country}"

            //Случайный фильм - запрос в АПИ
            when (val resultFilm =
                apiDataUseCaseInterface.getDataApi(
                    ApiParameters.RANDOM_FILMS.type,
                    queryParams,
                    null
                )

            ) {
                //Случайный фильм - получение из АПИ
                is ResultFromApi.Success<*> -> {
                    if (first) {
                        randomFilters1 = randomFilter

                        labelRandomFilm1 = label

                        _randomFilms1StateFlow.value =
                            MapperApiToUi.mapperRecyclerHorizontalLimit(
                                resultFilm.successData
                            )
                        _stateStateFlow.value = true
                    } else {
                        randomFilters2 = randomFilter

                        labelRandomFilm2 = label

                        _randomFilms2StateFlow.value =
                            MapperApiToUi.mapperRecyclerHorizontalLimit(
                                resultFilm.successData
                            )
                        _stateStateFlow.value = true
                    }
                }

                is ResultFromApi.Error<*> -> {
                    Log.d("Nik", "${resultFilm.ErrorMessage}")
                    _errorStateFlow.value = resultFilm.ErrorMessage.toString()
                    _stateStateFlow.value = true
                }
            }
        }
    }


    // Сериалы
    private val _seriesStateFlow = MutableStateFlow<List<RecyclerItem>?>(null)
    val seriesStateFlow = _seriesStateFlow.asStateFlow()
    private val _seriesRecyclerItemStateFlow = MutableStateFlow<List<RecyclerItem>?>(null)
    val seriesRecyclerItemStateFlow = _seriesRecyclerItemStateFlow.asStateFlow()
    fun getSeries() {
        _stateStateFlow.value = false
        viewModelScope.launch {
            when (val result = apiDataUseCaseInterface.getDataApi(
                com.best.entity.ApiParameters.SERIES.type,
                null,
                null
            )) {
                is ResultFromApi.Success<*> -> {
                    _seriesStateFlow.value =
                        MapperApiToUi.mapperRecyclerHorizontalLimit(
                            result.successData
                        )
                    _stateStateFlow.value = true
                }

                is ResultFromApi.Error<*> -> {
                    Log.d("Nik", "${result.ErrorMessage}")
                    _errorStateFlow.value = result.ErrorMessage.toString()
                    _stateStateFlow.value = true
                }
            }
        }
    }

    fun checkAlreadySaw(data: List<RecyclerItem>, type: ApiParameters, first: Boolean = true) {
        viewModelScope.launch(Dispatchers.IO) {
            data.forEach {
                if (it != null)
                    it.alreadySaw = roomUseCase.existFilmAndCollectionF(
                        FilmAndCollectionF(
                            filmId = it.id.toString(),
                            collection = CollectionParameters.ALREADYSAW.label
                        )
                    )
                // Log.d("Nik", "2  ${it}")
            }
            when (type) {
                ApiParameters.TOP250 -> _top250RecyclerItemStateFlow.value = data

                ApiParameters.PREMIERS -> _premieresRecyclerItemStateFlow.value = data

                ApiParameters.POPULAR -> _popularRecyclerItemStateFlow.value = data

                ApiParameters.RANDOM_FILMS -> {
                    if (first)
                        _randomFilms1RecyclerItemStateFlow.value = data
                    else
                        _randomFilms2RecyclerItemStateFlow.value = data
                }

                ApiParameters.SERIES -> _seriesRecyclerItemStateFlow.value = data

                else -> {
                    _errorStateFlow.value =
                        "HomeFragmentViewModel.checkAlreadySaw Ошибка. Не определен тип"
                }

            }
        }
    }


    //Сгенерировать случайную страну и жанр
    private fun getRandomFilter(): Pair<Genre, Country>// Pair<CountryInterface?, GenreInterface?>
    {
        //случайная страна и жанр из готового набора сочетаний
        val pairId = RandomFilmsPair.getRandomPair()

        val country: Country = filters?.countries?.find { it.id == pairId.first } ?: Country()
        val genre: Genre = filters?.genres?.find { it.id == pairId.second } ?: Genre()

        return Pair(genre, country)
    }

}