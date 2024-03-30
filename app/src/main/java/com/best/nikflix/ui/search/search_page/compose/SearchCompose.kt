package com.best.nikflix.ui.search.search_page.compose

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.best.entity.FilmAndCollectionF
import com.best.entity.ItemApiUniversalInterface
import com.best.entity.QueryParams
import com.best.nikflix.R
import com.best.nikflix.ui.home.common.MapperApiToUi
import com.best.nikflix.ui.search.entity.QueryParamsObject
import com.best.nikflix.ui.search.search_page.compose.recycler.SearchFragmentComposeElement


@SuppressLint("MutableCollectionMutableState")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchFragmentCompose(
    lazyPagingItems: LazyPagingItems<ItemApiUniversalInterface>,
    searchTextList: List<String>?,
    alreadySaw: List<FilmAndCollectionF>?,
    searchFilm: (QueryParams) -> Unit,
    clickFilm: (Int) -> Unit,
    openSearchConfigPage: () -> Unit,
    saveSearchText: (String) -> Unit
) {


    // Log.d("Nik", alreadySaw.toString())

    //ранее искомый текст
    val rememberSearchText = remember {
        mutableListOf<String>()
    }

    //состояние поиска для вывода состояния на экран
    val stateSearch = remember {
        mutableStateOf(false)
    }

    //текст поиска
    val searchText = remember {
        mutableStateOf("")
    }

    //список подсказок из ранее введенного текста с фильтром по тексту поиска
    val helpList = remember {
        mutableStateOf(mutableListOf<String>())
    }

    //загрузка текстом поиска из БД
    if (!searchTextList.isNullOrEmpty()) {
        rememberSearchText.clear()
        rememberSearchText.addAll(searchTextList)
    }


    //показывать/скрывать подсказки
    val isActive = remember {
        mutableStateOf(false)
    }


    //перезаполнение выпадающего списка подсказок
    rememberSearchText.filter {
        helpList.value.clear()
        it.lowercase().startsWith(searchText.value.lowercase())
    }.forEach {
        helpList.value.add(it)
    }

    //строка поиска
    Column(
        Modifier.fillMaxSize()
    ) {
        SearchBar(
            modifier = Modifier
                .fillMaxWidth()

            //.background(colorResource(R.color.gray_background))
            ,
            query = searchText.value,
            onQueryChange = { text -> searchText.value = text },

            onSearch = { textInput ->
                isActive.value = false

                //удалить пробелы в конце текста
                //мешает поиску
                val textSearch = textInput.trimEnd()

                //сохранить текст поиска в БД
                if (textSearch != "")
                    saveSearchText(textSearch)

                //запрос в апи
                searchFilm(
                    QueryParams(
                        genres = QueryParamsObject.genres,
                        countries = QueryParamsObject.countries,
                        order = QueryParamsObject.order,
                        type = QueryParamsObject.type,
                        ratingFrom = QueryParamsObject.ratingFrom,
                        ratingTo = QueryParamsObject.ratingTo,
                        yearFrom = QueryParamsObject.yearFrom,
                        yearTo = QueryParamsObject.yearTo,
                        imdbId = "",
                        keyword = textSearch
                    )
                )
                stateSearch.value = true
            },
            placeholder = {
                Text(text = "название фильма...",
                   // color = colorResource(R.color.gray_background)
                )
            },
            active = isActive.value,
            onActiveChange = {
                isActive.value = it
            },
            //иконка - увеличилка
            leadingIcon = {
                Icon(imageVector = Icons.Default.Search, contentDescription = "Search icon")
            },
            //смена иконок:  - крестик / ключик
            trailingIcon = {
                if (isActive.value) {
                    //иконка крестик - очитска
                    Icon(
                        modifier = Modifier.clickable {
                            if (searchText.value.isNotEmpty()) {
                                //фильтрование подсказок
                                helpList.value.clear()
                                searchText.value = ""

                                //перезаполнение выпадающего списка подсказок
                                helpList.value.clear()
                                rememberSearchText.forEach {
                                    helpList.value.add(it)
                                }
                            } else {
                                isActive.value = false
                            }
                        },
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close icon"
                    )
                } else {
                    //иконка ключик - переход в настройку поиска
                    Icon(
                        modifier = Modifier.clickable { openSearchConfigPage() },
                        imageVector = Icons.Default.Build,
                        contentDescription = "Config icon"
                    )
                }
            })
        {

            //отображение ранее введенного текста
            LazyColumn{
                items(helpList.value) {
                    Box(modifier = Modifier
                        //.background(colorResource(R.color.gray_background))
                        .clickable {
                            //выбор подмказки и добавлениее текста в строку поиска
                            searchText.value = it

                            //перезаполнение выпадающего списка подсказок
                            helpList.value.clear()

                            rememberSearchText
                                .filter { rememberText ->
                                    rememberText
                                        .lowercase()
                                        .startsWith(it.lowercase())
                                }
                                .forEach { helpList.value.add(it) }
                        }
                        .fillMaxWidth()
                        .padding(10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = it, color = colorResource(R.color.blackС))
                    }
                }
            }
        }


        when (val state = lazyPagingItems.loadState.refresh) {
            //обработка ошибки загрузки из апи
            is LoadState.Error -> {
                stateSearch.value = false
                Text(
                    text = state.error.message ?: "Ошибка в SearchFragmentCompose",
                    fontSize = 20.sp,
                    color = colorResource(R.color.blackС),
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 30.dp),
                    textAlign = TextAlign.Center
                )
            }

            //состояние загрузки из апи
            is LoadState.Loading -> {
                if (stateSearch.value) {
                    Text(
                        text = "Поиск",
                        fontSize = 20.sp,
                        color = colorResource(R.color.blackС),
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 30.dp),
                        textAlign = TextAlign.Center
                    )
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
            }


            //состояние "не грузится"
            is LoadState.NotLoading -> {
                //если есть данные, вывод на экран
                if (lazyPagingItems.itemCount > 0) {
                    //Log.d("Nik","lazyPagingItems")
                    LazyColumn {
                        items(lazyPagingItems.itemCount) { index ->
                            val item = MapperApiToUi.mapperItem(lazyPagingItems[index])

                            if (item != null) {

                                //проверка отметки "уже видел" и обновление
                                item.alreadySaw = !alreadySaw?.filter {
                                    item.id.toString() == it.filmId
                                }.isNullOrEmpty()

                                //отрисовка эдемента
                                SearchFragmentComposeElement(
                                    recyclerItem = item
                                ) {
                                    clickFilm(it)
                                }
                            }
                        }
                    }
                    stateSearch.value = false
                } else {
                    //если данных не пришло
                    if (stateSearch.value)
                        Text(
                            text = "Ничего не найдено",
                            fontSize = 20.sp,
                            color = colorResource(R.color.blackС),
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 30.dp),
                            textAlign = TextAlign.Center
                        )
                }
            }


        }
    }
}



