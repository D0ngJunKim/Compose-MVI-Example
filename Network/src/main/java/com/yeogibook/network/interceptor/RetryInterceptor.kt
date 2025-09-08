package com.yeogibook.network.interceptor

import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

private const val MAX_RETRIES = 3
private const val RETRY_DELAY_MS = 1000L

class RetryInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        var lastException: IOException? = null
        var lastResponse: Response? = null

        for (attempt in 0 until MAX_RETRIES) {
            try {
                lastResponse?.close()
                val response = chain.proceed(chain.request())

                if (response.code() in 500..599) {
                    return response
                }

                lastResponse = response
                retry(attempt)
            } catch (e: IOException) {
                lastException = e
                retry(attempt)
            }
        }

        lastResponse?.close()
        throw lastException ?: IOException()
    }

    private fun retry(attempt: Int) {
        if (attempt < MAX_RETRIES - 1) {
            try {
                Thread.sleep(RETRY_DELAY_MS)
            } catch (e: InterruptedException) {
                Thread.currentThread().interrupt()
                throw IOException("Retry Interrupted", e)
            }
        }
    }
}