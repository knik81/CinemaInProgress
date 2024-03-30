package com.best.nikflix.ui.profile.profile_page.compose

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.best.entity.CollectionParameters
import com.best.nikflix.entity.RecyclerItem
import com.best.nikflix.ui.home.common.compose.ComposeHorizontalRecycler

@Composable
//@Preview
fun ProfileCompose(
    filmAndCollectionMap: Map<String, Int>?,
    alreadySawList: List<RecyclerItem>?,
    interestingList: List<RecyclerItem>?,
    clickDeleteCollection: (String) -> Unit,
    createNewCollection: (String) -> Unit,
    openItem: (Int) -> Unit,
    openCollection: (String) -> Unit
) {

    //val itemList = listOf<RecyclerItem>()
    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
    ) {
        if (!alreadySawList.isNullOrEmpty())
            ComposeHorizontalRecycler(
                itemList = alreadySawList,
                label = CollectionParameters.ALREADYSAW.label
            ) { idFilm ->
                openItem(idFilm)
            }

        ProfileCollectionsCompose(
            filmAndCollectionMap,
            { collection ->
                clickDeleteCollection(collection)
            },
            { collectionName -> createNewCollection(collectionName) },
            { collection ->
                //отокрыть все фильмы этой коллекции
                //Log.d("Nik", "openCollection  $collection")
                openCollection(collection)
            }
        )

        if (!interestingList.isNullOrEmpty()) {

            /*
            interestingList.forEach {
                Log.d("Nik", "it  $it")
            }

             */

            ComposeHorizontalRecycler(
                itemList = interestingList,
                label = CollectionParameters.INTERESTING.label
            ) { idFilm ->
                openItem(idFilm)
            }
        }

    }

}