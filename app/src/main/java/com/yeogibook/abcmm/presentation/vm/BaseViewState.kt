package com.yeogibook.abcmm.presentation.vm

import com.yeogibook.abcmm.presentation.core.LazyItem

abstract class BaseViewState<Intent : Any> {
    abstract val dataList: List<LazyItem<Intent>>
}