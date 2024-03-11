package com.best.data.entity

import com.best.entity.ImagesItemInterface

data class ImagesItem(
    override val imageUrl: String,
    override val previewUrl: String
): ImagesItemInterface
