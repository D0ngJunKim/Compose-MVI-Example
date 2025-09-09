package com.yeogibook.abcmm.data.request

import androidx.annotation.CallSuper
import com.yeogibook.abcmm.data.request.interceptor.AuthInterceptor
import com.yeogibook.network.AbstractRequest
import com.yeogibook.network.interceptor.RetryInterceptor
import okhttp3.OkHttpClient


abstract class BaseRequest<RESPONSE> : AbstractRequest<RESPONSE>() {

    @CallSuper
    override fun setupOkHttp(builder: OkHttpClient.Builder): OkHttpClient.Builder {
        return builder
            .addInterceptor(AuthInterceptor())
    }
}