package com.yeogibook.abcmm.presentation.core

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.CoroutineScope

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
        navController.navigate(route = route.route)
    }

    fun navigateBack() {
        navController.popBackStack()
    }
}