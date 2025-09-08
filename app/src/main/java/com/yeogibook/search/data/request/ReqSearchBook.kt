package com.yeogibook.search.data.request

import com.google.gson.JsonObject
import com.yeogibook.abcmm.data.request.BaseRequest
import com.yeogibook.abcmm.data.request.constants.Params
import com.yeogibook.abcmm.data.request.constants.YeogiBookDomain
import com.yeogibook.abcmm.presentation.extenstion.getAsIntOrNull
import com.yeogibook.abcmm.presentation.extenstion.getAsStringOrNull
import com.yeogibook.search.data.entity.SearchBookDiData
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Query

class ReqSearchBook : BaseRequest<SearchBookDiData>() {
    companion object {
        private const val PATH = "v3/search/book"
    }

    private interface Service {
        @GET(PATH)
        fun createCall(
            @Query(Params.QUERY) query: String?,
            @Query(Params.SORT) sort: String?,
            @Query(Params.PAGE) page: Int?,
            @Query(Params.SIZE) size: Int?,
            @Query(Params.TARGET) target: String?
        ): Call<SearchBookDiData>
    }

    override fun createCall(
        retrofit: Retrofit,
        params: JsonObject,
        extraData: Any?
    ): Call<SearchBookDiData> {
        return retrofit.create(Service::class.java).createCall(
            query = params.getAsStringOrNull(Params.QUERY),
            sort = params.getAsStringOrNull(Params.SORT),
            page = params.getAsIntOrNull(Params.PAGE),
            size = params.getAsIntOrNull(Params.SIZE),
            target = params.getAsStringOrNull(Params.TARGET)
        )
    }

    override val baseUrl: String = YeogiBookDomain.Kakao
    override val path: String = PATH
}