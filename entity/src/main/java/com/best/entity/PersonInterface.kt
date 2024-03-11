package com.best.entity

interface PersonInterface {
    val personId: Int?
    val webUrl: String?
    val nameRu: String?
    val nameEn: String?
    val posterUrl: String?
    val profession: String?
    val films: List<ItemApiUniversalInterface>
}