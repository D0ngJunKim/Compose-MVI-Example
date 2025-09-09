package com.yeogibook.search.result.presentation.units.item

import com.yeogibook.abcmm.data.entity.BookDocumentDiData
import com.yeogibook.abcmm.presentation.extenstion.toCommaFormatOrNull

fun getSearchResultItemUiItem(data: BookDocumentDiData?): SearchResultItemUiItem? {
    if (data != null) {
        val originPrice = data.price.toCommaFormatOrNull()?.plus("원")
        val displayPrice = data.salePrice.toCommaFormatOrNull()?.plus("원")

        return SearchResultItemUiItem(
            imageUrl = data.thumbnail,
            saleStatus = data.status,
            bookName = data.title,
            authorName = data.authors?.joinToString(", "),
            originPrice = originPrice,
            displayPrice = displayPrice,
            origin = data
        )
    }
    return null
}