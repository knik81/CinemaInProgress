package com.example.testcinema.ui.home.person_page

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.entity.ApiDataUseCaseInterface
import com.example.entity.ApiParameters
import com.example.entity.FilmApiInterface
import com.example.entity.ItemApiUniversalInterface
import com.example.entity.PersonInterface
import com.example.entity.ResultFromApi
import com.example.testcinema.entity.RecyclerItem
import com.example.testcinema.ui.home.common.MapperApiToUi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class PersonFragmentViewModel @Inject constructor(
    private val apiDataUseCase: ApiDataUseCaseInterface
) : ViewModel() {

    // ошибка
    private val _errorStateFlow = MutableStateFlow<String?>(null)
    val errorStateFlow = _errorStateFlow.asStateFlow()

    //персона
    private val _personStateFlow = MutableStateFlow<PersonInterface?>(null)
    val personStateFlow = _personStateFlow.asStateFlow()

    //запрос в апи данных персоны
    fun getPerson(idPerson: String, takeFilms: Int? = null) {
        viewModelScope.launch {
            when (val result =
                apiDataUseCase.getDataApi(ApiParameters.PERSON.type, null, idPerson)) {
                is ResultFromApi.Success<*> -> {
                    val temp = result.successData as PersonInterface
                    getFilms(temp.films, takeFilms)
                    _personStateFlow.value = temp
                }

                is ResultFromApi.Error<*> -> {
                    val errorMessage = result.ErrorMessage as String
                    Log.d("Nik", "PersonFragmentViewModel.getPerson $errorMessage")
                    _errorStateFlow.value = errorMessage
                }
            }
        }
    }

    //список 10 лучших фильмов
    private val _filmsStateFlow = MutableStateFlow<List<RecyclerItem>?>(null)
    val filmsStateFlow = _filmsStateFlow.asStateFlow()


    //список 10 лучших фильмов
    private val _totalFilmsStateFlow = MutableStateFlow<Int?>(null)
    val totalFilmsStateFlow = _totalFilmsStateFlow.asStateFlow()

    private fun getFilms(
        itemApiUniversalList: List<ItemApiUniversalInterface>,
        takeFilms: Int? = null
    ) {

        val allFilms = itemApiUniversalList
            .distinctBy { it.filmId }
            .sortedByDescending { it.rating}

        val films: List<ItemApiUniversalInterface>
        if (takeFilms != null) {
            films = allFilms.take(takeFilms)
            if (allFilms.size > takeFilms - 1)
                _totalFilmsStateFlow.value = allFilms.size
        } else
            films = allFilms



        viewModelScope.launch {
            val filmApiList = mutableListOf<FilmApiInterface>()

            //обращение к апи по каждому фильму
            films.forEach { film ->
                when (val result = apiDataUseCase.getDataApi(
                    type = ApiParameters.FILM.type,
                    filters = null,
                    id = film.filmId.toString()
                )) {
                    is ResultFromApi.Success<*> -> {
                        filmApiList.add(result.successData as FilmApiInterface)
                    }

                    is ResultFromApi.Error<*> -> {
                        _errorStateFlow.value = result.ErrorMessage.toString()
                        Log.d("Nik", "${result.ErrorMessage}")
                    }
                }
            }
            //Log.d("Nik", "temp ${temp}")
            _filmsStateFlow.value = com.example.testcinema.ui.home.common.MapperApiToUi.mapperRecyclerHorizontalLimit(filmApiList)
        }
    }


}

