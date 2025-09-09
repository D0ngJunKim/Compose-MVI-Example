package com.yeogibook.search.result.data.service.repository

import com.google.gson.JsonObject
import com.yeogibook.abcmm.presentation.vm.IRepository
import com.yeogibook.network.constants.ResponseState
import com.yeogibook.search.result.data.entity.SearchBookDiData
import com.yeogibook.search.result.data.service.datasource.SearchResultDataSource
import kotlinx.coroutines.flow.Flow

class SearchResultRepository(private val dataSource: SearchResultDataSource) : IRepository {

    fun requestApi(params: JsonObject): Flow<ResponseState<SearchBookDiData>> {
        return dataSource.requestApi(params)
    }

    override fun cancel(apiType: String) {
        cancelAll()
    }

    override fun cancelAll() {
        dataSource.cancelApi()
    }
}