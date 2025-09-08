package com.yeogibook.abcmm.presentation.ui.ds

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Stable
import androidx.compose.ui.Modifier
import com.yeogibook.abcmm.presentation.ui.ds.token.SpaceToken
import com.yeogibook.abcmm.presentation.ui.ds.token.SpaceTokenValues

@Stable
fun Modifier.padding(
    start: SpaceToken = SpaceToken._0,
    top: SpaceToken = SpaceToken._0,
    end: SpaceToken = SpaceToken._0,
    bottom: SpaceToken = SpaceToken._0
) = padding(
    start = start.dp,
    top = top.dp,
    end = end.dp,
    bottom = bottom.dp
)

@Stable
fun Modifier.padding(
    horizontal: SpaceToken = SpaceToken._0,
    vertical: SpaceToken = SpaceToken._0
) = padding(
    horizontal = horizontal.dp,
    vertical = vertical.dp
)

@Stable
fun Modifier.padding(
    all: SpaceToken = SpaceToken._0
) = padding(
    all = all.dp
)

@Stable
fun Modifier.padding(
    spaceTokenValues: SpaceTokenValues
) = padding(spaceTokenValues)