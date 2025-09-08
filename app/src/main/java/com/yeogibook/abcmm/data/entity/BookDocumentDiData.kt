package com.yeogibook.abcmm.data.entity

import com.google.gson.annotations.SerializedName

data class BookDocumentDiData(
    val title: String?,                 // 도서 제목
    val contents: String?,              // 도서 소개
    val url: String?,                   // 도서 상세 URL
    val isbn: String?,                  // ISBN 10 or ISBN 13, 두 값이 모두 제공될 경우 공백으로 구분
    val datetime: String?,              // 도서 출판날짜, ISO 8601 형식
    val authors: List<String?>?,        // 도서 저자 리스트
    val publisher: String?,             // 도서 출판사
    val translators: List<String?>?,    // 도서 번역자 리스트
    val price: Int?,                    // 도서 정가
    @SerializedName("sale_price")
    val salePrice: Int?,                // 도서 판매가
    val thumbnail: String?,             // 도서 표지 미리보기 URL
    val status: String?                 // 도서 판매 상태
)