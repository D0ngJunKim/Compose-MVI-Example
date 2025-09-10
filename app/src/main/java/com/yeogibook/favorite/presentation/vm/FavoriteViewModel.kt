package com.yeogibook.favorite.presentation.vm

import androidx.lifecycle.viewModelScope
import com.yeogibook.abcmm.presentation.core.LazyItem
import com.yeogibook.abcmm.presentation.vm.BaseViewModel
import com.yeogibook.favorite.presentation.constants.FavoriteSorts
import com.yeogibook.favorite.presentation.units.empty.FavoriteEmptyUiItem
import com.yeogibook.favorite.presentation.units.empty.FavoriteSearchFailUiItem
import com.yeogibook.favorite.presentation.units.filter.FavoriteFilterUiItem
import com.yeogibook.favorite.presentation.units.item.getFavoriteItemUiItem
import com.yeogibook.favorite.presentation.util.FavoriteBookManager
import com.yeogibook.favorite.presentation.vm.intent.FavoriteIntent
import com.yeogibook.favorite.presentation.vm.intent.FavoriteSideEffect
import com.yeogibook.favorite.presentation.vm.intent.FavoriteSideEffect.OpenDetail
import com.yeogibook.favorite.presentation.vm.intent.FavoriteSideEffect.OpenKeyIn
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class FavoriteViewModel : BaseViewModel<FavoriteSideEffect, FavoriteIntent>() {
    private var query: String? = null
    private var sort: FavoriteSorts = FavoriteSorts.ASCENDING
    private val _uiItems = MutableStateFlow<List<LazyItem<FavoriteIntent>>>(emptyList())
    val uiItems: StateFlow<List<LazyItem<FavoriteIntent>>> = _uiItems.asStateFlow()

    private val listener: () -> Unit = {
        viewModelScope.launch {
            updateFavoriteBooks()
        }
    }

    init {
        updateFavoriteBooks()
        FavoriteBookManager.addListener(listener)
    }

    private fun updateFavoriteBooks() {
        val currentQuery = query
        val filteredList = FavoriteBookManager.favoriteBookList
            .filter { book ->
                currentQuery?.let { q ->
                    book.salePrice?.toString()?.contains(q, ignoreCase = true) == true
                } != false
            }

        val sortedList = when (sort) {
            FavoriteSorts.ASCENDING -> filteredList.sortedBy { it.title }
            FavoriteSorts.DESCENDING -> filteredList.sortedByDescending { it.title }
        }
        val itemList: ArrayList<LazyItem<FavoriteIntent>> =
            sortedList.mapTo(arrayListOf()) { getFavoriteItemUiItem(it) }

        if (itemList.isNotEmpty()) {
            itemList.add(0, FavoriteFilterUiItem(sort))
        } else {
            if (query.isNullOrEmpty()) {
                itemList.add(FavoriteEmptyUiItem())
            } else {
                itemList.add(FavoriteSearchFailUiItem())
            }
        }

        _uiItems.value = itemList
    }

    fun setQuery(newQuery: String?) {
        query = newQuery
        updateFavoriteBooks()
    }

    private fun onSort(sort: FavoriteSorts) {
        this.sort = sort
        updateFavoriteBooks()
    }

    override fun processIntent(intent: FavoriteIntent) {
        when (intent) {
            FavoriteIntent.OpenKeyIn -> {
                sendSideEffect(OpenKeyIn(query.orEmpty()))
            }

            is FavoriteIntent.OpenDetail -> {
                sendSideEffect(OpenDetail(intent.origin))
            }

            is FavoriteIntent.Sort -> {
                onSort(intent.sort)
            }
        }
    }

    override fun onCleared() {
        FavoriteBookManager.removeListener(listener)
        super.onCleared()
    }

}