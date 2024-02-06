package com.example.entity

interface FilmsApiUniversalInterface {
    val total: Int
    val totalPages: Int
    val items: List<ItemApiUniversalInterface>
}