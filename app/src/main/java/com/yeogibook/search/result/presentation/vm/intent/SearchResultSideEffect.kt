package com.yeogibook.search.result.presentation.vm.intent

sealed class SearchResultSideEffect {
    data class OpenKeyIn(val query: String) : SearchResultSideEffect()
}