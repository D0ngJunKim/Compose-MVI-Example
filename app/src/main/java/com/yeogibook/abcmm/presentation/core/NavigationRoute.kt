package com.yeogibook.abcmm.presentation.core

import androidx.navigation.NamedNavArgument
import androidx.navigation.NavType
import androidx.navigation.navArgument
import androidx.savedstate.SavedState
import com.google.gson.Gson
import com.yeogibook.search.keyin.presentation.SearchKeyInExtra

/**
 * Created by 180842 on 2025. 9. 9..
 */
sealed class NavigationRoute<T>(protected val _route: String) {
    abstract val config: Config
    abstract fun route(extra: T? = null): Route

    abstract fun getExtra(savedState: SavedState?): T?

    data object Main : NavigationRoute<Any>("Main") {
        override val config: Config = Config(route = _route)
        override fun route(extra: Any?): Route = Route(_route)
        override fun getExtra(savedState: SavedState?): Any? {
            return null
        }
    }

    data object Detail : NavigationRoute<Any>("Detail") {
        override val config: Config = Config(route = _route)
        override fun route(extra: Any?): Route = Route(_route)
        override fun getExtra(savedState: SavedState?): Any? {
            return null
        }
    }

    data object KeyIn : NavigationRoute<SearchKeyInExtra>("KeyIn") {
        private val gson = Gson()
        override val config: Config = Config(
            route = "$_route/{extra}",
            arguments = listOf(
                navArgument("extra") { type = NavType.StringType }
            ))

        override fun route(extra: SearchKeyInExtra?): Route {
            val extraString = try {
                gson.toJson(extra)
            } catch (_: Exception) {
                ""
            }
            return Route("$_route/${extraString}")
        }

        override fun getExtra(savedState: SavedState?): SearchKeyInExtra? {
            return try {
                gson.fromJson(savedState?.getString("extra"), SearchKeyInExtra::class.java)
            } catch (_: Exception) {
                null
            }
        }
    }

    data class Config(
        val route: String,
        val arguments: List<NamedNavArgument> = emptyList(),
    )

    data class Route(
        val route: String,
    )
}