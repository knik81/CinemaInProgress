package com.best.nikflix.ui.search.search_config_page.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.best.entity.Filters
import com.best.nikflix.ui.search.entity.Screen

@Composable
fun ScreenSelectorCompose(
    filters: Filters?,
    arrowBack: (Boolean) -> Unit
) {

    val screen = remember {
        mutableStateOf(Screen.Main as Screen)
    }

    when (screen.value) {
        Screen.Main -> {
            arrowBack(true)
            SearchConfigCompose {
                screen.value = it
            }
        }

        Screen.Country -> {
            arrowBack(false)
            SearchConfigCountryFragmentCompose(filters?.countries){
                screen.value = Screen.Main
            }
        }

        Screen.Genre -> {
            arrowBack(false)
            SearchConfigGenreFragmentCompose(filters?.genres) {
                screen.value = Screen.Main
            }
        }

        Screen.Date -> {
            arrowBack(false)
            SearchConfigDateCompose{
                screen.value = Screen.Main
            }
        }
    }
}