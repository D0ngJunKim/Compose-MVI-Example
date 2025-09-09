package com.yeogibook.abcmm.presentation.ui

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.yeogibook.abcmm.presentation.core.LazyItem

@Composable
fun NavigationSizeSpacer(space: Dp) {
    Spacer(
        modifier = Modifier.height(
            WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding() + space
        )
    )
}

class SpacerUiItem<Intent : Any>(private val space: Dp) : LazyItem<Intent>() {
    @Composable
    override fun BuildItem(processIntent: (Intent) -> Unit) {
        Spacer(
            modifier = Modifier.height(space)
        )
    }
}

class FooterSpacerUiItem<Intent : Any>() : LazyItem<Intent>() {
    @Composable
    override fun BuildItem(processIntent: (Intent) -> Unit) {
        NavigationSizeSpacer(space = 100.dp)
    }
}