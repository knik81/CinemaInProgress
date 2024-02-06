package com.example.testcinema.ui.home.film_page

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.entity.ApiDataUseCaseInterface
import com.example.entity.ApiParameters
import com.example.entity.ImagesInterface
import com.example.entity.ResultFromApi
import com.example.testcinema.entity.FilmUi
import com.example.testcinema.entity.Recycler
import com.example.testcinema.entity.RecyclerItem
import com.example.testcinema.ui.home.common.MapperApiToUi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class FilmFragmentViewModel @Inject constructor(
    private val allFilmsUseCase: ApiDataUseCaseInterface
) : ViewModel() {


    //переменная для загрузки данных с описанием фильма
    private val _filmStateFlow = MutableStateFlow<FilmUi?>(null)
    val filmStateFlow = _filmStateFlow.asStateFlow()

    //переменная для загрузки ошибки
    private val _errorStateFlow = MutableStateFlow<String?>(null)
    val errorStateFlow = _errorStateFlow.asStateFlow()

    //запрос данных фильма
    fun getFilm(id: Int) {
        viewModelScope.launch {
            when (val result = allFilmsUseCase.getDataApi(
                type = ApiParameters.FILM.type,
                filters = null,
                id = id.toString()
            )) {
                is ResultFromApi.Success<*> -> {
                    _filmStateFlow.value =
                        com.example.testcinema.ui.home.common.MapperApiToUi.mapperFilmApiToUI(result.successData)
                }

                is ResultFromApi.Error<*> -> {
                    _errorStateFlow.value = result.ErrorMessage.toString()
                    Log.d("Nik", "${result.ErrorMessage}")
                }
            }
        }
    }


    //переменная для хранения актеров
    private val _actorStateFlow = MutableStateFlow<List<RecyclerItem>?>(null)
    val actorStateFlow = _actorStateFlow.asStateFlow()

    //переменная для хранения над фильмом работали
    private val _stuffStateFlow = MutableStateFlow<List<RecyclerItem>?>(null)
    val stuffStateFlow = _stuffStateFlow.asStateFlow()

    //запрос данных персонала
    fun getStaff(id: Int) {
        viewModelScope.launch {
            when (val result = allFilmsUseCase.getDataApi(
                type = ApiParameters.STAFF.type,
                filters = null,
                id = id.toString()
            )) {
                is ResultFromApi.Success<*> -> {
                    _stuffStateFlow.value =
                        com.example.testcinema.ui.home.common.MapperApiToUi.mapperRecyclerHorizontalLimitStaff(
                            result.successData,
                            false
                        )
                    _actorStateFlow.value =
                        com.example.testcinema.ui.home.common.MapperApiToUi.mapperRecyclerHorizontalLimitStaff(
                            result.successData,
                            true
                        )
                }

                is ResultFromApi.Error<*> -> {
                    _errorStateFlow.value = result.ErrorMessage.toString()
                    Log.d("Nik", "${result.ErrorMessage}")
                }
            }
        }
    }

    //переменные для хранения картинок
    //private val _imagesStateFlow = MutableStateFlow<List<RecyclerItem>?>(null)
    private val _imagesStateFlow = MutableStateFlow<Recycler?>(null)
    val imagesStateFlow = _imagesStateFlow.asStateFlow()

    //запрос  картинок к фильму
    fun getImages(id: Int) {
        //val recyclerItem: MutableList<RecyclerItem>
        //val _imagesStateFlow = MutableStateFlow<Recycler?>(null)
        viewModelScope.launch {
            when (val result = allFilmsUseCase.getDataApi(
                type = ApiParameters.IMAGES.type,
                filters = null,
                id = id.toString()
            )) {
                is ResultFromApi.Success<*> -> {
                    val successData = result.successData as ImagesInterface
                    val recyclerItem: List<RecyclerItem> =
                        MapperApiToUi.mapperRecyclerHorizontalLimit(successData)

                    _imagesStateFlow.value = Recycler(
                        type = ApiParameters.IMAGES.type,
                        totalItemsFromApi = successData.total,
                        itemList = recyclerItem
                    )
                }

                is ResultFromApi.Error<*> -> {
                    _errorStateFlow.value = result.ErrorMessage.toString()
                    Log.d("Nik", "${result.ErrorMessage}")
                }
            }
        }
    }

    //переменная для хранения похжих фильмов
    private val _similarsStateFlow = MutableStateFlow<List<RecyclerItem>?>(null)
    val similarsStateFlow = _similarsStateFlow.asStateFlow()

    //запрос похожих фильмов
    fun getSimilars(id: Int) {
        viewModelScope.launch {
            when (val result = allFilmsUseCase.getDataApi(
                type = ApiParameters.SIMILARS.type,
                filters = null,
                id = id.toString()
            )) {
                is ResultFromApi.Success<*> -> {
                    _similarsStateFlow.value =
                        com.example.testcinema.ui.home.common.MapperApiToUi.mapperRecyclerHorizontalLimit(
                            result.successData
                        )
                }

                is ResultFromApi.Error<*> -> {
                    _errorStateFlow.value = result.ErrorMessage.toString()
                    Log.d("Nik", "${result.ErrorMessage}")
                }
            }
        }
    }

}