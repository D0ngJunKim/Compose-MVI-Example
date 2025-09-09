package com.yeogibook.abcmm.presentation.core

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.yeogibook.abcmm.presentation.ui.FooterSpacerUiItem
import com.yeogibook.abcmm.presentation.ui.PullToRefresh
import com.yeogibook.abcmm.presentation.ui.PullToRefreshIndicator
import com.yeogibook.abcmm.presentation.ui.rememberPullToRefreshState
import com.yeogibook.abcmm.presentation.vm.BaseViewModel

fun <Intent : Any> LazyListScope.lazyList(
    items: List<LazyItem<Intent>>,
    processIntent: (Intent) -> Unit,
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

@Composable
fun <Intent : Any> List(
    listState: LazyListState,
    viewModel: BaseViewModel<*, *, Intent>,
    modifier: Modifier = Modifier,
    isUsePullToRefresh: Boolean = false,
    contentGap: Dp = 0.dp,
    headerItem: LazyItem<Intent>? = null,
    footerItem: LazyItem<Intent>? = remember { FooterSpacerUiItem() },
) {
    val dataList = viewModel.viewState.collectAsState().value?.dataList ?: emptyList()

    PullToRefreshLayout(isUsePullToRefresh, viewModel) {
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

            lazyList(
                items = dataList,
                processIntent = viewModel::processIntent
            )

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
private fun PullToRefreshLayout(
    isUsePullToRefresh: Boolean,
    viewModel: BaseViewModel<*, *, *>,
    content: @Composable () -> Unit,
) {
    if (isUsePullToRefresh) {
        val state = rememberPullToRefreshState(maxDragOffset = 80.dp) {
            viewModel.refreshData()
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