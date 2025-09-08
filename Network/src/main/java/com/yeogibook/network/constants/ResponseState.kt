package com.yeogibook.network.constants

sealed class ResponseState<RESPONSE> {
    data class Success<RESPONSE>(
        val resultCode: Int?,
        val data: RESPONSE?
    ) : ResponseState<RESPONSE>()

    data class Error<RESPONSE>(
        val resultCode: Int?,
        val data: RESPONSE?
    ) : ResponseState<RESPONSE>()
}