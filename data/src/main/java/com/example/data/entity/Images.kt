package com.example.data.entity

import com.example.entity.ImagesInterface

data class Images(
     override val total: Int,
     override val totalPages: Int,
     override val items: List<ImagesItem>
):ImagesInterface