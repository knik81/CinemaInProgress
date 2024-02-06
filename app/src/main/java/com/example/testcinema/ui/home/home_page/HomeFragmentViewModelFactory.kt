package com.example.testcinema.ui.home.home_page

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import javax.inject.Inject

class HomeFragmentViewModelFactory @Inject constructor(
    private val homeFargmentViewModel: HomeFargmentViewModel
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return homeFargmentViewModel as T
    }
}