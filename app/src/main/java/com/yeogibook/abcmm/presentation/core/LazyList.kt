package com.yeogibook.abcmm.presentation.core

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import com.yeogibook.abcmm.presentation.ui.FooterSpacerUiItem
import com.yeogibook.abcmm.presentation.ui.PullToRefresh
import com.yeogibook.abcmm.presentation.ui.PullToRefreshIndicator
import com.yeogibook.abcmm.presentation.ui.rememberPullToRefreshState
import com.yeogibook.abcmm.presentation.vm.BaseViewModel

@Composable
fun <Intent : Any> List(
    listState: LazyListState,
    viewModel: BaseViewModel<*, Intent>,
    items: LazyPagingItems<LazyItem<Intent>>,
    modifier: Modifier = Modifier,
    isUsePullToRefresh: Boolean = true,
    onRefresh: () -> Unit = {},
    contentGap: Dp = 0.dp,
    headerItem: LazyItem<Intent>? = null,
    footerItem: LazyItem<Intent>? = remember { FooterSpacerUiItem() },
) {
    PullToRefreshLayout(isUsePullToRefresh, onRefresh, items) {
        LazyColumn(
            state = listState,
            modifier = modifier
                .fillMaxSize()
                .background(Color.White),
            verticalArrangement = Arrangement.spacedBy(contentGap, Alignment.Top),
            overscrollEffect = null
        ) {
            if (headerItem != null) {
                stickyHeader(
                    key = headerItem.itemKey(),
                    contentType = headerItem::class
                ) {
                    headerItem.BuildStickyItem(viewModel::processIntent)
                }
                item(
                    key = headerItem.itemKey(),
                    contentType = headerItem::class
                ) {
                    headerItem.BuildItem(viewModel::processIntent)
                }
            }

            for (i in 0 until items.itemCount) {
                val item = items.peek(i) ?: continue

                if (item.hasSticky()) {
                    stickyHeader(
                        key = item.itemKey(),
                        contentType = item::class
                    ) {
                        item.BuildStickyItem(viewModel::processIntent)
                    }
                }

                item(
                    key = item.itemKey(),
                    contentType = item::class
                ) {
                    items[i]?.BuildItem(viewModel::processIntent)
                }
            }

            if (footerItem != null) {
                stickyHeader(
                    key = footerItem.itemKey(),
                    contentType = footerItem::class
                ) {
                    footerItem.BuildStickyItem(viewModel::processIntent)
                }
                item(
                    key = footerItem.itemKey(),
                    contentType = footerItem::class
                ) {
                    footerItem.BuildItem(viewModel::processIntent)
                }
            }
        }
    }
}

@Composable
private fun <Intent : Any> PullToRefreshLayout(
    isUsePullToRefresh: Boolean,
    onRefresh: () -> Unit,
    items: LazyPagingItems<LazyItem<Intent>>,
    content: @Composable () -> Unit,
) {
    if (isUsePullToRefresh) {
        val state = rememberPullToRefreshState(maxDragOffset = 80.dp) {
            onRefresh()
            items.refresh()
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