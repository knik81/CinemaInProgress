package com.example.data.entity

import com.example.entity.PremiersSimilarsApiInterface


data class PremiersSimilarsApi(
    override val total: Int,
    override val items: List<ItemApiUniversal>
) : PremiersSimilarsApiInterface {
}