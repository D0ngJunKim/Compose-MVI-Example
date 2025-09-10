package com.yeogibook.search.result.presentation.vm

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.yeogibook.abcmm.presentation.core.LazyItem
import com.yeogibook.abcmm.presentation.vm.BaseViewModel
import com.yeogibook.search.result.data.constants.SearchResultSorts
import com.yeogibook.search.result.presentation.service.repository.SearchResultRepository
import com.yeogibook.search.result.presentation.vm.intent.SearchResultIntent
import com.yeogibook.search.result.presentation.vm.intent.SearchResultSideEffect
import com.yeogibook.search.result.presentation.vm.intent.SearchResultSideEffect.OpenDetail
import com.yeogibook.search.result.presentation.vm.intent.SearchResultSideEffect.OpenKeyIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import javax.inject.Inject

@HiltViewModel
class SearchResultViewModel @Inject constructor(
    val repository: SearchResultRepository,
) : BaseViewModel<SearchResultSideEffect, SearchResultIntent>() {
    private val query = MutableStateFlow<String?>(null)
    private val sort = MutableStateFlow(SearchResultSorts.ACCURACY)

    @OptIn(ExperimentalCoroutinesApi::class)
    val uiFlow: Flow<PagingData<LazyItem<SearchResultIntent>>> =
        combine(query, sort) { query, sort ->
            query to sort
        }.flatMapLatest { (query, sort) ->
            repository.getSearchResults(query, sort)
                .cachedIn(viewModelScope)
        }

    fun setQuery(query: String?) {
        this.query.value = query
    }

    private fun onSort(sort: SearchResultSorts) {
        this.sort.value = sort
    }

    override fun processIntent(intent: SearchResultIntent) {
        when (intent) {
            SearchResultIntent.OpenKeyIn -> {
                sendSideEffect(OpenKeyIn(query.value.orEmpty()))
            }

            is SearchResultIntent.Sort -> {
                onSort(intent.sort)
            }

            is SearchResultIntent.OpenDetail -> {
                sendSideEffect(OpenDetail(intent.origin))
            }
        }
    }
}