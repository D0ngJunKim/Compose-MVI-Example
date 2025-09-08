package com.yeogibook.network.util

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

private const val CONNECTION_TIMEOUT = 30L
private const val READ_TIMEOUT = 30L
private const val WRITE_TIMEOUT = 30L

object RetrofitCacheManager {
    private val retrofitCache = mutableMapOf<String, Retrofit>()

    fun getRetrofit(
        baseUrl: String,
        path: String,
        setupGson: GsonBuilder.() -> GsonBuilder,
        setupOkHttp: OkHttpClient.Builder.() -> OkHttpClient.Builder
    ): Retrofit {
        val gsonBuilder = GsonBuilder()
            .setupGson()

        val key = baseUrl + path + gsonBuilder.create().toString()
        return retrofitCache.getOrPut(key) {
            Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(
                    OkHttpClient.Builder()
                        .connectTimeout(CONNECTION_TIMEOUT, TimeUnit.SECONDS)
                        .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
                        .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
                        .followRedirects(true)
                        .followSslRedirects(true)
                        .cookieJar(CookieHandler())
                        .setupOkHttp()
                        .build()
                )
                .addConverterFactory(GsonConverterFactory.create(gsonBuilder.create()))
                .build()
        }
    }
}