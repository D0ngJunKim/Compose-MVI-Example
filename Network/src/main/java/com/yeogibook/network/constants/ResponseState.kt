package com.yeogibook.network.constants

sealed class ResponseState<RESPONSE> {
    abstract val data: RESPONSE?

    data class Success<RESPONSE>(
        val resultCode: Int?,
        override val data: RESPONSE?,
    ) : ResponseState<RESPONSE>()

    data class Error<RESPONSE>(
        val resultCode: Int?,
        override val data: RESPONSE?,
    ) : ResponseState<RESPONSE>()
}