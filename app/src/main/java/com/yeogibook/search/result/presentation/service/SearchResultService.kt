package com.yeogibook.search.result.presentation.service

import com.yeogibook.abcmm.data.request.constants.Params
import com.yeogibook.abcmm.data.request.constants.YeogiBookDomain
import com.yeogibook.abcmm.data.request.interceptor.AuthInterceptor
import com.yeogibook.abcmm.presentation.service.BaseLoadParams
import com.yeogibook.network.util.RetrofitCacheManager
import com.yeogibook.search.result.data.constants.SearchResultSorts
import com.yeogibook.search.result.data.entity.SearchResultDiData
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import javax.inject.Singleton

