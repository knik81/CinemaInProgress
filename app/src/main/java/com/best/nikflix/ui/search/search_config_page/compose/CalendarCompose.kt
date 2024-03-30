package com.best.nikflix.ui.search.search_config_page.compose

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.best.nikflix.R

@SuppressLint("MutableCollectionMutableState")
@Composable
fun Calendar(
    year: Int,
    clickItem: (Int) -> Unit
) {

    val item = remember {
        mutableStateOf(mutableListOf<Int>())
    }
    repeat(12) {
        item.value.add(year + it - 6)
    }

    val itemNum = remember {
        mutableIntStateOf(6)
    }

    Box(
        modifier = Modifier
            .width(300.dp)
            .height(250.dp)
            .border(2.dp, colorResource(R.color.blackС))
        //.clip(shape = RoundedCornerShape(5.dp))
    ) {
        Column {
            Row(
                modifier = Modifier
                    .padding(top = 17.dp, start = 10.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "${item.value[0]} - ${item.value[11]}",
                    fontSize = 20.sp,
                    color = Color.Blue,
                )
                Box {
                    Image(
                        imageVector = ImageVector.vectorResource(R.drawable.calendar_arrow_left),
                        contentDescription = "",
                        modifier = Modifier
                            .size(40.dp, 30.dp)
                            .clickable {
                                val temp = mutableListOf<Int>()
                                repeat(12) { counter ->
                                    temp.add(item.value[counter] - 12)
                                }
                                item.value = temp
                            },
                    )
                    Image(
                        imageVector = ImageVector.vectorResource(R.drawable.calendar_arrow_right),
                        contentDescription = "",
                        modifier = Modifier
                            .padding(start = 30.dp)
                            .size(40.dp, 30.dp)
                            .clickable {
                                val temp = mutableListOf<Int>()
                                repeat(12) { counter ->
                                    temp.add(item.value[counter] + 12)
                                }
                                item.value = temp
                            },
                    )
                }


            }
            //Log.d("Nik", item.toString())
            Column(modifier = Modifier.padding(top = 10.dp)) {
                repeat(4) { counter1 ->
                    Row {
                        repeat(3) { counter2 ->
                            //вывод годов
                            Text(
                                text = item.value[counter1 * 3 + counter2].toString(),
                                fontSize = 20.sp,
                                color = colorResource(R.color.blackС),
                                modifier = Modifier
                                    .padding(top = 17.dp, start = 40.dp)
                                    .clickable {
                                        itemNum.intValue = counter1 * 3 + counter2
                                        clickItem(item.value[counter1 * 3 + counter2])
                                    }
                                    .background(
                                        if (itemNum.intValue == counter1 * 3 + counter2)
                                            colorResource(R.color.gray_dark)
                                        else
                                            colorResource(R.color.whiteС)
                                    )
                            )
                        }
                    }
                }
            }
        }
    }

}