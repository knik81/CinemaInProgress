package com.best.data.entity

import com.best.entity.PremiersSimilarsApiInterface

data class PremiersSimilarsApi(
    override val total: Int,
    override val items: List<ItemApiUniversal>
) : PremiersSimilarsApiInterface
