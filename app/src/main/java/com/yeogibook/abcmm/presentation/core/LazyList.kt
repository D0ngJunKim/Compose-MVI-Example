package com.yeogibook.abcmm.presentation.core

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.UiComposable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import com.yeogibook.abcmm.presentation.ui.FooterSpacerUiItem
import com.yeogibook.abcmm.presentation.ui.PullToRefresh
import com.yeogibook.abcmm.presentation.ui.PullToRefreshIndicator
import com.yeogibook.abcmm.presentation.ui.rememberPullToRefreshState
import com.yeogibook.abcmm.presentation.vm.BaseViewModel

private const val COLUMN_ON_TABLET = 4
private const val COLUMN_ON_MOBILE = 2

enum class ListSpan(private val mobileSpanCount: Int, private val tabletSpanCount: Int) {
    FULL_FOR_ALL(COLUMN_ON_MOBILE, COLUMN_ON_TABLET),
    FULL_ON_MOBILE_HALF_ON_TABLET(COLUMN_ON_MOBILE, COLUMN_ON_TABLET / 2),
    SINGLE_FOR_ALL(1, 1);

    companion object {
        fun getColumn(isTablet: Boolean): GridCells {
            return GridCells.Fixed(if (isTablet) COLUMN_ON_TABLET else COLUMN_ON_MOBILE)
        }
    }

    fun getSpanCount(isTablet: Boolean): Int {
        return if (isTablet) tabletSpanCount else mobileSpanCount
    }
}

@Composable
fun <Intent : Any> List(
    state: LazyGridState,
    viewModel: BaseViewModel<*, Intent>,
    items: LazyPagingItems<LazyItem<Intent>>,
    modifier: Modifier = Modifier,
    isUsePullToRefresh: Boolean = true,
    onRefresh: () -> Unit = {},
    edgePadding: Dp = 16.dp,
    spacingPadding: Dp = 10.dp,
    headerItem: LazyItem<Intent>? = null,
    footerItem: LazyItem<Intent>? = remember { FooterSpacerUiItem() },
) {
    ListImpl(
        state = state,
        viewModel = viewModel,
        peekItem = { index -> items.peek(index) },
        getItem = { index -> items[index] },
        itemCount = { items.itemCount },
        modifier = modifier,
        isUsePullToRefresh = isUsePullToRefresh,
        onRefresh = {
            items.refresh()
            onRefresh()
        },
        headerItem = headerItem,
        footerItem = footerItem,
        edgePadding = edgePadding,
        spacingPadding = spacingPadding
    )
}


@Composable
fun <Intent : Any> List(
    state: LazyGridState,
    viewModel: BaseViewModel<*, Intent>,
    items: List<LazyItem<Intent>>,
    modifier: Modifier = Modifier,
    isUsePullToRefresh: Boolean = true,
    onRefresh: () -> Unit = {},
    edgePadding: Dp = 16.dp,
    spacingPadding: Dp = 10.dp,
    headerItem: LazyItem<Intent>? = null,
    footerItem: LazyItem<Intent>? = remember { FooterSpacerUiItem() },
) {
    ListImpl(
        state = state,
        viewModel = viewModel,
        peekItem = { index -> items.getOrNull(index) },
        itemCount = { items.size },
        modifier = modifier,
        isUsePullToRefresh = isUsePullToRefresh,
        onRefresh = onRefresh,
        headerItem = headerItem,
        footerItem = footerItem,
        edgePadding = edgePadding,
        spacingPadding = spacingPadding
    )
}


@Composable
private fun <Intent : Any> ListImpl(
    state: LazyGridState,
    viewModel: BaseViewModel<*, Intent>,
    peekItem: (Int) -> LazyItem<Intent>?,
    itemCount: () -> Int,
    modifier: Modifier = Modifier,
    isUsePullToRefresh: Boolean = true,
    onRefresh: () -> Unit = {},
    edgePadding: Dp = 0.dp,
    spacingPadding: Dp = 0.dp,
    getItem: (Int) -> LazyItem<Intent>? = peekItem,
    headerItem: LazyItem<Intent>? = null,
    footerItem: LazyItem<Intent>? = remember { FooterSpacerUiItem() },
) {
    val isTablet by rememberUpdatedState(isTablet())

    PullToRefreshLayout(
        isUsePullToRefresh,
        onRefresh = {
            onRefresh()
        }) {
        LazyVerticalGrid(
            columns = ListSpan.getColumn(isTablet),
            state = state,
            modifier = modifier
                .fillMaxSize()
                .background(Color.White),
            contentPadding = PaddingValues(horizontal = edgePadding),
            horizontalArrangement = Arrangement.spacedBy(spacingPadding),
            overscrollEffect = null,
        ) {
            if (headerItem != null) {
                if (headerItem.hasSticky()) {
                    stickyHeader(
                        key = headerItem.itemKey(),
                        contentType = headerItem::class
                    ) {
                        StickyLayout(edgePadding) {
                            headerItem.BuildStickyItem(viewModel::processIntent)
                        }
                    }
                }

                item(
                    key = headerItem.itemKey(),
                    contentType = headerItem::class,
                    span = { GridItemSpan(headerItem.span.getSpanCount(isTablet)) }
                ) {
                    headerItem.BuildItem(viewModel::processIntent)
                }
            }

            for (i in 0 until itemCount()) {
                val item = peekItem(i) ?: continue

                if (item.hasSticky()) {
                    stickyHeader(
                        key = item.itemKey(),
                        contentType = item::class
                    ) {
                        StickyLayout(edgePadding) {
                            item.BuildStickyItem(viewModel::processIntent)
                        }
                    }
                }

                item(
                    key = item.itemKey(),
                    contentType = item::class,
                    span = { GridItemSpan(item.span.getSpanCount(isTablet)) }

                ) {
                    getItem(i)?.BuildItem(viewModel::processIntent)
                }
            }

            if (footerItem != null) {
                if (footerItem.hasSticky()) {
                    stickyHeader(
                        key = footerItem.itemKey(),
                        contentType = footerItem::class
                    ) {
                        StickyLayout(edgePadding) {
                            footerItem.BuildStickyItem(viewModel::processIntent)
                        }
                    }
                }
                item(
                    key = footerItem.itemKey(),
                    contentType = footerItem::class,
                    span = { GridItemSpan(footerItem.span.getSpanCount(isTablet)) }

                ) {
                    footerItem.BuildItem(viewModel::processIntent)
                }
            }
        }
    }
}

@Composable
private fun StickyLayout(
    edgePadding: Dp,
    content: @Composable @UiComposable () -> Unit,
) {
    Layout(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White),
        content = content
    ) { measurables, constraints ->
        val placeable = measurables[0].measure(constraints.copy(minWidth = 0, minHeight = 0))

        layout(placeable.width + (edgePadding * 2).roundToPx(), placeable.height) {
            placeable.placeRelative(edgePadding.roundToPx(), 0)
        }
    }
}

@Composable
private fun PullToRefreshLayout(
    isUsePullToRefresh: Boolean,
    onRefresh: () -> Unit,
    content: @Composable () -> Unit,
) {
    if (isUsePullToRefresh) {
        val state = rememberPullToRefreshState(maxDragOffset = 80.dp) {
            onRefresh()
        }
        PullToRefresh(
            refreshIndicator = {
                PullToRefreshIndicator(this)
            },
            state = state,
            modifier = Modifier.fillMaxSize(),
            content = content
        )
    } else {
        content()
    }
}