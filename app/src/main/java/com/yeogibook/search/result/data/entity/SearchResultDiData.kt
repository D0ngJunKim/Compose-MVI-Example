package com.yeogibook.search.result.data.entity

import com.google.gson.annotations.SerializedName
import com.yeogibook.abcmm.data.entity.BookDocumentDiData

data class SearchResultDiData(
    val meta: SearchResultMetaDiData?,
    val documents: List<BookDocumentDiData?>?
)

data class SearchResultMetaDiData(
    @SerializedName("is_end")
    val isEnd: Boolean,
    @SerializedName("total_count")
    val totalCount: Int?
)