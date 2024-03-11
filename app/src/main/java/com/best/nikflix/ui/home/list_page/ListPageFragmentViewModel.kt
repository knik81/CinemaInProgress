package com.best.nikflix.ui.home.list_page

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.best.entity.ApiDataUseCaseInterface
import com.best.entity.ApiParameters
import com.best.entity.ItemApiUniversalInterface
import com.best.entity.QueryParams
import com.best.entity.ResultFromApi
import com.best.nikflix.entity.RecyclerItem
import com.best.nikflix.ui.home.common.MapperApiToUi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class ListPageFragmentViewModel @Inject constructor(
    private val apiDataUseCase: ApiDataUseCaseInterface
) : ViewModel() {


    fun getPagingData(
        type: String,
        queryParams: QueryParams?,
        id: String?
    ): Flow<PagingData<ItemApiUniversalInterface>> {
        val result = apiDataUseCase.getPagingData(
            type = type,
            queryParams = queryParams,
            id = id,
            imageType = null
        )
        return result.flow.cachedIn(viewModelScope)
    }

    private val _filmListStateFlow = MutableStateFlow<List<RecyclerItem>?>(null)
    val filmListStateFlow = _filmListStateFlow.asStateFlow()
    fun getFilms(idFilList: List<String>) {
       // Log.d("Nik", "idFilList  ${idFilList}")
        viewModelScope.launch {
            val filmList = mutableListOf<RecyclerItem>()
            idFilList.forEach { idFilm ->
                when (val result = apiDataUseCase.getDataApi(
                    type = ApiParameters.FILM.type,
                    queryParams = null,
                    id = idFilm
                )) {
                    is ResultFromApi.Success<*> -> {
                        filmList.add(MapperApiToUi.mapperFilmApiToUI(result.successData,true))
                    }

                    is ResultFromApi.Error<*> -> {
                        // _errorStateFlow.value = result.ErrorMessage.toString()
                        Log.d("Nik", "${result.ErrorMessage}")
                    }
                }
            }
            _filmListStateFlow.value = filmList
        }
    }

}