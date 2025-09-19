package com.yeogibook.abcmm.data.request

import android.webkit.URLUtil
import androidx.annotation.CallSuper
import com.google.gson.GsonBuilder
import com.yeogibook.abcmm.data.request.interceptor.AuthInterceptor
import com.yeogibook.abcmm.presentation.service.BaseLoadParams
import com.yeogibook.network.util.RetrofitCacheManager
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit


abstract class BaseRequest<Params, Response> where Params : BaseLoadParams<Params>, Response : Any {

    fun createCall(page: Int, params: Params?): Call<Response> {
        var baseUrl = domain.trim()
        if (!URLUtil.isNetworkUrl(baseUrl)) {
            baseUrl = "https://$baseUrl"
        }
        if (!baseUrl.endsWith("/")) {
            baseUrl = "$baseUrl/"
        }

        val retrofit = RetrofitCacheManager.getRetrofit(baseUrl, path, ::setupGson, ::setupOkHttp)

        return createCall(retrofit, page, params)
    }

    protected abstract val domain: String
    protected abstract val path: String
    protected abstract fun createCall(retrofit: Retrofit, page: Int, params: Params?): Call<Response>

    protected open fun setupGson(builder: GsonBuilder): GsonBuilder {
        return builder
    }

    @CallSuper
    protected open fun setupOkHttp(builder: OkHttpClient.Builder): OkHttpClient.Builder {
        return builder.addInterceptor(AuthInterceptor())
    }
}