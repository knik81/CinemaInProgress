package com.best.nikflix.ui.home.common.compose

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.best.nikflix.ui.home.filmography_page.entity.ChipItem


//рисует чипы
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateChip(
    cipItemList: List<ChipItem>,
    selectSeason: Int = 0,
    onClick: (String) -> Unit
) {

    var selectedItem by remember {
        mutableStateOf(
            if (cipItemList.isNotEmpty()) {
                onClick(cipItemList[selectSeason].type)
                cipItemList[selectSeason]
            } else ""
        )
    }

    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        items(cipItemList) { professionKey ->
            // Log.d("Nik", selectedItem.toString())

            FilterChip(
                modifier = Modifier
                    .padding(end = 10.dp),
                selected = (professionKey == selectedItem),
                onClick = {
                    if (selectedItem != professionKey)
                        onClick(professionKey.type)
                    selectedItem = professionKey
                },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = Color.Blue
                ),
                label = { Text(professionKey.name) },
            )
        }
    }
}