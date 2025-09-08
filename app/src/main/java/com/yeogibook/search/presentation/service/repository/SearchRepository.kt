package com.yeogibook.search.presentation.service.repository

import com.google.gson.JsonObject
import com.yeogibook.abcmm.presentation.vm.IRepository
import com.yeogibook.network.constants.ResponseState
import com.yeogibook.search.data.entity.SearchBookDiData
import com.yeogibook.search.presentation.service.datasource.SearchBookDataSource
import kotlinx.coroutines.flow.Flow

class SearchRepository(private val dataSource: SearchBookDataSource) : IRepository {

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