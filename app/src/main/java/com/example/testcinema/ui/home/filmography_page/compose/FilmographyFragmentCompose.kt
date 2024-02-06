package com.example.testcinema.ui.home.filmography_page.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.testcinema.R
import org.intellij.lang.annotations.JdkConstants.HorizontalAlignment

@Composable
fun FilmographyFragmentCompose(
    label: String,
    name: String
) {

    Column(
        modifier = Modifier
            .padding(10.dp)
            .fillMaxWidth(),
        verticalArrangement = Arrangement.Center
    ) {
        Text(

            //HorizontalAlignment = Alignment.CenterHorizontally,
            text = label,
            fontSize = 20.sp,
            color = colorResource(R.color.black),
            fontWeight = FontWeight.Bold
        )
        Text(
            text = name,
            fontSize = 25.sp,
            color = colorResource(R.color.black),
            fontWeight = FontWeight.Bold
        )

        CreateFilterChip()

    }
}

//рисует чипы
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateFilterChip() {

    val itemsList = listOf("Android", "iOS", "Windows", "MAC", "Linux")

    var selectedItem by remember { mutableStateOf(itemsList[0]) }

    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        items(itemsList) { item ->
            FilterChip(
                modifier = Modifier
                    .padding(end = 10.dp),
                selected = (item == selectedItem),
                onClick = { selectedItem = item },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = Color(R.color.purple_700)
                ),
                label = { Text(item) },
            )
        }
    }
}





