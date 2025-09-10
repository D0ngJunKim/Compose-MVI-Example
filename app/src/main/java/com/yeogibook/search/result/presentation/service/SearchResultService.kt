package com.yeogibook.search.result.presentation.service

import com.yeogibook.abcmm.data.request.constants.Params
import com.yeogibook.abcmm.data.request.constants.YeogiBookDomain
import com.yeogibook.abcmm.data.request.interceptor.AuthInterceptor
import com.yeogibook.network.util.RetrofitCacheManager
import com.yeogibook.search.result.data.entity.SearchResultDiData
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class SearchResultServiceProvider {

    @Singleton
    @Provides
    fun provideRetrofitInstance(): SearchResultService =
        RetrofitCacheManager.getRetrofit(
            baseUrl = YeogiBookDomain.Kakao,
            path = SearchResultService.PATH,
            setupGson = { this },
            setupOkHttp = { this.addInterceptor(AuthInterceptor()) }
        ).create(SearchResultService::class.java)
}

interface SearchResultService {
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