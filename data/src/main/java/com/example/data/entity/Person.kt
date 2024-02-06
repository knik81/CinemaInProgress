package com.example.data.entity

import com.example.entity.PersonInterface

data class Person(
    override val personId: Int,
    override val webUrl: String,
    override val nameRu: String,
    override val nameEn: String,
    override val posterUrl: String,
    override val profession: String,
    override val films: List<ItemApiUniversal>
): PersonInterface
