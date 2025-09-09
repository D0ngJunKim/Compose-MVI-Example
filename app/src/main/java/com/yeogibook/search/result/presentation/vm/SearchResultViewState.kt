package com.yeogibook.search.result.presentation.vm

import com.yeogibook.abcmm.presentation.core.LazyItem
import com.yeogibook.abcmm.presentation.vm.BaseViewState
import com.yeogibook.search.result.presentation.vm.intent.SearchResultIntent

data class SearchResultViewState(
    override val dataList: List<LazyItem<SearchResultIntent>>,
) : BaseViewState<SearchResultIntent>()