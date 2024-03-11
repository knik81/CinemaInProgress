package com.best.nikflix.entity

data class Recycler(
    val type: String? = null,
    val totalItemsFromApi: Int? = null,
    val itemList: List<RecyclerItem>?
)
