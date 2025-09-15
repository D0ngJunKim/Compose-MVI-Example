package com.yeogibook.favorite.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.yeogibook.abcmm.presentation.core.AppState
import com.yeogibook.abcmm.presentation.core.NavigationRoute
import com.yeogibook.abcmm.presentation.ui.FooterSpacerUiItem
import com.yeogibook.abcmm.presentation.vm.observeSideEffects
import com.yeogibook.favorite.presentation.units.header.FavoriteHeaderUiItem
import com.yeogibook.favorite.presentation.vm.FavoriteViewModel
import com.yeogibook.favorite.presentation.vm.intent.FavoriteIntent
import com.yeogibook.favorite.presentation.vm.intent.FavoriteSideEffect
import com.yeogibook.search.keyin.presentation.SearchKeyInExtra
import com.yeogibook.search.keyin.presentation.SearchKeyInScreen

@Composable
fun FavoriteScreen(
    appState: AppState,
    listState: LazyListState = rememberLazyListState(),
    viewModel: FavoriteViewModel = hiltViewModel<FavoriteViewModel>(),
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val favoriteBooks by viewModel.uiItems.collectAsState()

    var isShowKeyIn by remember { mutableStateOf(false) }
    var keyInExtra: SearchKeyInExtra? by remember { mutableStateOf(null) }
    var query: String? by remember { mutableStateOf("") }

    val headerUiItem = remember(query) { FavoriteHeaderUiItem(query) }
    val footerUiItem = remember { FooterSpacerUiItem<FavoriteIntent>() }


    DisposableEffect(
        viewModel, lifecycleOwner
    ) {
        val job = viewModel.observeSideEffects(lifecycleOwner.lifecycle) { sideEffect ->
            when (sideEffect) {
                is FavoriteSideEffect.OpenKeyIn -> {
                    isShowKeyIn = true
                    keyInExtra = SearchKeyInExtra(sideEffect.query)
                }

                is FavoriteSideEffect.OpenDetail -> {
                    appState.navigate(NavigationRoute.Detail.route(sideEffect.origin))
                }
            }
        }
        onDispose {
            job.cancel()
        }
    }

    Column(
        Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        headerUiItem.BuildItem(viewModel::processIntent)

        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            overscrollEffect = null
        ) {
            favoriteBooks.forEach { book ->
                if (book.hasSticky()) {
                    stickyHeader {
                        book.BuildStickyItem(viewModel::processIntent)
                    }
                }

                item {
                    book.BuildItem(viewModel::processIntent)
                }
            }

            item {
                footerUiItem.BuildItem(viewModel::processIntent)
            }
        }
    }


    if (isShowKeyIn) {
        SearchKeyInScreen(
            appState = appState,
            extra = keyInExtra,
            keyboardType = KeyboardType.Number,
            hintText = "금액대를 입력해 주세요.",
            onQuery = { newQuery ->
                query = newQuery
                viewModel.setQuery(query)
            }) {
            isShowKeyIn = false
            keyInExtra = null
        }
    }
}