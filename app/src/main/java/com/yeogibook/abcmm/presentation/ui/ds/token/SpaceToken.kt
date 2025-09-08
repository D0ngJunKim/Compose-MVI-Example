package com.yeogibook.abcmm.presentation.ui.ds.token

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp

enum class SpaceToken(val dp: Dp) {
    _0(0.dp),
    _1(1.dp),
    _2(2.dp),
    _4(4.dp),
    _8(8.dp),
    _10(10.dp),
    _12(12.dp),
    _16(16.dp),
    _32(32.dp),
    _52(52.dp)
}

@Stable
fun SpaceTokenValues(
    all: SpaceToken
): SpaceTokenValues = SpaceTokenValuesImpl(all, all, all, all)

@Stable
fun SpaceTokenValues(
    horizontal: SpaceToken = SpaceToken._0,
    vertical: SpaceToken = SpaceToken._0
): SpaceTokenValues =
    SpaceTokenValuesImpl(horizontal, vertical, horizontal, vertical)

@Stable
fun SpaceTokenValues(
    start: SpaceToken = SpaceToken._0,
    top: SpaceToken = SpaceToken._0,
    end: SpaceToken = SpaceToken._0,
    bottom: SpaceToken = SpaceToken._0
): SpaceTokenValues = SpaceTokenValuesImpl(start, top, end, bottom)

@Stable
interface SpaceTokenValues : PaddingValues

@Immutable
private class SpaceTokenValuesImpl(
    @Stable val start: SpaceToken = SpaceToken._0,
    @Stable val top: SpaceToken = SpaceToken._0,
    @Stable val end: SpaceToken = SpaceToken._0,
    @Stable val bottom: SpaceToken = SpaceToken._0,
) : SpaceTokenValues {

    init {
        check(
            (start.dp.value >= 0f) and (top.dp.value >= 0f) and (end.dp.value >= 0f) and (bottom.dp.value >= 0f)
        ) {
            "Padding must be non-negative"
        }
    }

    override fun calculateLeftPadding(layoutDirection: LayoutDirection) =
        if (layoutDirection == LayoutDirection.Ltr) start.dp else end.dp

    override fun calculateTopPadding() = top.dp

    override fun calculateRightPadding(layoutDirection: LayoutDirection) =
        if (layoutDirection == LayoutDirection.Ltr) end.dp else start.dp

    override fun calculateBottomPadding() = bottom.dp

    override fun equals(other: Any?): Boolean {
        if (other !is SpaceTokenValuesImpl) return false
        return start.dp == other.start.dp &&
                top.dp == other.top.dp &&
                end.dp == other.end.dp &&
                bottom.dp == other.bottom.dp
    }

    override fun hashCode() =
        ((start.dp.hashCode() * 31 + top.dp.hashCode()) * 31 + end.dp.hashCode()) * 31 + bottom.dp.hashCode()

    override fun toString() = "SpaceTokenValues(start=$start, top=$top, end=$end, bottom=$bottom)"
}