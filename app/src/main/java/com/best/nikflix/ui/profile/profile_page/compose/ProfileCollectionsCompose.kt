package com.best.nikflix.ui.profile.profile_page.compose

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.best.nikflix.R
import com.best.entity.CollectionParameters
import com.best.nikflix.ui.profile.profile_page.entity.Item

@SuppressLint("MutableCollectionMutableState")
@Composable
fun ProfileCollectionsCompose(
    filmAndCollectionMap: Map<String, Int>?,
    clickDeleteCollection: (String) -> Unit,
    createNewCollection: (String) -> Unit,
    openCollection: (String) -> Unit
) {
    //для открытия диалога
    val openAlertDialog = remember { mutableStateOf(false) }
    when {
        openAlertDialog.value -> {
            DialogCompose(
                isOpenAlertDialog = true
            ) { collectionName ->
                openAlertDialog.value = false
                if (collectionName != "")
                    createNewCollection(collectionName)
            }
        }
    }
    Text(
        text = "Коллекции",
        fontSize = 20.sp,
        color = colorResource(R.color.blackС),
        fontWeight = FontWeight.Bold
    )

    Text(
        text = "+   Создать свою коллекцию",
        fontSize = 17.sp,
        color = colorResource(R.color.blackС),
        modifier = Modifier
            .padding(top = 20.dp)
            .clickable {
                openAlertDialog.value = true
            }

    )


    //отрисовка обязательных квадратиков с коллекциями
    Row {
        ProfileCollectionItemCompose(
            label = CollectionParameters.LIKE.label,
            count = filmAndCollectionMap?.get(CollectionParameters.LIKE.label) ?: 0,
            icon = R.drawable.like_black,
            closeEnabled = false,
            {},
            { collection ->
                //отокрыть все фильмы этой коллекции
                openCollection(collection)
            })
        ProfileCollectionItemCompose(
            label = CollectionParameters.WANTTOSEE.label,
            count = filmAndCollectionMap?.get(CollectionParameters.WANTTOSEE.label) ?: 0,
            icon = R.drawable.want_see_black,
            closeEnabled = false,
            {},
            { collection ->
                //отокрыть все фильмы этой коллекции
                openCollection(collection)
            })

    }


    val itemList = remember {
        mutableStateOf(mutableListOf<Item>())
    }
    val itemList2 = mutableListOf<Item>()
    filmAndCollectionMap?.forEach {
        if (it.key != CollectionParameters.LIKE.label
            && it.key != CollectionParameters.ALREADYSAW.label
            && it.key != CollectionParameters.WANTTOSEE.label
            && it.key != CollectionParameters.INTERESTING.label
        )
            itemList2.add(
                Item(
                    collection = it.key,
                    qty = it.value
                )
            )
    }
    itemList.value = itemList2


    //отрисовка пользовательских квадратиков с коллекциями
    LazyRow {
        items(itemList.value) { item ->
            ProfileCollectionItemCompose(
                label = item.collection,
                count = item.qty,
                icon = R.drawable.icon_profile,
                closeEnabled = true,
                { collection ->
                    //удалить коллекцию
                    clickDeleteCollection(collection)
                },
                { collection ->
                    //отокрыть все фильмы этой коллекции
                    openCollection(collection)
                }
            )
        }
    }
}


