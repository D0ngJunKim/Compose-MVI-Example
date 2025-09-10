package com.yeogibook.search.result.data.constants

enum class SearchResultSorts(val typeCd: String, val typeName: String) {
    ACCURACY("accuracy", "정확도순"),
    LATEST("latest", "발간일순")
}