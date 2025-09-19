package com.yeogibook.search.result.presentation.service.datasource

import com.yeogibook.abcmm.presentation.core.LazyItem
import com.yeogibook.abcmm.presentation.service.BaseDataSource
import com.yeogibook.search.result.data.entity.SearchResultDiData
import com.yeogibook.search.result.data.request.SearchResultLoadParams
import com.yeogibook.search.result.data.request.SearchResultRequest
import com.yeogibook.search.result.presentation.service.process.SearchResultProcess
import com.yeogibook.search.result.presentation.vm.intent.SearchResultIntent

class SearchResultDataSource : BaseDataSource<SearchResultLoadParams, SearchResultDiData, SearchResultIntent>() {
    override val request: SearchResultRequest = SearchResultRequest

    override fun onPreRequest(params: SearchResultLoadParams?): LoadResult<SearchResultLoadParams, LazyItem<SearchResultIntent>>? {
        if (params?.query.isNullOrEmpty()) {
            return LoadResult.Page(
                data = SearchResultProcess.processInit(),
                prevKey = null,
                nextKey = null
            )
        }
        return null
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
        return SearchResultProcess.processItem(page, params?.query, params?.sort, response)
    }
}