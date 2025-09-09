package com.yeogibook.search.result.data.service.usecase

import com.yeogibook.abcmm.data.entity.BookDocumentDiData
import com.yeogibook.abcmm.presentation.core.LazyItem
import com.yeogibook.search.result.presentation.units.empty.SearchResultEmptyUiItem
import com.yeogibook.search.result.presentation.units.item.getSearchResultItemUiItem
import com.yeogibook.search.result.presentation.vm.intent.SearchResultIntent

class SearchResultUseCaseProcess {
    fun processInit(documents: List<BookDocumentDiData?>?): ArrayList<LazyItem<SearchResultIntent>> {
        val itemList = arrayListOf<LazyItem<SearchResultIntent>>()

        documents?.forEach { document ->
            if (document != null) {
                getSearchResultItemUiItem(document)?.run {
                    itemList.add(this)
                }
            }
        }

        if (itemList.isEmpty()) {
            itemList.add(SearchResultEmptyUiItem())
        }

        return itemList
    }
}