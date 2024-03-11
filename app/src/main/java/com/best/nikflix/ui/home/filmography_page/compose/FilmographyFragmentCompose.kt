package com.best.nikflix.ui.home.filmography_page.compose

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.best.nikflix.R
import com.best.entity.ApiParameters
import com.best.entity.PersonInterface
import com.best.nikflix.entity.RecyclerItem
import com.best.nikflix.ui.home.common.compose.CreateChip
import com.best.nikflix.ui.home.filmography_page.entity.ChipItem

@Composable
fun FilmographyFragmentCompose(
    person: PersonInterface?,
    films: List<RecyclerItem>?,
    chipList: List<ChipItem>,
    onClickChip: (String) -> Unit,
    onClickFilm: (Int) -> Unit
) {
    Column(
        modifier = Modifier
            .padding(10.dp)
            .fillMaxWidth(),
    ) {

        Text(
            text = ApiParameters.PERSON_FILMS.label,
            fontSize = 20.sp,
            color = colorResource(R.color.blackС),
            fontWeight = FontWeight.Bold,

            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 5.dp),
            textAlign = TextAlign.Center,
        )

        Text(
            text = person?.nameRu ?: person?.nameEn ?: "",
            fontSize = 25.sp,
            color = colorResource(R.color.blackС),
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 5.dp)
        )

        CreateChip(chipList) { nameChip ->
            //загрузить список фильмов по professionKey
            onClickChip(nameChip)
        }

        if (films != null) {
            RecyclerVerticalCompose(films) { filmId ->
                //открыть фильм
                onClickFilm(filmId)
            }
        }
    }
}






