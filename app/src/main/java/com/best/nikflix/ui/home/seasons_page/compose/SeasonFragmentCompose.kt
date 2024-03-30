package com.best.nikflix.ui.home.seasons_page.compose

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.best.nikflix.R
import com.best.entity.EpisodeInterface
import com.best.nikflix.ui.home.common.compose.CreateChip
import com.best.nikflix.ui.home.filmography_page.entity.ChipItem

@Composable
fun SeasonFragmentCompose(
    episodeList: List<EpisodeInterface>?,
    selectSeason: Int,
    filmName: String,
    totalSeasons: Int,
    clickChip: (Int) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        //Название сериала
        Text(
            text = filmName,
            fontSize = 20.sp,
            color = colorResource(R.color.blackС),
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 5.dp, top = 10.dp),
            textAlign = TextAlign.Center
        )
        //строка с чипами
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Сезон",
                fontSize = 20.sp,
                color = colorResource(R.color.blackС),
                modifier = Modifier
                    .padding(end = 10.dp),
            )

            //создание списка chipItemList с сезонами
            val chipItemList = mutableListOf<ChipItem>()
            repeat(totalSeasons) { num ->
                chipItemList.add(
                    ChipItem(
                        type = num.toString(),
                        name = (num + 1).toString()
                    )
                )
            }

            //создание чипов с сезонами
            CreateChip(chipItemList, selectSeason) {
                //Log.d("Nik", it)
                clickChip(it.toIntOrNull() ?: 0)
            }
        }
        //Вывод эпизодов
        CreateEpisodes(episodeList)
    }
}


//вывод  эпизодов
@Composable
fun CreateEpisodes(
    episodeList: List<EpisodeInterface>?
) {
    //Log.d("Nik", "CreateEpisodes" + episodeList.toString())
    Column(
        modifier = Modifier
            .padding(top = 15.dp)
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
    ) {
        //if (episodeList?.isNotEmpty() == true) {
        //Log.d("Nik", "${episodeList}")
        Text(
            text = "${episodeList?.get(0)?.seasonNumber} cезон, ${episodeList?.size} серий",
            fontSize = 18.sp,
            color = Color.Gray,
        )
        //}

        episodeList?.forEach { episode ->
            Text(
                text = ("${episode.episodeNumber} серия. " + episode.nameRu),
                fontSize = 18.sp,
                color = Color.Black,
                modifier = Modifier
                    .padding(top = 15.dp)
            )
            Text(
                text = "${
                    if (!episode.releaseDate.isNullOrEmpty())
                        episode.releaseDate
                    else " - "
                }",
                fontSize = 18.sp,
                color = Color.Gray,//colorResource(R.color.gray_dark),
                modifier = Modifier
                    .padding(top = 2.dp)
            )
        }
    }
}


