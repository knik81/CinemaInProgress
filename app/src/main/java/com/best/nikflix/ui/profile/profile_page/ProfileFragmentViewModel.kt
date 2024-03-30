package com.best.nikflix.ui.profile.profile_page

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.best.entity.ApiDataUseCaseInterface
import com.best.entity.ApiParameters
import com.best.entity.FilmAndCollectionF
import com.best.entity.FilmApiInterface
import com.best.entity.ResultFromApi
import com.best.entity.CollectionParameters
import com.best.nikflix.entity.RecyclerItem
import com.best.nikflix.ui.home.common.MapperApiToUi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class ProfileFragmentViewModel @Inject constructor(
    private val apiDataUseCase: ApiDataUseCaseInterface
) : ViewModel() {

    //переменная для загрузки ошибки
    private val _errorStateFlow = MutableStateFlow<String?>(null)
    val errorStateFlow = _errorStateFlow.asStateFlow()

    private val _alreadySawListStateFlow = MutableStateFlow<List<RecyclerItem>?>(null)
    val alreadySawListStateFlow = _alreadySawListStateFlow.asStateFlow()

    private val _interestingListStateFlow = MutableStateFlow<List<RecyclerItem>?>(null)
    val interestingListStateFlow = _interestingListStateFlow.asStateFlow()

    //получить список фильмов для вывода в ленту
    fun getFilms(specialList: List<FilmAndCollectionF>) {
        viewModelScope.launch(Dispatchers.IO) {
            val alreadySawList = mutableListOf<FilmApiInterface>()
            val interestingList = mutableListOf<FilmApiInterface>()

            specialList.forEach { filmAndCollectionF ->
                when (val result = apiDataUseCase.getDataApi(
                    type = ApiParameters.FILM.type,
                    queryParams = null,
                    id = filmAndCollectionF.filmId
                )) {
                    //переложить апи в соответсвующий список интересно или просмотрено
                    is ResultFromApi.Success<*> -> {
                        when (filmAndCollectionF.collection) {
                            CollectionParameters.ALREADYSAW.label -> {
                                alreadySawList.add(result.successData as FilmApiInterface)
                            }
                            CollectionParameters.INTERESTING.label -> {
                                interestingList.add(result.successData as FilmApiInterface)
                            }
                        }
                    }

                    is ResultFromApi.Error<*> -> {
                        _errorStateFlow.value = result.ErrorMessage.toString()
                        Log.d("Nik", "${result.ErrorMessage}")
                    }
                }
            }
            _alreadySawListStateFlow.value = MapperApiToUi.mapperRecyclerHorizontalLimit(alreadySawList)
            _interestingListStateFlow.value = MapperApiToUi.mapperRecyclerHorizontalLimit(interestingList)
        }
    }


}