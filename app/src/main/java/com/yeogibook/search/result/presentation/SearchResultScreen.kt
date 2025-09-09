package com.yeogibook.search.result.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.yeogibook.abcmm.presentation.core.AppState
import com.yeogibook.abcmm.presentation.core.List
import com.yeogibook.abcmm.presentation.core.NavigationRoute
import com.yeogibook.abcmm.presentation.core.rememberAppState
import com.yeogibook.abcmm.presentation.ui.SpacerUiItem
import com.yeogibook.abcmm.presentation.ui.ds.CircularProgressIndicator
import com.yeogibook.abcmm.presentation.ui.onClick
import com.yeogibook.abcmm.presentation.vm.observeSideEffects
import com.yeogibook.search.keyin.presentation.SearchKeyInExtra
import com.yeogibook.search.result.presentation.units.header.SearchResultHeaderUiItem
import com.yeogibook.search.result.presentation.vm.SearchResultViewModel
import com.yeogibook.search.result.presentation.vm.intent.SearchResultSideEffect

@Composable
fun SearchResultScreen(
    appState: AppState,
    viewModel: SearchResultViewModel = hiltViewModel(),
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val isLoading by viewModel.isLoading.collectAsState()
    val listState = rememberLazyListState()
    val headerUiItem = remember { SearchResultHeaderUiItem() }

    LaunchedEffect(viewModel.hashCode()) {
        viewModel.loadInit()
    }

    DisposableEffect(
        viewModel, lifecycleOwner
    ) {
        val job = viewModel.observeSideEffects(lifecycleOwner.lifecycle) { sideEffect ->
            when (sideEffect) {
                is SearchResultSideEffect.OpenKeyIn -> {
                    appState.navigate(NavigationRoute.KeyIn.route(SearchKeyInExtra(sideEffect.query)))
                }
            }
        }
        onDispose {
            job.cancel()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column {
            headerUiItem.BuildStickyItem(viewModel::processIntent)

            List(
                listState = listState,
                viewModel = viewModel,
                isUsePullToRefresh = true,
                contentGap = 10.dp,
                headerItem = remember { SpacerUiItem(0.dp) }
            )
        }

        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .onClick(onClick = {})
            ) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun Preview() {
    SearchResultScreen(rememberAppState())
}