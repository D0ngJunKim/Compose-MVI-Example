package com.yeogibook.search.result.presentation.service.process

import com.yeogibook.abcmm.presentation.core.LazyItem
import com.yeogibook.search.result.data.constants.SearchResultSorts
import com.yeogibook.search.result.data.entity.SearchResultDiData
import com.yeogibook.search.result.presentation.units.empty.SearchResultEmptyUiItem
import com.yeogibook.search.result.presentation.units.filter.SearchResultFilterUiItem
import com.yeogibook.search.result.presentation.units.intro.SearchResultIntroUiItem
import com.yeogibook.search.result.presentation.units.item.getSearchResultItemUiItem
import com.yeogibook.search.result.presentation.units.result.getSearchResultDashboardUiItem
import com.yeogibook.search.result.presentation.vm.intent.SearchResultIntent

class SearchResultProcess {
    fun processInit(): List<LazyItem<SearchResultIntent>> {
        return arrayListOf(SearchResultIntroUiItem())
    }

    fun processItem(
        page: Int,
        query: String?,
        sort: SearchResultSorts?,
        data: SearchResultDiData?,
    ): List<LazyItem<SearchResultIntent>> {
        val dataList = arrayListOf<LazyItem<SearchResultIntent>>()

        if (page == 1) {
            getSearchResultDashboardUiItem(query, data?.meta)?.run {
                dataList.add(this)
            }
        }

        val itemList = arrayListOf<LazyItem<SearchResultIntent>>()
        data?.documents?.forEach { document ->
            if (document != null) {
                getSearchResultItemUiItem(document).run {
                    itemList.add(this)
                }
            }
        }

        if (page == 1) {
            if (!itemList.isEmpty()) {
                if (sort != null) {
                    itemList.add(0, SearchResultFilterUiItem(sort))
                }
            } else {
                dataList.add(SearchResultEmptyUiItem())
            }
        }

        dataList.addAll(itemList)
        itemList.clear()

        return dataList
    }
}