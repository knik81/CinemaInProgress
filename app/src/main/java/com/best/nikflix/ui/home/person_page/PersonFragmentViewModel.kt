package com.best.nikflix.ui.home.person_page

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.best.entity.ApiDataUseCaseInterface
import com.best.entity.ApiParameters
import com.best.entity.PersonInterface
import com.best.entity.ResultFromApi
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

    //всего фильмов
    private val _totalFilmsStateFlow = MutableStateFlow<Int?>(null)
    val totalFilmsStateFlow = _totalFilmsStateFlow.asStateFlow()


    //запрос в апи данных персоны
    fun getPerson(idPerson: String, takeFilms: Int) {
        viewModelScope.launch {
            when (val result =
                apiDataUseCase.getDataApi(ApiParameters.PERSON.type, null, idPerson)) {
                is ResultFromApi.Success<*> -> {
                    val temp = result.successData as PersonInterface
                    //getFilms(temp.films, takeFilms)
                    //temp.films.forEach {
                     //   Log.d("Nik", "it $it")
                   // }

                    _personStateFlow.value = temp
                    if (temp.films.size > takeFilms - 1)
                        _totalFilmsStateFlow.value = temp.films.size
                }

                is ResultFromApi.Error<*> -> {
                    val errorMessage = result.ErrorMessage as String
                    Log.d("Nik", "PersonFragmentViewModel.getPerson $errorMessage")
                    _errorStateFlow.value = errorMessage
                }
            }
        }
    }
}

