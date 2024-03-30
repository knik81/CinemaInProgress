package com.best.nikflix.ui.home.filmography_page

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import javax.inject.Inject

class FilmographyFragmentViewModelFactory @Inject constructor(
    private val filmographyFragmentViewModel: FilmographyFragmentViewModel
):ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return filmographyFragmentViewModel as T
    }
}