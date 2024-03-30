package com.best.nikflix.ui.home.gallery_page

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.best.entity.ApiDataUseCaseInterface
import com.best.entity.ItemApiUniversalInterface
import com.best.entity.QueryParams
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GalleryFragmentViewModel @Inject constructor(
    private val apiDataUseCase: ApiDataUseCaseInterface
): ViewModel(){


    fun getPagingData(
        type: String,
        queryParams: QueryParams?,
        id: String?,
        imageType: String
    ): Flow<PagingData<ItemApiUniversalInterface>> {
        //Log.d("Nik", "getImageFromApi launch ")
        //_stateStateFlow.value = State.Loading
        //Log.d("Nik",imageType)
        val result = apiDataUseCase.getPagingData(type = type, queryParams = queryParams, id = id, imageType)
        //_stateStateFlow.value = State.Finish
        // Log.d("Nik","$result")
        return result.flow.cachedIn(viewModelScope)
    }

}