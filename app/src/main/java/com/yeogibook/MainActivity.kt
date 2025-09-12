package com.yeogibook

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.addCallback
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.yeogibook.abcmm.presentation.core.AppState
import com.yeogibook.abcmm.presentation.core.NavigationRoute
import com.yeogibook.abcmm.presentation.core.rememberAppState
import com.yeogibook.abcmm.presentation.ui.LocalText
import com.yeogibook.abcmm.presentation.ui.ds.padding
import com.yeogibook.abcmm.presentation.ui.ds.token.SpaceToken
import com.yeogibook.abcmm.presentation.ui.onClick
import com.yeogibook.bookdetail.presentation.BookDetailScreen
import com.yeogibook.favorite.presentation.FavoriteScreen
import com.yeogibook.search.result.presentation.SearchResultScreen
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val appState = rememberAppState()
            AppNavHost(appState)

            onBackPressedDispatcher.addCallback(this@MainActivity) {
                if (appState.navController.currentDestination?.route != NavigationRoute.Main.config.route) {
                    appState.navigateBack()
                } else {
                    finish()
                }
            }
        }
    }
}


@Composable
private fun AppNavHost(
    appState: AppState,
    modifier: Modifier = Modifier,
) {
    val navController = appState.navController

    NavHost(
        navController = navController,
        startDestination = NavigationRoute.Main.config.route,
        modifier = modifier,
        enterTransition = {
            slideIntoContainer(
                AnimatedContentTransitionScope.SlideDirection.Left,
                animationSpec = tween(300)
            )
        },
        exitTransition = {
            slideOutOfContainer(
                AnimatedContentTransitionScope.SlideDirection.Left,
                animationSpec = tween(300)
            )
        },
        popEnterTransition = {
            slideIntoContainer(
                AnimatedContentTransitionScope.SlideDirection.Right,
                animationSpec = tween(300)
            )
        },
        popExitTransition = {
            slideOutOfContainer(
                AnimatedContentTransitionScope.SlideDirection.Right,
                animationSpec = tween(300)
            )
        }
    ) {
        composable(
            route = NavigationRoute.Main.config.route,
            arguments = NavigationRoute.Main.config.arguments
        ) {
            MainScreen(appState)
        }

        composable(
            route = NavigationRoute.Detail.config.route,
            arguments = NavigationRoute.Detail.config.arguments
        ) { backStackEntry ->
            BookDetailScreen(appState, NavigationRoute.Detail.getExtra(backStackEntry.arguments))
        }
    }
}

@Composable
private fun MainScreen(appState: AppState) {
    val tabs = listOf("검색", "즐겨찾기")
    val pagerState = rememberPagerState(
        initialPage = 0,
        pageCount = { tabs.size }
    )
    Column(modifier = Modifier.fillMaxSize()) {
        Spacer(
            modifier = Modifier.height(
                WindowInsets.statusBars.asPaddingValues().calculateTopPadding()
            )
        )

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
                key = { page -> tabs[page] }
            ) { page ->
                when (page) {
                    0 -> SearchResultScreen(appState)
                    1 -> FavoriteScreen(appState)
                }
            }

            Column(
                modifier = Modifier
                    .padding(
                        start = SpaceToken._16,
                        end = SpaceToken._16,
                        bottom = SpaceToken._32
                    )
                    .align(Alignment.BottomCenter)
            ) {
                Row(
                    modifier = Modifier
                        .shadow(elevation = 4.dp, shape = CircleShape)
                        .background(Color.White, shape = CircleShape)
                ) {
                    for ((index, tab) in tabs.withIndex()) {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(40.dp)
                                .onClick {
                                    appState.scope.launch {
                                        pagerState.scrollToPage(tabs.indexOf(tab))
                                    }
                                }
                        ) {
                            LocalText(
                                text = tab,
                                modifier = Modifier.align(Alignment.Center),
                                color = if (pagerState.currentPage == index) Color.Red else Color.DarkGray,
                                fontWeight = if (pagerState.currentPage == index) FontWeight.Bold else FontWeight.Normal
                            )
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
    }
}

@Preview(
    showBackground = true,
    showSystemUi = true
)
@Composable
private fun Preview() {
    AppNavHost(rememberAppState())
}