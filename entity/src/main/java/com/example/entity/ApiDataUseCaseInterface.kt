package com.example.entity

import androidx.paging.Pager


interface ApiDataUseCaseInterface {

    suspend fun getPagingData(type: String, filters: Filters?, id: String?, imageType: String?): Pager<Int, ItemApiUniversalInterface>
    suspend fun getDataApi(type: String, filters: Filters?,id: String?): ResultFromApi
}