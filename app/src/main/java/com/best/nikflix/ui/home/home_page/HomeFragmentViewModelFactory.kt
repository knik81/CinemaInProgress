package com.best.nikflix.ui.home.home_page

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import javax.inject.Inject

class HomeFragmentViewModelFactory @Inject constructor(
    private val homeFragmentViewModel: HomeFragmentViewModel
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return homeFragmentViewModel as T
    }
}