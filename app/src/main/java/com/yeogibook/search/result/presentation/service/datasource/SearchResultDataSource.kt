package com.yeogibook.search.result.presentation.service.datasource

import com.yeogibook.abcmm.presentation.core.LazyItem
import com.yeogibook.abcmm.presentation.service.BaseDataSource
import com.yeogibook.search.result.data.entity.SearchResultDiData
import com.yeogibook.search.result.presentation.service.SearchResultLoadParams
import com.yeogibook.search.result.presentation.service.SearchResultService
import com.yeogibook.search.result.presentation.service.process.SearchResultProcess
import com.yeogibook.search.result.presentation.vm.intent.SearchResultIntent
import retrofit2.Call

class SearchResultDataSource(
    private val service: SearchResultService,
) : BaseDataSource<SearchResultLoadParams, SearchResultDiData, SearchResultIntent>() {
    private val process = SearchResultProcess()

    override fun onPreRequest(params: SearchResultLoadParams?): LoadResult<SearchResultLoadParams, LazyItem<SearchResultIntent>>? {
        if (params?.query.isNullOrEmpty()) {
            return LoadResult.Page(
                data = process.processInit(),
                prevKey = null,
                nextKey = null
            )
        }
        return null
    }

    override fun getCall( 
        page: Int,
        params: SearchResultLoadParams?,
    ): Call<SearchResultDiData> {
        return service.getSearchResult(
            page = page,
            query = params?.query,
            sort = params?.sort?.typeCd,
            size = 40
        )
    }

    override fun hasNextPage(response: SearchResultDiData?): Boolean {
        val meta = response?.meta
        return !(meta == null || meta.isEnd)
    }

    override fun onParse(
        params: SearchResultLoadParams?,
        page: Int,
        response: SearchResultDiData?,
    ): List<LazyItem<SearchResultIntent>> {
        return process.processItem(page, params?.query, params?.sort, response)
    }
}