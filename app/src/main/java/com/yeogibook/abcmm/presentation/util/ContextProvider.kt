package com.yeogibook.abcmm.presentation.util

import android.annotation.SuppressLint
import android.content.Context

@SuppressLint("StaticFieldLeak")
object ContextProvider {
    private var _context: Context? = null

    val context: Context
        get() = _context ?: throw IllegalStateException("Context not initialized")


    fun setContext(ctx: Context) {
        _context = ctx.applicationContext
    }
}

