package com.yeogibook.abcmm.presentation.core

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import androidx.savedstate.savedState
import kotlinx.coroutines.CoroutineScope
import kotlin.math.round

@Composable
fun rememberAppState(
    navController: NavHostController = rememberNavController(),
    scope: CoroutineScope = rememberCoroutineScope(),
): AppState {
    return remember(navController, scope) {
        AppState(navController, scope)
    }
}

@Stable
data class AppState(
    val navController: NavHostController,
    val scope: CoroutineScope,
) {
    fun navigate(route: NavigationRoute.Route) {
        navController.navigate(route.route) {
            launchSingleTop = true
            restoreState = true
        }
    }

    fun navigateBack() {
        navController.popBackStack(navController.graph.startDestinationId, inclusive = false)
    }
}