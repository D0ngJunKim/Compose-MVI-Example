package com.yeogibook.abcmm.presentation.core

import androidx.compose.runtime.Composable
import java.util.UUID
import kotlin.uuid.toKotlinUuid

abstract class LazyItem<Intent : Any> {
    open fun itemKey(): Int = UUID.randomUUID().hashCode()
    open fun hasSticky(): Boolean = false

    @Composable
    open fun BuildStickyItem(processIntent: (Intent) -> Unit) {
        // No Op.
    }

    @Composable
    abstract fun BuildItem(processIntent: (Intent) -> Unit)
}