package com.yeogibook.search.result.presentation.vm.intent

import com.yeogibook.abcmm.data.entity.BookDocumentDiData
import com.yeogibook.search.result.data.constants.SearchResultSorts

sealed class SearchResultIntent {
    data object OpenKeyIn : SearchResultIntent()
    data class Sort(val sort: SearchResultSorts) : SearchResultIntent()
    data class OpenDetail(val origin: BookDocumentDiData) : SearchResultIntent()
}