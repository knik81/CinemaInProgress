package com.best.nikflix.ui.search.search_config_page.compose

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.best.nikflix.R
import com.best.nikflix.ui.search.entity.QueryParamsObject

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
//@Preview
fun SearchConfigDateCompose(
    arrowBack: () -> Unit
) {
    Column {
        Row {
            GlideImage(
                model = R.drawable.left_arrow,
                contentDescription = "",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .height(40.dp)
                    .width(40.dp)
                    .clickable {
                        arrowBack()
                    }
            )

            Text(
                text = "Дата",
                fontSize = 20.sp,
                color = colorResource(R.color.blackС),
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 5.dp, top = 10.dp)
                    .clickable(enabled = true) {
                    },
                textAlign = TextAlign.Center
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp)
                .verticalScroll(rememberScrollState()),
        ) {
            Text(
                text = "Искать в период с",
                fontSize = 18.sp,
                color = Color.LightGray,
                modifier = Modifier
                    .padding(start = 10.dp)
                //.clickable(enabled = true) { }
                , textAlign = TextAlign.Start
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Calendar(QueryParamsObject.yearFrom) { year ->
                    QueryParamsObject.yearFrom = year
                    // Log.d("Nik","yearFrom  ${QueryParamsObject.yearFrom}  $year ")
                }
            }

            Text(
                text = "Искать в период до",
                fontSize = 18.sp,
                color = Color.LightGray,
                modifier = Modifier
                    .padding(start = 10.dp, top = 40.dp)
                //.clickable(enabled = true) { }
                , textAlign = TextAlign.Start
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp, bottom = 10.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Calendar(QueryParamsObject.yearTo) { year ->
                    QueryParamsObject.yearTo = year
                    //Log.d("Nik","yearTo ${QueryParamsObject.yearTo}  $year ")
                }
            }
        }

    }
}

