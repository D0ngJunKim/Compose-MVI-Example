package com.yeogibook.search.result.presentation.vm.intent

sealed class SearchResultIntent {
    data object OpenKeyIn : SearchResultIntent()
}