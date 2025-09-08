package com.yeogibook.search.data.entity

import com.google.gson.annotations.SerializedName
import com.yeogibook.abcmm.data.entity.BookDocumentDiData

data class SearchBookDiData(
    val meta: SearchBookMetaDiData?,
    val documents: List<BookDocumentDiData?>?
)

data class SearchBookMetaDiData(
    @SerializedName("is_end")
    val isEnd: Boolean,
    @SerializedName("total_count")
    val totalCount: Int?
)