package com.yeogibook.search.result.data.request

import com.yeogibook.abcmm.data.request.BaseRequest
import com.yeogibook.abcmm.data.request.constants.Params
import com.yeogibook.abcmm.data.request.constants.YeogiBookDomain
import com.yeogibook.abcmm.presentation.service.BaseLoadParams
import com.yeogibook.search.result.data.constants.SearchResultSorts
import com.yeogibook.search.result.data.entity.SearchResultDiData
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Query


data object SearchResultRequest : BaseRequest<SearchResultLoadParams, SearchResultDiData>() {
    override val domain: String
        get() = YeogiBookDomain.Kakao
    override val path: String
        get() = Service.PATH

    override fun createCall(
        retrofit: Retrofit,
        page: Int,
        params: SearchResultLoadParams?,
    ): Call<SearchResultDiData> {
        return retrofit.create(Service::class.java).getSearchResult(
            query = params?.query,
            sort = params?.sort?.typeCd,
            page = page,
            size = 40
        )
    }

    private interface Service {
        companion object {
            const val PATH = "v3/search/book"
        }

        @GET(PATH)
        fun getSearchResult(
            @Query(Params.QUERY) query: String?,
            @Query(Params.SORT) sort: String?,
            @Query(Params.PAGE) page: Int?,
            @Query(Params.SIZE) size: Int?,
        ): Call<SearchResultDiData>
    }
}


data class SearchResultLoadParams(
    override val page: Int,
    val query: String?,
    val sort: SearchResultSorts?,
) : BaseLoadParams<SearchResultLoadParams>() {

    override fun increment(): SearchResultLoadParams {
        return copy(page = page + 1)
    }

    override fun decrement(): SearchResultLoadParams {
        return copy(page = page - 1)
    }
}