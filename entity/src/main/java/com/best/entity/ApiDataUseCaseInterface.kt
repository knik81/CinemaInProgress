package com.best.entity

import androidx.paging.Pager


interface ApiDataUseCaseInterface {

    fun getPagingData(type: String, queryParams: QueryParams?, id: String?, imageType: String?): Pager<Int, ItemApiUniversalInterface>
    suspend fun getDataApi(type: String, queryParams: QueryParams?,id: String?): ResultFromApi
}