package com.yeogibook.favorite.presentation.util

import com.yeogibook.abcmm.data.entity.BookDocumentDiData

object FavoriteBookManager {
    private val favoriteBookLMap = LinkedHashMap<String, BookDocumentDiData>()
    val favoriteBookList: List<BookDocumentDiData>
        get() = favoriteBookLMap.values.toList()
    private val listeners = mutableSetOf<() -> Unit>()

    fun addListener(listener: () -> Unit) {
        listeners.add(listener)
    }

    fun removeListener(listener: () -> Unit) {
        listeners.remove(listener)
    }

    private fun notifyListeners() {
        listeners.forEach { it.invoke() }
    }

    fun isFavoriteBook(book: BookDocumentDiData): Boolean {
        return favoriteBookLMap.containsKey(book.isbn.orEmpty())
    }

    fun addFavoriteBook(book: BookDocumentDiData) {
        if (favoriteBookLMap.put(book.isbn.orEmpty(), book) == null) {
            notifyListeners()
        }
    }

    fun removeFavoriteBook(book: BookDocumentDiData) {
        if (favoriteBookLMap.remove(book.isbn.orEmpty()) != null) {
            notifyListeners()
        }
    }
}