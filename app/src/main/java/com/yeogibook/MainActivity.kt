package com.yeogibook

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.addCallback
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.yeogibook.abcmm.presentation.core.AppState
import com.yeogibook.abcmm.presentation.core.NavigationRoute
import com.yeogibook.abcmm.presentation.core.rememberAppState
import com.yeogibook.bookdetail.presentation.BookDetailScreen
import com.yeogibook.main.MainScreen
import dagger.hilt.android.AndroidEntryPoint

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

@Preview(
    showBackground = true,
    showSystemUi = true
)
@Composable
private fun Preview() {
    AppNavHost(rememberAppState())
}