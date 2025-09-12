package com.yeogibook.abcmm.presentation.service

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.yeogibook.abcmm.presentation.core.LazyItem
import retrofit2.Call
import retrofit2.awaitResponse

/**
 * Created by 180842 on 2025. 9. 12..
 */
abstract class BaseDataSource<Params : BaseLoadParams<Params>, Response : Any, Intent : Any> :
    PagingSource<Params, LazyItem<Intent>>() {

    override fun getRefreshKey(state: PagingState<Params, LazyItem<Intent>>): Params? {
        return null
    }

    override suspend fun load(params: LoadParams<Params>): LoadResult<Params, LazyItem<Intent>> {
        try {
            val requestParams = params.key
            val preRequest = onPreRequest(requestParams)
            if (preRequest != null) {
                return preRequest
            }

            val page = requestParams?.page ?: 1
            val response = getCall(page, requestParams).awaitResponse()

            if (response.isSuccessful) {
                val result = response.body()
                val hasNext = hasNextPage(result)

                return LoadResult.Page(
                    data = onParse(requestParams, page, result),
                    prevKey = if (page == 1) null else requestParams?.decrement(),
                    nextKey = if (hasNext) requestParams?.increment() else null
                )
            } else {
                throw Exception("Api Error")
            }
        } catch (e: Exception) {
            return LoadResult.Error(e)
        }
    }

    protected open fun onPreRequest(params: Params?): LoadResult<Params, LazyItem<Intent>>? {
        return null
    }

    abstract fun getCall(page: Int, params: Params?): Call<Response>

    abstract fun hasNextPage(response: Response?): Boolean

    abstract fun onParse(params: Params?, page: Int, response: Response?): List<LazyItem<Intent>>
}