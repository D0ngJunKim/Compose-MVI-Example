package com.yeogibook.abcmm.data.prefs

import android.content.Context
import androidx.core.content.edit
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.yeogibook.abcmm.data.entity.BookDocumentDiData
import com.yeogibook.abcmm.presentation.util.ContextProvider

object AppPreference {
    private val pref =
        ContextProvider.context.getSharedPreferences("YEOGIBOOK", Context.MODE_PRIVATE)
    private val gson = Gson()

    private const val KEY_FAVORITES = "KEY_FAVORITES"
    var favoriteBooks: List<BookDocumentDiData>
        get() {
            val favorites = pref.getString(KEY_FAVORITES, null)
            return if (favorites.isNullOrEmpty()) {
                emptyList()
            } else {
                gson.fromJson(favorites, object : TypeToken<List<BookDocumentDiData>>() {}.type)
            }
        }
        set(value) {
            pref.edit {
                putString(KEY_FAVORITES, gson.toJson(value))
            }
        }
}