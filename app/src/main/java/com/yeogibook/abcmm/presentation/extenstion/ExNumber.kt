package com.yeogibook.abcmm.presentation.extenstion

import java.text.NumberFormat
import java.util.Locale

fun Number?.toCommaFormatOrNull(): String? {
    return try {
        NumberFormat.getInstance(Locale.KOREA).format(this)
    } catch (_: Exception) {
        null
    }
}