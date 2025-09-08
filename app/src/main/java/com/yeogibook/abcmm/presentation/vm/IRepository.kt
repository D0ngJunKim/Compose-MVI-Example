package com.yeogibook.abcmm.presentation.vm

interface IRepository {
    fun cancel(apiType: String = "")
    fun cancelAll()
}