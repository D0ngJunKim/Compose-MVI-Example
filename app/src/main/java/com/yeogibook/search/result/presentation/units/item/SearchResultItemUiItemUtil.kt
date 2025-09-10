package com.yeogibook.search.result.presentation.units.item

import com.yeogibook.abcmm.data.entity.BookDocumentDiData
import com.yeogibook.abcmm.presentation.extenstion.toCommaFormatOrNull
import com.yeogibook.abcmm.presentation.extenstion.toDatePatternOf

fun getSearchResultItemUiItem(data: BookDocumentDiData): SearchResultItemUiItem {
    val saleStatus = if (data.status?.contains("정상") == true) {
        null
    } else {
        data.status
    }
    val originPrice = data.price.toCommaFormatOrNull()?.plus("원")
    val displayPrice = data.salePrice.toCommaFormatOrNull()?.plus("원")

    val subTitleBuilder = StringBuilder()
    if (!data.authors.isNullOrEmpty()) {
        subTitleBuilder.append(data.authors.joinToString(", "))
    }

    if (!data.publisher.isNullOrEmpty()) {
        if (subTitleBuilder.isNotEmpty()) {
            subTitleBuilder.append(" | ")
        }
        subTitleBuilder.append(data.publisher)
    }

    val date = data.datetime.toDatePatternOf("yyyy년 MM월")
    if (!date.isNullOrEmpty()) {
        if (subTitleBuilder.isNotEmpty()) {
            subTitleBuilder.append(" | ")
        }
        subTitleBuilder.append(date)
    }

    return SearchResultItemUiItem(
        imageUrl = data.thumbnail,
        saleStatus = saleStatus,
        title = data.title,
        subTitle = subTitleBuilder.toString(),
        originPrice = originPrice,
        displayPrice = displayPrice,
        origin = data
    )
}