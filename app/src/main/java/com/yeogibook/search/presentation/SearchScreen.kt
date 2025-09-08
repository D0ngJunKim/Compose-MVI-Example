package com.yeogibook.search.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.yeogibook.abcmm.presentation.core.lazyList
import com.yeogibook.abcmm.presentation.ui.ds.CircularProgressIndicator
import com.yeogibook.abcmm.presentation.vm.observeSideEffects
import com.yeogibook.search.presentation.units.header.SearchHeaderUiItem
import com.yeogibook.search.presentation.vm.SearchViewModel

@Composable
fun SearchScreen(viewModel: SearchViewModel = viewModel()) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val isLoading by viewModel.isLoading.collectAsState()
    val viewState by viewModel.viewState.collectAsState()
    val listState = rememberLazyListState()
    val headerTransY by remember {
        derivedStateOf {
            if (listState.layoutInfo.visibleItemsInfo.isNotEmpty() &&
                listState.firstVisibleItemIndex == 0
            ) {
                listState.firstVisibleItemScrollOffset * 0.6f
            } else {
                0f
            }
        }
    }
    val headerUiItem = remember { SearchHeaderUiItem() }

    LaunchedEffect(viewModel) {
        viewModel.loadInit()
    }

    DisposableEffect(
        viewModel, lifecycleOwner
    ) {
        val job = viewModel.observeSideEffects(lifecycleOwner.lifecycle) { sideEffect ->
            when (sideEffect) {
                else -> {}
            }
        }
        onDispose {
            job.cancel()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .graphicsLayer(translationY = headerTransY)
            ) {
                headerUiItem.BuildItem(viewModel::processIntent)
            }

            LazyColumn(state = listState) {
                lazyList(
                    items = viewState?.dataList ?: emptyList(),
                    processIntent = viewModel::processIntent
                )
            }
        }

        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) {

                    }
            ) {
                CircularProgressIndicator()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun Preview() {
    SearchScreen()
}