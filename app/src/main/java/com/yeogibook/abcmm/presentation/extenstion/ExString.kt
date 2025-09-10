package com.yeogibook.abcmm.presentation.extenstion

import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

fun String?.toDatePatternOf(pattern: String): String? {
    if (this.isNullOrEmpty()) return null
    return try {
        val date = OffsetDateTime.parse(this)
        val formatter = DateTimeFormatter.ofPattern(pattern, Locale.KOREAN)

        date.format(formatter)
    } catch (e: Exception) {
        null
    }
}