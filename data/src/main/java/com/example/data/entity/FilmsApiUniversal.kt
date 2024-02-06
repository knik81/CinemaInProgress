package com.example.data.entity

import com.example.entity.FilmsApiUniversalInterface


data class FilmsApiUniversal(
    override val totalPages: Int,
    override val items: List<ItemApiUniversal>,
    override val total: Int
) : FilmsApiUniversalInterface
