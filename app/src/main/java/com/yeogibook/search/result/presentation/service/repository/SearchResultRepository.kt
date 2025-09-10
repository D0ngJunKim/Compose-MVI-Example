package com.yeogibook.search.result.presentation.service.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.yeogibook.search.result.data.constants.SearchResultSorts
import com.yeogibook.search.result.presentation.service.SearchResultService
import com.yeogibook.search.result.presentation.service.datasource.SearchResultDataSource

class SearchResultRepository(private val apiService: SearchResultService) {
    fun getSearchResults(
        query: String?,
        sort: SearchResultSorts
    ) = Pager(
        config = PagingConfig(
            pageSize = 20,
            prefetchDistance = 10
        ),
        pagingSourceFactory = { SearchResultDataSource(apiService, query, sort) }
    ).flow
}