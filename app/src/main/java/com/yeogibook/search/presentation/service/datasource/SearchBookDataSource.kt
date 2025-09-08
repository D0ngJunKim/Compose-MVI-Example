package com.yeogibook.search.presentation.service.datasource

import com.google.gson.JsonObject
import com.yeogibook.abcmm.presentation.vm.IDataSource
import com.yeogibook.network.constants.ResponseState
import com.yeogibook.search.data.entity.SearchBookDiData
import com.yeogibook.search.data.request.ReqSearchBook
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class SearchBookDataSource : IDataSource {
    private val request = ReqSearchBook()

    fun requestApi(params: JsonObject): Flow<ResponseState<SearchBookDiData>> {
        return flow {
            emit(request.request(params))
        }.flowOn(Dispatchers.IO)
    }

    override fun cancelApi() {
        request.cancelApi()
    }
}