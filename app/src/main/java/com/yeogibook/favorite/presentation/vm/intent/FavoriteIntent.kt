package com.yeogibook.favorite.presentation.vm.intent

import com.yeogibook.abcmm.data.entity.BookDocumentDiData
import com.yeogibook.favorite.presentation.constants.FavoriteSorts

sealed class FavoriteIntent {
    data object OpenKeyIn : FavoriteIntent()
    data class Sort(val sort: FavoriteSorts) : FavoriteIntent()
    data class OpenDetail(val origin: BookDocumentDiData) : FavoriteIntent()
}