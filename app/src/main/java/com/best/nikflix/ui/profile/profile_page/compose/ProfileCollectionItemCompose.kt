package com.best.nikflix.ui.profile.profile_page.compose

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.best.nikflix.R

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
//@Preview
fun ProfileCollectionItemCompose(
    label: String,
    count: Int,
    icon: Int,
    closeEnabled: Boolean,
    closeClick: (String) -> Unit,
    click: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .width(146.dp)
            .height(146.dp)
            .padding(10.dp)
            .border(2.dp, colorResource(R.color.gray_dark))
            .clip(shape = RoundedCornerShape(10.dp))
            .fillMaxSize()
            .clickable {
                click(label)
            }

    ) {
        if (closeEnabled)
            GlideImage(
                model = R.drawable.x,
                contentDescription = "",
                modifier = Modifier
                    .padding(0.dp)
                    .width(28.dp)
                    .height(30.dp)
                    .align(Alignment.End)
                    .clickable {
                        closeClick(label)
                    }
            )

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            GlideImage(
                model = icon,
                contentDescription = "",
                modifier = Modifier
                    .width(28.dp)
                    .height(20.dp)
                    .padding(bottom = 5.dp)
            )
            Text(text = label,
                color = colorResource(R.color.blackС))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(alignment = Alignment.CenterHorizontally)
            ) {
                GlideImage(
                    model = R.drawable.frame_rating,
                    contentDescription = "",
                    modifier = Modifier
                        .width(28.dp)
                        .height(20.dp)
                        .align(Center)
                )
                Text(
                    text = count.toString(),
                    fontSize = 12.sp,
                    color = colorResource(R.color.whiteС),
                    textAlign = TextAlign.End,
                    modifier = Modifier
                        .align(Center)
                )
            }
        }
    }

}