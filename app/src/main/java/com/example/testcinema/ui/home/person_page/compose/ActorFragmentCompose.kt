package com.example.testcinema.ui.home.person_page.compose

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.entity.ApiParameters
import com.example.entity.PersonInterface
import com.example.testcinema.R
import com.example.testcinema.entity.RecyclerItem
import com.example.testcinema.ui.home.common.compose.ComposeHorizontalRecycler

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ActorFragmentCompose(
    person: PersonInterface?,
    bestTenFilms: List<RecyclerItem>?,
    allFilms: Int?,
    openAllFilms: () -> Unit,
    openFilm: (Int) -> Unit,
    openPicturePage: (String) -> Unit,
) {
    if (person != null) {
        Column {
            Row {
                GlideImage(
                    model = person.posterUrl,
                    contentDescription = ApiParameters.PERSON.label,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .padding(top = 10.dp)
                        .height(201.dp)
                        .width(146.dp)
                        .clickable() {
                            openPicturePage(person.posterUrl.toString())
                        }

                )
                Column(
                    modifier = Modifier.padding(
                        start = 10.dp
                    )
                ) {
                    //val name: String = person.nameRu ?: person.nameEn ?: "noname"
                    val name: String =
                        if (person.nameRu != null && person.nameRu != "")
                            person.nameRu!!
                        else {
                            if (person.nameEn != null && person.nameEn != "")
                                person.nameEn!!
                            else
                                 ""
                        }


                    Text(
                        text = name,
                        fontSize = 20.sp,
                        color = colorResource(R.color.black),
                    )

                    Text(
                        text = person.profession ?: "",
                        fontSize = 15.sp,
                        color = colorResource(R.color.gray_dark)
                    )
                }
            }

            if (bestTenFilms != null) {
                ComposeHorizontalRecycler(bestTenFilms, "Лучшее") { idFilm ->
                    openFilm(idFilm)
                }
                if (allFilms != null) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically

                    ) {
                        Text(
                            text = ApiParameters.PERSON_FILMS.label,
                            fontSize = 20.sp,
                            color = colorResource(R.color.black),
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "К списку  >",
                            fontSize = 15.sp,
                            color = colorResource(R.color.purple_700),
                            modifier = Modifier.clickable() {
                                openAllFilms()
                            }
                        )
                    }
                    Text(
                        text = "всего фильмов " + allFilms.toString(),
                        fontSize = 15.sp,
                        color = colorResource(R.color.gray_dark),
                        modifier = Modifier
                            .padding(top = 5.dp)
                    )
                }

            }
        }
    }
}


