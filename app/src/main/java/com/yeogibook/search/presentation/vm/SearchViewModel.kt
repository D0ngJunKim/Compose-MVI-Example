package com.yeogibook.search.presentation.vm

import androidx.lifecycle.viewModelScope
import com.yeogibook.abcmm.presentation.vm.BaseViewModel
import com.yeogibook.search.presentation.service.repository.SearchRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    override val repository: SearchRepository
) : BaseViewModel<SearchViewState, SearchSideEffect, SearchIntent>() {

    override fun loadInit() {
        viewModelScope.launch {

        }
    }
    override fun loadMore() {
        TODO("Not yet implemented")
    }

    override fun processIntent(intent: SearchIntent) {
        TODO("Not yet implemented")
    }
}