package com.best.data.entity

import com.best.entity.FilmsApiUniversalInterface


data class FilmsApiUniversal(
    override val totalPages: Int,
    override var items: List<ItemApiUniversal>,
    override val total: Int
) : FilmsApiUniversalInterface
