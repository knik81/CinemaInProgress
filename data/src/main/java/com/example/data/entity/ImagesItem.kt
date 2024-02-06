package com.example.data.entity

import com.example.entity.ImagesItemInterface

data class ImagesItem(
    override val imageUrl: String,
    override val previewUrl: String
): ImagesItemInterface
