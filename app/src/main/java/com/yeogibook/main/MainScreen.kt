package com.yeogibook.main

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.stopScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.yeogibook.R
import com.yeogibook.abcmm.presentation.core.AppState
import com.yeogibook.abcmm.presentation.ui.LocalText
import com.yeogibook.abcmm.presentation.ui.ds.padding
import com.yeogibook.abcmm.presentation.ui.ds.token.SpaceToken
import com.yeogibook.abcmm.presentation.ui.isClickable
import com.yeogibook.abcmm.presentation.ui.onClick
import com.yeogibook.favorite.presentation.FavoriteScreen
import com.yeogibook.search.result.presentation.SearchResultScreen
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

/**
 * Created by 180842 on 2025. 9. 15..
 */
data class TabInfo(val tabNm: String, val listState: LazyListState)

@Composable
fun MainScreen(appState: AppState) {
    val isInspectMode = LocalInspectionMode.current
    val tabs = listOf(
        TabInfo("검색", rememberLazyListState()),
        TabInfo("즐겨찾기", rememberLazyListState())
    )
    val pagerState = rememberPagerState(
        initialPage = 0,
        pageCount = { tabs.size }
    )

    var isCollapsed by rememberSaveable { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize()) {
        Spacer(
            modifier = Modifier.height(
                WindowInsets.statusBars.asPaddingValues().calculateTopPadding()
            )
        )

        LaunchedEffect(pagerState.currentPage) {
            val currentListState = tabs[pagerState.currentPage].listState
            snapshotFlow { currentListState.firstVisibleItemIndex }
                .distinctUntilChanged()
                .collect { firstIndex ->
                    isCollapsed = firstIndex > 0
                }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            HorizontalPager(
                userScrollEnabled = false,
                state = pagerState,
                modifier = Modifier
                    .fillMaxSize(),
                beyondViewportPageCount = 1,
                key = { page -> tabs[page].tabNm }
            ) { page ->
                if (isInspectMode.not()) {
                    when (page) {
                        0 -> SearchResultScreen(appState, tabs[page].listState)
                        1 -> FavoriteScreen(appState, tabs[page].listState)
                    }
                }
            }

            TabBar(
                appState = appState,
                tabs = tabs,
                pagerState = pagerState,
                isCollapsed = isCollapsed,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
            )
        }
    }
}

@Composable
private fun TabBar(
    appState: AppState,
    tabs: List<TabInfo>,
    pagerState: PagerState,
    isCollapsed: Boolean,
    modifier: Modifier,
) {
    val screenWidth = with(LocalDensity.current) { LocalWindowInfo.current.containerSize.width.toDp() }
    val fullTabBarWidth = screenWidth - (SpaceToken._16.dp * 2)

    val animatedAlpha by animateFloatAsState(
        targetValue = if (isCollapsed) 1f else 0f,
        animationSpec = tween(durationMillis = 300),
        label = "alphaAnimation"
    )
    val animatedShadow by animateFloatAsState(
        targetValue = if (isCollapsed) 4f else 0f,
        animationSpec = tween(durationMillis = 300),
        label = "shadowAnimation"
    )
    val animatedTabBarWidth by animateDpAsState(
        targetValue = if (isCollapsed) 100.dp else fullTabBarWidth,
        animationSpec = tween(durationMillis = 300),
        label = "tabBarWidthAnimation"
    )

    Column(
        modifier = modifier
            .padding(
                start = SpaceToken._16,
                end = SpaceToken._16,
                bottom = SpaceToken._32
            )
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .align(Alignment.BottomEnd)
                    .shadow(
                        elevation = Dp(animatedShadow),
                        shape = RoundedCornerShape(15.dp)
                    )
                    .background(
                        color = Color.White.compositeOver(Color.White.copy(animatedAlpha)),
                        shape = RoundedCornerShape(15.dp)
                    )
                    .onClick {
                        appState.scope.launch {
                            tabs[pagerState.currentPage].listState.animateScrollToItem(0)
                        }
                    }
            ) {
                Image(
                    painter = painterResource(R.drawable.token_arrow_upward),
                    contentDescription = "위로가기",
                    modifier = Modifier
                        .align(Alignment.Center)
                        .alpha(animatedAlpha)
                )
            }

            Row(
                modifier = Modifier
                    .shadow(elevation = 4.dp, shape = CircleShape)
                    .background(Color.White, shape = CircleShape)
                    .align(Alignment.BottomCenter)
                    .width(animatedTabBarWidth)
                    .height(40.dp)
                    .isClickable()
            ) {
                for ((index, tab) in tabs.withIndex()) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .onClick {
                                appState.scope.launch {
                                    tab.listState.stopScroll()
                                    pagerState.scrollToPage(tabs.indexOf(tab))
                                }
                            }
                    ) {
                        val color = if (pagerState.currentPage == index) Color.Red else Color.DarkGray
                        if (isCollapsed) {
                            val iconResId = when (index) {
                                0 -> R.drawable.token_search
                                else -> R.drawable.token_book_ribbon
                            }
                            Image(
                                painter = painterResource(iconResId),
                                contentDescription = tab.tabNm,
                                colorFilter = ColorFilter.tint(color),
                                modifier = Modifier
                                    .size(24.dp)
                                    .align(Alignment.Center)
                            )

                        } else {
                            LocalText(
                                text = tab.tabNm,
                                color = color,
                                fontWeight = if (pagerState.currentPage == index) FontWeight.Bold else FontWeight.Normal,
                                modifier = Modifier
                                    .align(Alignment.Center)
                                    .wrapContentHeight()
                            )
                        }
                    }
                }
            }
        }

        Spacer(
            modifier = Modifier.height(
                WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()
            )
        )
    }
}