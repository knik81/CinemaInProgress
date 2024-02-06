package com.example.testcinema.ui.home.gallery_page

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.entity.ApiDataUseCaseInterface
import com.example.entity.Filters
import com.example.entity.ItemApiUniversalInterface
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GalleryFragmentViewModel @Inject constructor(
    val apiDataUseCase: ApiDataUseCaseInterface
): ViewModel(){


    suspend fun getPagingData(
        type: String,
        filters: Filters?,
        id: String?,
        imageType: String
    ): Flow<PagingData<ItemApiUniversalInterface>> {

        //_stateStateFlow.value = State.Loading
        //Log.d("Nik",imageType)
        val result = apiDataUseCase.getPagingData(type = type, filters = filters, id = id, imageType)
        //_stateStateFlow.value = State.Finish
        // Log.d("Nik","kjkjljl")
        return result.flow.cachedIn(viewModelScope)
    }

}