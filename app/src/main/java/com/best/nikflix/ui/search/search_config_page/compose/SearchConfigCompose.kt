package com.best.nikflix.ui.search.search_config_page.compose

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.RangeSlider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.best.entity.SEARCH
import com.best.nikflix.R
import com.best.nikflix.ui.search.entity.QueryParamsObject
import com.best.nikflix.ui.search.entity.Screen
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun SearchConfigCompose(
    changeScreen: (Screen) -> Unit
) {

    Text(
        text = "Настройа поиска",
        fontSize = 20.sp,
        color = colorResource(R.color.blackС),
        fontWeight = FontWeight.Bold,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 5.dp),
        textAlign = TextAlign.Center
    )

    //Содержимое экрана
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 40.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = "Показывать",
            fontSize = 14.sp,
            color = colorResource(R.color.gray_background),
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 5.dp, top = 30.dp),
        )

        //фильтр по типу фильма
        val type = remember {
            mutableStateOf(QueryParamsObject.type)
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {

            FilterChip(
                modifier = Modifier
                    .fillMaxWidth(.3f),
                selected = (type.value == SEARCH.TYPE.ALL.name),
                onClick = {
                    type.value = SEARCH.TYPE.ALL.name
                    QueryParamsObject.type = SEARCH.TYPE.ALL.name
                },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = Color.Blue
                ),
                label = {
                    Text(
                        text = SEARCH.TYPE.ALL.label,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                }
            )
            FilterChip(
                modifier = Modifier.fillMaxWidth(.6f),
                selected = (type.value == SEARCH.TYPE.FILM.name),
                onClick = {
                    type.value = SEARCH.TYPE.FILM.name
                    QueryParamsObject.type = SEARCH.TYPE.FILM.name
                },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = Color.Blue
                ),
                label = {
                    Text(
                        text = SEARCH.TYPE.FILM.label,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                }
            )
            FilterChip(
                modifier = Modifier.fillMaxWidth(),
                selected = (type.value == SEARCH.TYPE.TV_SERIES.name),
                onClick = {
                    type.value = SEARCH.TYPE.TV_SERIES.name
                    QueryParamsObject.type = SEARCH.TYPE.TV_SERIES.name
                },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = Color.Blue
                ),
                label = {
                    Text(
                        text = SEARCH.TYPE.TV_SERIES.label,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                }
            )
        }

//_________________________________________
        //фильтр - Страна
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 5.dp, top = 30.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Страна",
                fontSize = 20.sp,
                color = colorResource(R.color.gray_background),
            )
            ClickableText(
                text = AnnotatedString(QueryParamsObject.countries.country),
                onClick = { changeScreen(Screen.Country) },
                style = TextStyle(
                    fontSize = 18.sp,
                    color = colorResource(R.color.gray_dark),
                )
            )
        }
        Divider(
            modifier = Modifier.padding(top = 5.dp),
            color = colorResource(R.color.gray_background),
            thickness = 2.dp
        )

//_________________________________________
        //фильтр - Жанр
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 5.dp, top = 30.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Жанр",
                fontSize = 20.sp,
                color = colorResource(R.color.gray_background),
            )

            Text(
                text = QueryParamsObject.genres.genre,
                fontSize = 18.sp,
                color = colorResource(R.color.gray_dark),
                modifier = Modifier.clickable { changeScreen(Screen.Genre) }
            )
        }

        Divider(
            modifier = Modifier.padding(top = 5.dp),
            color = colorResource(R.color.gray_background),
            thickness = 2.dp
        )

//_________________________________________
        //фильтр - Дата
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 5.dp, top = 30.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Год",
                fontSize = 20.sp,
                color = colorResource(R.color.gray_background),
            )

            Text(
                text = "с ${QueryParamsObject.yearFrom} по ${QueryParamsObject.yearTo}",
                fontSize = 18.sp,
                color = colorResource(R.color.gray_dark),
                modifier = Modifier
                    .clickable {
                        changeScreen(Screen.Date)
                    }
            )
        }

        Divider(
            modifier = Modifier.padding(top = 5.dp),
            color = colorResource(R.color.gray_background),
            thickness = 2.dp
        )

//_________________________________________
        //фильтр - рейтинг
        val sliderPosition = remember {
            mutableStateOf(
                QueryParamsObject.ratingFrom.toFloat() / 10f..
                        QueryParamsObject.ratingTo.toFloat() / 10f
            )
        }
        //Log.d("Nik", QueryParamsObject.ratingFrom.toString())
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 30.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Рейтинг",
                fontSize = 20.sp,
                color = colorResource(R.color.gray_background),
            )
            Text(
                text = if (sliderPosition.value == (0f..1f))
                    "любой" else
                    "${(sliderPosition.value.start * 10).roundToInt()} .. " +
                            "${(sliderPosition.value.endInclusive * 10).roundToInt()}",
                fontSize = 18.sp,
                color = colorResource(R.color.gray_dark),
            )
        }
        RangeSlider(
            value = sliderPosition.value,
            steps = 10,
            onValueChange = { range ->
                sliderPosition.value = range
                QueryParamsObject.ratingFrom = (range.start * 10).roundToInt()
                QueryParamsObject.ratingTo = (range.endInclusive * 10).roundToInt()
            },
            valueRange = 0f..1f,
            onValueChangeFinished = {},
        )

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = (sliderPosition.value.start * 10).roundToInt().toString(),
                fontSize = 18.sp,
                color = colorResource(R.color.gray_dark),
            )
            Text(
                text = (sliderPosition.value.endInclusive * 10).roundToInt().toString(),
                fontSize = 18.sp,
                color = colorResource(R.color.gray_dark),
            )
        }
        Divider(
            modifier = Modifier.padding(top = 5.dp),
            color = colorResource(R.color.gray_very_dark),
            thickness = 2.dp
        )

//_________________________________________
        Text(
            text = "Сортировать",
            fontSize = 14.sp,
            color = colorResource(R.color.gray_background),
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 5.dp, top = 30.dp),
        )
        //сортировка списка
        val order = remember {
            mutableStateOf(QueryParamsObject.order)
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 20.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {

            FilterChip(
                modifier = Modifier
                    .fillMaxWidth(.3f),
                selected = (order.value == SEARCH.ORDER.YEAR.name),
                onClick = {
                    order.value = SEARCH.ORDER.YEAR.name
                    QueryParamsObject.order = order.value
                },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = Color.Blue
                ),
                label = {
                    Text(
                        text = SEARCH.ORDER.YEAR.label,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                }
            )
            FilterChip(
                modifier = Modifier.fillMaxWidth(.6f),
                selected = (order.value == SEARCH.ORDER.NUM_VOTE.name),
                onClick = {
                    order.value = SEARCH.ORDER.NUM_VOTE.name
                    QueryParamsObject.order = order.value
                },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = Color.Blue
                ),
                label = {
                    Text(
                        text = SEARCH.ORDER.NUM_VOTE.label,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                }
            )
            FilterChip(
                modifier = Modifier.fillMaxWidth(),
                selected = (order.value == SEARCH.ORDER.RATING.name),
                onClick = {
                    order.value = SEARCH.ORDER.RATING.name
                    QueryParamsObject.order = order.value
                },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = Color.Blue
                ),
                label = {
                    Text(
                        text = SEARCH.ORDER.RATING.label,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                }
            )
        }
    }

}

