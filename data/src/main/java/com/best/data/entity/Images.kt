package com.best.data.entity

import com.best.entity.ImagesInterface


data class Images(
     override val total: Int,
     override val totalPages: Int,
     override val items: List<ImagesItem>
):ImagesInterface