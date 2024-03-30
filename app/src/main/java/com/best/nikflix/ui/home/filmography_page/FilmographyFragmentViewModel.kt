package com.best.nikflix.ui.home.filmography_page

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.best.entity.ApiDataUseCaseInterface
import com.best.entity.ApiParameters
import com.best.entity.FilmApiInterface
import com.best.entity.ItemApiUniversalInterface
import com.best.entity.ResultFromApi
import com.best.nikflix.entity.RecyclerItem
import com.best.nikflix.ui.home.common.MapperApiToUi
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class FilmographyFragmentViewModel @Inject constructor(
    private val apiDataUseCase: ApiDataUseCaseInterface
) : ViewModel() {

    //список фильмов
    private val _filmsStateFlow = MutableStateFlow<List<RecyclerItem>?>(null)
    val filmsStateFlow = _filmsStateFlow.asStateFlow()

    // ошибка
    private val _errorStateFlow = MutableStateFlow<String?>(null)
    val errorStateFlow = _errorStateFlow.asStateFlow()

    private var job: Job? = null
    fun getFilms(
        itemApiUniversalList: List<ItemApiUniversalInterface>,
        takeFilms: Int? = null,
        professionKey: String?
    ) {

        //отмена джоба при переключении чипов
        job?.cancel()


        val allFilms = if (professionKey != null) {
            itemApiUniversalList
                .filter { it.professionKey == professionKey }
                .distinctBy { it.filmId }
                .sortedByDescending { it.rating }
        } else itemApiUniversalList
            .distinctBy { it.filmId }
            .sortedByDescending { it.rating }


        val films: List<ItemApiUniversalInterface> =
            if (takeFilms != null)
                allFilms.take(takeFilms)
            else
                allFilms

        job = viewModelScope.launch {
        //job =  CoroutineScope(Dispatchers.IO).launch {
            val filmApiList = mutableListOf<FilmApiInterface>()
            val recyclerItemList = mutableListOf<RecyclerItem>()

            //обращение к апи по каждому фильму
            films.forEachIndexed { index, film ->
                //Log.d("Nik", "film $film")

                //придумал чтобы ограничить лимит
                if (index < 30) {
                    when (val result = apiDataUseCase.getDataApi(
                        type = ApiParameters.FILM.type,
                        queryParams = null,
                        id = film.filmId.toString()
                    )) {
                        is ResultFromApi.Success<*> -> {
                            // Log.d("Nik", "film.filmId ${film.filmId}")
                            val temp = result.successData as FilmApiInterface
                            filmApiList.add(temp)
                            // Log.d("Nik", "temp ${temp}")

                        }

                        is ResultFromApi.Error<*> -> {
                            _errorStateFlow.value = result.ErrorMessage.toString()
                            Log.d("Nik", "${result.ErrorMessage}")
                        }
                    }


                    //перекладывание результата
                    // и пауза от блокировки сервера
                    if ((index + 1) % 10 == 0) {
                        _filmsStateFlow.value =
                            MapperApiToUi.mapperRecyclerHorizontalLimit(filmApiList)
                        delay(2000)
                    }


                } else {
                    recyclerItemList.addAll(MapperApiToUi.mapperRecyclerHorizontalLimit(film))
                }
            }
            _filmsStateFlow.value = null
            recyclerItemList.addAll(MapperApiToUi.mapperRecyclerHorizontalLimit(filmApiList))
            recyclerItemList.addAll(recyclerItemList)
            _filmsStateFlow.value = recyclerItemList.sortedByDescending { it.rating }.distinct()
        }

    }

    /*
    override fun onCleared() {
        super.onCleared()
        //Log.d("Nik", "FilmographyFragmentViewModel.onCleared")
        //job?.cancel()
    }

     */
}