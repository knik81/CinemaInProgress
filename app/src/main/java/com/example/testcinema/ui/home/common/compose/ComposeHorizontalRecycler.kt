package com.example.testcinema.ui.home.common.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.testcinema.R
import com.example.testcinema.entity.RecyclerItem


@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ComposeHorizontalRecycler(
    itemList: List<RecyclerItem>,
    label: String,
    openItem: (Int) -> Unit
) {
    Column(
        Modifier
            .background(colorResource(R.color.white))
            .padding(top = 25.dp)
    ) {
        Text(
            text = label,
            fontSize = 20.sp,
            color = colorResource(R.color.black),
            fontWeight = FontWeight.Bold
        )

        Row(
            Modifier
                .background(colorResource(R.color.white))
                .padding(top = 10.dp)
                .horizontalScroll(rememberScrollState())
        ) {
            itemList.forEach { item ->
                Column(
                    Modifier
                        .padding(
                            start = 10.dp,
                            end = 10.dp
                        )
                        .height(250.dp)
                        .width(111.dp)
                ) {
                    //Log.d("Nik","film = $film")

                    Box {
                        GlideImage(
                            model = item.iconUri,
                            contentDescription = "",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .height(156.dp)
                                .clickable(){
                                    openItem(item.id)
                                }
                        )

                        if (item.rating != null) {
                            GlideImage(
                                model = R.drawable.frame_rating,
                                contentDescription = "",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .width(28.dp)
                                    .height(20.dp)
                                    .padding(top = 5.dp, start = 3.dp)
                            )
                            Text(
                                text = item.rating.toString(),
                                fontSize = 12.sp,
                                color = colorResource(R.color.white),
                                modifier = Modifier
                                    .padding(top = 5.dp, start = 7.dp)
                            )

                        }

                    }

                    Text(
                        text = item.name,
                        fontSize = 12.sp,
                        color = colorResource(R.color.black),
                        modifier = Modifier
                            .padding(top = 3.dp)
                    )

                    Text(
                        text = item.genre,
                        fontSize = 10.sp,
                        color = colorResource(R.color.black),
                        modifier = Modifier
                            .padding(top = 3.dp)
                    )
                }
            }
        }

    }
}