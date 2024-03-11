package com.best.nikflix.ui.home.person_page

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import javax.inject.Inject

class PersonFragmentViewModelFactory @Inject constructor(
    private val personFragmentViewModel: PersonFragmentViewModel
):ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return personFragmentViewModel as T
    }
}