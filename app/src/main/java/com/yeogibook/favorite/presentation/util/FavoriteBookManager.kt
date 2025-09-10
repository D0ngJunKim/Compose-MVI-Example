package com.yeogibook.favorite.presentation.util

import com.yeogibook.abcmm.data.entity.BookDocumentDiData
import com.yeogibook.abcmm.data.prefs.AppPreference

object FavoriteBookManager {
    private val favoriteBookMap = LinkedHashMap<String, BookDocumentDiData>()
    val favoriteBookList: List<BookDocumentDiData>
        get() = favoriteBookMap.values.toList()
    private val listeners = mutableSetOf<() -> Unit>()

    init {
        AppPreference.favoriteBooks.forEach { book ->
            favoriteBookMap.put(book.isbn.orEmpty(), book)
        }
    }

    fun addListener(listener: () -> Unit) {
        listeners.add(listener)
    }

    fun removeListener(listener: () -> Unit) {
        listeners.remove(listener)
    }

    private fun notifyListeners() {
        listeners.forEach { it.invoke() }
        AppPreference.favoriteBooks = favoriteBookList
    }

    fun isFavoriteBook(book: BookDocumentDiData): Boolean {
        return favoriteBookMap.containsKey(book.isbn.orEmpty())
    }

    fun addFavoriteBook(book: BookDocumentDiData) {
        if (favoriteBookMap.put(book.isbn.orEmpty(), book) == null) {
            notifyListeners()
        }
    }

    fun removeFavoriteBook(book: BookDocumentDiData) {
        if (favoriteBookMap.remove(book.isbn.orEmpty()) != null) {
            notifyListeners()
        }
    }
}