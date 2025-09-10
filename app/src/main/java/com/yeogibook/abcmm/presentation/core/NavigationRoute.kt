package com.yeogibook.abcmm.presentation.core

import androidx.navigation.NamedNavArgument
import androidx.navigation.NavType
import androidx.navigation.navArgument
import androidx.savedstate.SavedState
import com.google.gson.Gson
import com.yeogibook.abcmm.data.entity.BookDocumentDiData
import java.net.URLDecoder
import java.net.URLEncoder

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

    data object Detail : NavigationRoute<BookDocumentDiData>("Detail") {
        private val gson = Gson()
        override val config: Config = Config(
            route = "$_route/{data}",
            arguments = listOf(
                navArgument("data") { type = NavType.StringType }
            ))

        override fun route(extra: BookDocumentDiData?): Route {
            val json = try {
                gson.toJson(extra)
            } catch (_: Exception) {
                ""
            }
            val encoded = URLEncoder.encode(json)
            return Route("$_route/${encoded}")
        }

        override fun getExtra(savedState: SavedState?): BookDocumentDiData? {
            return try {
                val decoded = URLDecoder.decode(savedState?.getString("data"))
                gson.fromJson(decoded, BookDocumentDiData::class.java)
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