package com.yeogibook.search.result.presentation.units.result

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import com.yeogibook.abcmm.presentation.extenstion.toCommaFormatOrNull
import com.yeogibook.search.result.data.entity.SearchResultMetaDiData

fun getSearchResultDashboardUiItem(
    query: String?,
    data: SearchResultMetaDiData?,
): SearchResultDashboardUiItem? {
    if (query.isNullOrEmpty()) return null

    val annotatedString = buildAnnotatedString {
        withStyle(
            SpanStyle(
                color = Color.Red,
                fontWeight = FontWeight.Medium
            )
        ) {
            append("'$query'")
        }
        append(" 검색결과 총 ")
        withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
            append("${data?.totalCount.toCommaFormatOrNull() ?: 0}건")
        }
    }

    return SearchResultDashboardUiItem(annotatedString)
}