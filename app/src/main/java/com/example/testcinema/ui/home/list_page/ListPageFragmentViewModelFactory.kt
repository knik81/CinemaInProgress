package com.example.testcinema.ui.home.list_page

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import javax.inject.Inject

class ListPageFragmentViewModelFactory @Inject constructor(
    val listPageFragmentViewModel: ListPageFragmentViewModel
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return listPageFragmentViewModel as T
    }
}