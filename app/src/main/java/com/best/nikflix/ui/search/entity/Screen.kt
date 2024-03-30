package com.best.nikflix.ui.search.entity

sealed class Screen {
    data object Main: Screen()
    data object Country: Screen()
    data object Genre: Screen()
    data object Date: Screen()
}