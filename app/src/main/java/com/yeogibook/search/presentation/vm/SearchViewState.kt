package com.yeogibook.search.presentation.vm

import com.yeogibook.abcmm.presentation.core.LazyItem

data class SearchViewState(
    val dataList: List<LazyItem<SearchIntent>> = emptyList()
)