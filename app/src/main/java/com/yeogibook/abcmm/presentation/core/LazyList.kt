package com.yeogibook.abcmm.presentation.core

import androidx.compose.foundation.lazy.LazyListScope

fun <Intent : Any> LazyListScope.lazyList(
    items: List<LazyItem<Intent>>,
    processIntent: (Intent) -> Unit
) {
    items.forEach { item ->
        if (item.hasSticky()) {
            stickyHeader(
                key = item.itemKey(),
                contentType = item::class
            ) {
                item.BuildStickyItem(processIntent)
            }
        }

        item(
            key = item.itemKey(),
            contentType = item::class
        ) {
            item.BuildItem(processIntent)
        }
    }
}