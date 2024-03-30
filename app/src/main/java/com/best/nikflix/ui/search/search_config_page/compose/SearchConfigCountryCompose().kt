package com.best.nikflix.ui.search.search_config_page.compose

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.best.entity.Country
import com.best.nikflix.R
import com.best.nikflix.ui.search.entity.QueryParamsObject

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun SearchConfigCountryFragmentCompose(
    item: List<Country>?,
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
                text = "Страна",
                fontSize = 20.sp,
                color = colorResource(R.color.blackС),
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 5.dp),
                textAlign = TextAlign.Center,
            )
        }

        if (item != null)
            LazyColumn {
                items(item) {
                    Text(
                        text = it.country,
                        fontSize = 20.sp,
                        color = colorResource(R.color.blackС),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 20.dp)
                            .clickable(enabled = true) {
                                QueryParamsObject.countries = it
                                arrowBack()
                            },
                    )
                }

            }
    }





}