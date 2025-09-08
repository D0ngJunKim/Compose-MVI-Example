package com.yeogibook.abcmm.presentation.ui.ds.token

import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import com.yeogibook.R

object TypographyToken {
    val Gothic = FontFamily(
        Font(R.font.gothic_bold, FontWeight.Bold, FontStyle.Normal),
        Font(R.font.gothic_semibold, FontWeight.SemiBold, FontStyle.Normal),
        Font(R.font.gothic_medium, FontWeight.Medium, FontStyle.Normal),
        Font(R.font.gothic_regular, FontWeight.Normal, FontStyle.Normal)
    )
}