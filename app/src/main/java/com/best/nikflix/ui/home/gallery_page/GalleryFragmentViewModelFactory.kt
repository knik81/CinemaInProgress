package com.best.nikflix.ui.home.gallery_page

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import javax.inject.Inject

class GalleryFragmentViewModelFactory @Inject constructor(
    private val galleryFragmentViewModel: GalleryFragmentViewModel
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return galleryFragmentViewModel as T
    }
}