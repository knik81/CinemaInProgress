package com.best.nikflix.ui.search.search_page.compose.recycler

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.best.nikflix.R
import com.best.nikflix.entity.RecyclerItem

@SuppressLint("MutableCollectionMutableState")
@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun SearchFragmentComposeElement(
    recyclerItem: RecyclerItem,
    onClick: (Int) -> Unit
) {
    Column {
        Row(
            modifier = Modifier.padding(top = 10.dp, bottom = 10.dp)
        ) {

            //Log.d("Nik", recyclerItem.toString())

            Box {
                GlideImage(
                    model = recyclerItem.iconUri,
                    contentDescription = "",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .height(156.dp)
                        .width(106.dp)
                        .clickable {
                            onClick(recyclerItem.id)
                        }
                )
                if(recyclerItem.alreadySaw == true){
                    GlideImage(
                        model = R.drawable.alfa,
                        contentDescription = "",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .height(156.dp)
                            .width(106.dp)
                            .clickable {
                                onClick(recyclerItem.id)
                            }
                    )
                }

                if (recyclerItem.rating != null) {
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
                        text = recyclerItem.rating.toString(),
                        fontSize = 12.sp,
                        color = colorResource(R.color.whiteС),
                        modifier = Modifier
                            .padding(top = 5.dp, start = 7.dp)
                    )
                }
            }
            Column(
                Modifier.padding(top = 40.dp, start = 5.dp)
            ) {
                Text(
                    text = recyclerItem.name,
                    fontSize = 15.sp,
                    color = colorResource(R.color.blackС),
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .fillMaxWidth()
                )

                Text(
                    text = recyclerItem.genre,
                    fontSize = 12.sp,
                    color = colorResource(R.color.gray_very_dark),
                    modifier = Modifier
                        .fillMaxWidth()
                )
            }

        }
    }

}