package com.example.domain


import com.example.entity.ApiDataUseCaseInterface
import com.example.entity.Filters
import com.example.entity.RepositoryAPIInterface
import javax.inject.Inject

class ApiDataUseCase @Inject constructor(
    private val repositoryAPI: RepositoryAPIInterface,
) : ApiDataUseCaseInterface
{

    override suspend fun getDataApi(type: String, filters: Filters?, id: String?) =
        repositoryAPI.getDataApi(type, filters, id)

    override suspend fun getPagingData(
        type: String,
        filters: Filters?,
        id: String?,
        imageType: String?,
    ) =
        repositoryAPI.getPagingData(type, filters, id, imageType)

}