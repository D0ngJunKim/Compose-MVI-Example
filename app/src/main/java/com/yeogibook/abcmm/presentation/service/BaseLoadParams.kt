package com.yeogibook.abcmm.presentation.service


abstract class BaseLoadParams<Params> {
    abstract val page: Int

    abstract fun increment(): Params
    abstract fun decrement(): Params
}