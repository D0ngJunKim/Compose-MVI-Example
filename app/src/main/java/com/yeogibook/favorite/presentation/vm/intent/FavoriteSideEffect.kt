package com.yeogibook.favorite.presentation.vm.intent

import com.yeogibook.abcmm.data.entity.BookDocumentDiData

sealed class FavoriteSideEffect {
    data class OpenKeyIn(val query: String) : FavoriteSideEffect()
    data class OpenDetail(val origin: BookDocumentDiData) : FavoriteSideEffect()
}