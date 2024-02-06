package com.example.testcinema.ui.home.list_page

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.entity.ApiDataUseCaseInterface
import com.example.entity.Filters
import com.example.entity.ItemApiUniversalInterface
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ListPageFragmentViewModel @Inject constructor(
    private val allFilmsUseCase: ApiDataUseCaseInterface
) : ViewModel() {

    //val _stateStateFlow = MutableStateFlow<State>(State.Finish)
    //val stateStateFlow = _stateStateFlow.asStateFlow()

    suspend fun getPagingData(
        type: String,
        filters: Filters?,
        id: String?
    ): Flow<PagingData<ItemApiUniversalInterface>> {
        //_stateStateFlow.value = State.Loading
        //Log.d("Nik",_stateStateFlow.value.toString())
        val result = allFilmsUseCase.getPagingData(
            type = type,
            filters = filters,
            id = id,
            imageType = null
        )
       //_stateStateFlow.value = State.Finish
       // Log.d("Nik","kjkjljl")
        return result.flow.cachedIn(viewModelScope)
    }

}