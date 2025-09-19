package com.yeogibook.search.result.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.yeogibook.abcmm.presentation.core.AppState
import com.yeogibook.abcmm.presentation.core.List
import com.yeogibook.abcmm.presentation.core.NavigationRoute
import com.yeogibook.abcmm.presentation.core.rememberAppState
import com.yeogibook.abcmm.presentation.ui.ds.CircularProgressIndicator
import com.yeogibook.abcmm.presentation.ui.onClick
import com.yeogibook.abcmm.presentation.vm.observeSideEffects
import com.yeogibook.search.keyin.presentation.SearchKeyInExtra
import com.yeogibook.search.keyin.presentation.SearchKeyInScreen
import com.yeogibook.search.result.presentation.units.header.SearchResultHeaderUiItem
import com.yeogibook.search.result.presentation.vm.SearchResultViewModel
import com.yeogibook.search.result.presentation.vm.intent.SearchResultSideEffect

@Composable
fun SearchResultScreen(
    appState: AppState,
    listState: LazyGridState = rememberLazyGridState(),
    viewModel: SearchResultViewModel = hiltViewModel(),
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val items = viewModel.uiFlow.collectAsLazyPagingItems()
    val query: String? by viewModel.query.collectAsState()
    val headerUiItem = remember(query) { SearchResultHeaderUiItem(query) }

    var isShowKeyIn by remember { mutableStateOf(false) }
    var keyInExtra: SearchKeyInExtra? by remember { mutableStateOf(null) }

    DisposableEffect(
        viewModel, lifecycleOwner
    ) {
        val job = viewModel.observeSideEffects(lifecycleOwner.lifecycle) { sideEffect ->
            when (sideEffect) {
                is SearchResultSideEffect.OpenKeyIn -> {
                    isShowKeyIn = true
                    keyInExtra = SearchKeyInExtra(sideEffect.query)
                }

                is SearchResultSideEffect.OpenDetail -> {
                    appState.navigate(NavigationRoute.Detail.route(sideEffect.origin))
                }
            }
        }
        onDispose {
            job.cancel()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column {
            headerUiItem.BuildItem(viewModel::processIntent)

            List(
                state = listState,
                viewModel = viewModel,
                items = items,
                onRefresh = {
                    viewModel.setQuery("")
                }
            )
        }
        if (items.loadState.refresh is LoadState.Loading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .onClick {}
            ) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
        }
    }

    if (isShowKeyIn) {
        SearchKeyInScreen(
            appState = appState,
            extra = keyInExtra,
            onQuery = { newQuery ->
                listState.requestScrollToItem(0)
                viewModel.setQuery(newQuery)
            }) {
            isShowKeyIn = false
            keyInExtra = null
        }
    }
}

@Preview(showBackground = true)
@Composable
fun Preview() {
    SearchResultScreen(rememberAppState())
}