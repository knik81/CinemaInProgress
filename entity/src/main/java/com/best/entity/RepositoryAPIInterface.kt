package com.best.entity

import androidx.paging.Pager


interface RepositoryAPIInterface {

    fun getPager(type: String, queryParams: QueryParams?, idFilm: String?, imageType: String?): Pager<Int, ItemApiUniversalInterface>
    suspend fun getDataApi(type: String, queryParams: QueryParams?, id: String?): ResultFromApi
}