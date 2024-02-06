package com.example.testcinema.ui.home.home_page

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.entity.ApiDataUseCaseInterface
import com.example.entity.ApiParameters
import com.example.entity.Country
import com.example.entity.Filters
import com.example.entity.Genre
import com.example.entity.ResultFromApi
import com.example.testcinema.entity.RecyclerItem
import com.example.testcinema.ui.home.common.MapperApiToUi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class HomeFargmentViewModel @Inject constructor(
    private val allFilmsUseCase: ApiDataUseCaseInterface
) : ViewModel() {

    //переменная для загрузки ошибки
    private val _errorStateFlow = MutableStateFlow<String?>(null)
    val errorStateFlow = _errorStateFlow.asStateFlow()

    //страны и жанры
    private var getFiltersStarted = false
    private var filters: Filters? = null

    init {
        //разовое получение списка стран и жанров
        viewModelScope.launch {
            when (val resultFilters =
                allFilmsUseCase.getDataApi(
                    com.example.entity.ApiParameters.FILTERS.type,
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
    fun getPremiers() {
        viewModelScope.launch {
            when (val result = allFilmsUseCase.getDataApi(
                com.example.entity.ApiParameters.PREMIERS.type,
                null,
                null
            )) {
                is ResultFromApi.Success<*> ->
                    _premieresStateFlow.value =
                        com.example.testcinema.ui.home.common.MapperApiToUi.mapperRecyclerHorizontalLimit(result.successData)

                is ResultFromApi.Error<*> -> {
                    Log.d("Nik", "${result.ErrorMessage}")
                    _errorStateFlow.value = result.ErrorMessage.toString()
                }
            }
        }
    }

    //Популярные
    private val _popularStateFlow = MutableStateFlow<List<RecyclerItem>?>(null)
    val popularStateFlow = _popularStateFlow.asStateFlow()
    fun getPopular() {
        viewModelScope.launch {
            when (val result = allFilmsUseCase.getDataApi(
                ApiParameters.POPULAR.type,
                null,
                null
            )) {
                is ResultFromApi.Success<*> ->
                    _popularStateFlow.value =
                        com.example.testcinema.ui.home.common.MapperApiToUi.mapperRecyclerHorizontalLimit(result.successData)

                is ResultFromApi.Error<*> -> {
                    Log.d("Nik", "${result.ErrorMessage}")
                    _errorStateFlow.value = result.ErrorMessage.toString()
                }
            }
        }
    }


    //Топ250
    private val _top250StateFlow = MutableStateFlow<List<RecyclerItem>?>(null)
    val top250StateFlow = _top250StateFlow.asStateFlow()
    fun getTop250() {
        viewModelScope.launch {
            when (val result = allFilmsUseCase.getDataApi(
                ApiParameters.TOP250.type,
                null,
                null
            )) {
                is ResultFromApi.Success<*> ->
                    _top250StateFlow.value =
                        com.example.testcinema.ui.home.common.MapperApiToUi.mapperRecyclerHorizontalLimit(result.successData)

                is ResultFromApi.Error<*> -> {
                    Log.d("Nik", "${result.ErrorMessage}")
                    _errorStateFlow.value = result.ErrorMessage.toString()
                }
            }
        }
    }


    //Случайные фильмы
    private val _randomFilms1StateFlow = MutableStateFlow<List<RecyclerItem>?>(null)
    val randomFilm1StateFlow = _randomFilms1StateFlow.asStateFlow()
    private val _randomFilms2StateFlow = MutableStateFlow<List<RecyclerItem>?>(null)
    val randomFilm2StateFlow = _randomFilms2StateFlow.asStateFlow()
    var labelRandomFilm1 = ""
        private set
    var labelRandomFilm2 = ""
        private set
    var randomFilters1: Filters? = null
        private set
    var randomFilters2: Filters? = null
        private set

    //Случайный фильм - обращение к АПИ
    fun getRandom(first: Boolean) {
        viewModelScope.launch {

            //ждун данных из АПИ со списком стран и жанров.
            var count = 0
            while (!getFiltersStarted || count < 11) {
                delay(200)
                count++
            }

            val randomFilter = getRandomFilter()
            val label = randomFilter.countries[0]?.country.toString() + ", " +
                    randomFilter.genres[0]?.genre

            //Случайный фильм - запрос в АПИ
            when (val resultFilm =
                allFilmsUseCase.getDataApi(
                    ApiParameters.RANDOM_FILMS.type,
                    randomFilter,
                    null
                )

            ) {
                //Случайный фильм - получение из АПИ
                is ResultFromApi.Success<*> -> {
                    if (first) {
                        randomFilters1 = randomFilter

                        labelRandomFilm1 = label

                        _randomFilms1StateFlow.value =
                            com.example.testcinema.ui.home.common.MapperApiToUi.mapperRecyclerHorizontalLimit(resultFilm.successData)
                    } else {
                        randomFilters2 = randomFilter

                        labelRandomFilm2 = label

                        _randomFilms2StateFlow.value =
                            com.example.testcinema.ui.home.common.MapperApiToUi.mapperRecyclerHorizontalLimit(resultFilm.successData)
                    }
                }

                is ResultFromApi.Error<*> -> {
                    Log.d("Nik", "${resultFilm.ErrorMessage}")
                    _errorStateFlow.value = resultFilm.ErrorMessage.toString()
                }
            }
        }
    }


    // Сериалы
    private val _seriesStateFlow = MutableStateFlow<List<RecyclerItem>?>(null)
    val seriesStateFlow = _seriesStateFlow.asStateFlow()
    fun getSeries() {
        viewModelScope.launch {
            when (val result = allFilmsUseCase.getDataApi(
                com.example.entity.ApiParameters.SERIES.type,
                null,
                null
            )) {
                is ResultFromApi.Success<*> ->
                    _seriesStateFlow.value =
                        com.example.testcinema.ui.home.common.MapperApiToUi.mapperRecyclerHorizontalLimit(result.successData)

                is ResultFromApi.Error<*> -> {
                    Log.d("Nik", "${result.ErrorMessage}")
                    _errorStateFlow.value = result.ErrorMessage.toString()
                }
            }
        }
    }


    //Сгенерировать случайную страну и жанр
    private fun getRandomFilter(): Filters// Pair<CountryInterface?, GenreInterface?>
    {
        //случайная страна и жанр из готового набора сочетаний
        val pairId = RandomFilmsPair.getRandomPair()

        val country = filters?.countries?.find { it?.id == pairId.first }
        val genre = filters?.genres?.find { it?.id == pairId.second }

        return Filters(
            countries = listOf(country as Country),
            genres = listOf(genre as Genre)
        )
    }

}