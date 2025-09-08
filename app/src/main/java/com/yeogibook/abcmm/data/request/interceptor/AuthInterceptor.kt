package com.yeogibook.abcmm.data.request.interceptor

import okhttp3.Interceptor
import okhttp3.Response


class AuthInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val token = "73d9b197a6cd10d0bc3389080028daf2"

        return chain.proceed(
            chain.request().newBuilder()
                .header("Authorization", "KakaoAK $token")
                .build()
        )
    }
}