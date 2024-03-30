package com.best.nikflix.ui.profile.profile_page

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import javax.inject.Inject

class ProfileFragmentViewModelFactory @Inject constructor(
    private val profileFragmentViewModel: ProfileFragmentViewModel
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return profileFragmentViewModel as T
    }
}