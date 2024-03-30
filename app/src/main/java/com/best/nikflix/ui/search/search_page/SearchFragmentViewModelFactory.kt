package com.best.nikflix.ui.search.search_page

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import javax.inject.Inject

class SearchFragmentViewModelFactory @Inject constructor(
    private val searchFragmentViewModel: SearchFragmentViewModel
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return searchFragmentViewModel as T
    }
}