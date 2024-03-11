package com.best.nikflix.ui.home.film_page

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import javax.inject.Inject

class FilmFragmentViewModelFactory @Inject constructor(
    private val filmFragmentViewModel: FilmFragmentViewModel
):ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return filmFragmentViewModel as T
    }
}