package com.example.data.entity

import com.example.entity.StaffsItemInterface

data class StaffsItem(
    override val staffId: Int,
    override val nameRu: String,
    override val nameEn: String,
    override val description: String?,
    override val posterUrl: String,
    override val professionText: String,
    override val professionKey: String
): StaffsItemInterface
