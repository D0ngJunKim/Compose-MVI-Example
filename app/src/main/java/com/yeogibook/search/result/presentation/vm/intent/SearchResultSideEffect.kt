package com.yeogibook.search.result.presentation.vm.intent

import com.yeogibook.abcmm.data.entity.BookDocumentDiData

sealed class SearchResultSideEffect {
    data class OpenKeyIn(val query: String) : SearchResultSideEffect()
    data class OpenDetail(val origin: BookDocumentDiData) : SearchResultSideEffect()
}