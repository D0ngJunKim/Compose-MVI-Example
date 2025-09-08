package com.yeogibook.network

import android.webkit.URLUtil
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import com.yeogibook.network.constants.ResponseState
import com.yeogibook.network.util.RetrofitCacheManager
import kotlinx.coroutines.CancellationException
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.awaitResponse


abstract class AbstractRequest<RESPONSE> {
    protected abstract fun createCall(
        retrofit: Retrofit,
        params: JsonObject,
        extraData: Any?
    ): Call<RESPONSE>

    abstract val baseUrl: String
    abstract val path: String
    private var call: Call<RESPONSE>? = null

    protected open fun isSuccess(response: Response<RESPONSE>?): Boolean {
        return response?.isSuccessful == true
    }

    protected open fun setupGson(builder: GsonBuilder): GsonBuilder {
        return builder
    }

    protected open fun setupOkHttp(builder: OkHttpClient.Builder): OkHttpClient.Builder {
        return builder
    }

    suspend fun request(params: JsonObject?, extraData: Any? = null): ResponseState<RESPONSE> {
        var baseUrl = baseUrl.trim()
        if (!URLUtil.isNetworkUrl(baseUrl)) {
            baseUrl = "https://$baseUrl"
        }
        if (!baseUrl.endsWith("/")) {
            baseUrl = "$baseUrl/"
        }

        val retrofit = RetrofitCacheManager.getRetrofit(baseUrl, path, ::setupGson, ::setupOkHttp)

        try {
            call = createCall(retrofit, params ?: JsonObject(), extraData)
            val res = call?.awaitResponse()

            if (call?.isCanceled == true) throw CancellationException("Request cancelled by user")

            if (res?.code() == 401) {
                call?.cancel()
                throw CancellationException("Request cancelled due to 401 Unauthorized")
            }

            return if (isSuccess(res)) {
                ResponseState.Success(res?.code(), res?.body())
            } else {
                ResponseState.Error(res?.code(), res?.body())
            }
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            e.printStackTrace()
            return ResponseState.Error(null, null)
        }
    }

    fun cancelApi() {
        call?.cancel()
    }
}