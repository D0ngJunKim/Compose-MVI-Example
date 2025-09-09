package com.yeogibook.search.result.data.service.datasource

import com.google.gson.JsonObject
import com.yeogibook.abcmm.presentation.vm.IDataSource
import com.yeogibook.network.constants.ResponseState
import com.yeogibook.search.result.data.entity.SearchBookDiData
import com.yeogibook.search.result.data.request.ReqSearchResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class SearchResultDataSource : IDataSource {
    private val request = ReqSearchResult()

    fun requestApi(params: JsonObject): Flow<ResponseState<SearchBookDiData>> {
        return flow {
            emit(request.request(params))
        }.flowOn(Dispatchers.IO)
    }

    override fun cancelApi() {
        request.cancelApi()
    }
}