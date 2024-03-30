package com.best.nikflix.ui.home.common.room_view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import javax.inject.Inject

class RoomViewModelFactory @Inject constructor(
    private val roomViewModel: RoomViewModel
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return roomViewModel as T
    }
}