package com.yeogibook.abcmm.presentation.service

/**
 * Created by 180842 on 2025. 9. 12..
 */
abstract class BaseLoadParams<Params> {
    abstract val page: Int

    abstract fun increment(): Params
    abstract fun decrement(): Params
}