package com.best.domain


import com.best.entity.ApiDataUseCaseInterface
import com.best.entity.QueryParams
import com.best.entity.RepositoryAPIInterface
import javax.inject.Inject

class ApiDataUseCase @Inject constructor(
    private val repositoryAPI: RepositoryAPIInterface,
) : ApiDataUseCaseInterface {

    override suspend fun getDataApi(type: String, queryParams: QueryParams?, id: String?) =
        repositoryAPI.getDataApi(type, queryParams, id)

    override fun getPagingData(
        type: String,
        queryParams: QueryParams?,
        id: String?,
        imageType: String?,
    ) =
        repositoryAPI.getPager(type, queryParams, id, imageType)

}