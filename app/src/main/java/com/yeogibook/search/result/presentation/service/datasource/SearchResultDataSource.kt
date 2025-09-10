package com.yeogibook.search.result.presentation.service.datasource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.yeogibook.abcmm.presentation.core.LazyItem
import com.yeogibook.search.result.data.constants.SearchResultSorts
import com.yeogibook.search.result.presentation.service.SearchResultService
import com.yeogibook.search.result.presentation.service.process.SearchResultProcess
import com.yeogibook.search.result.presentation.vm.intent.SearchResultIntent
import retrofit2.awaitResponse

class SearchResultDataSource(
    private val service: SearchResultService,
    private val query: String?,
    private val sort: SearchResultSorts
) : PagingSource<Int, LazyItem<SearchResultIntent>>() {
    private val process = SearchResultProcess()

    override fun getRefreshKey(state: PagingState<Int, LazyItem<SearchResultIntent>>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            return state.anchorPosition?.let { pos ->
                state.closestPageToPosition(pos)?.prevKey?.plus(1)
                    ?: state.closestPageToPosition(pos)?.nextKey?.minus(1)
            }
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, LazyItem<SearchResultIntent>> {
        try {
            if (query.isNullOrEmpty()) {
                return LoadResult.Page(
                    data = process.processInit(),
                    prevKey = null,
                    nextKey = null
                )
            }
            val page = params.key ?: 1
            val response = service.getSearchResult(
                page = page,
                query = query,
                sort = sort.typeCd,
                size = 40
            ).awaitResponse()

            if (response.isSuccessful) {
                val result = response.body()
                val meta = result?.meta
                val hasNext = !(meta == null || meta.isEnd == true)

                return LoadResult.Page(
                    data = process.processItem(query, sort, result),
                    prevKey = if (page == 1) null else page - 1,
                    nextKey = if (hasNext) page + 1 else null
                )
            } else {
                throw Exception("")
            }
        } catch (e: Exception) {
            return LoadResult.Error(e)
        }
    }
}