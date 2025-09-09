package com.yeogibook.search.result.presentation.vm

import androidx.lifecycle.viewModelScope
import com.yeogibook.abcmm.presentation.vm.BaseViewModel
import com.yeogibook.search.result.data.service.repository.SearchResultRepository
import com.yeogibook.search.result.data.service.usecase.SearchResultUseCase
import com.yeogibook.search.result.presentation.vm.intent.SearchResultIntent
import com.yeogibook.search.result.presentation.vm.intent.SearchResultSideEffect
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchResultViewModel @Inject constructor(
    override val repository: SearchResultRepository,
) : BaseViewModel<SearchResultViewState, SearchResultSideEffect, SearchResultIntent>() {
    private val useCase = SearchResultUseCase(repository)

    override fun loadInit() {
        viewModelScope.launch(Dispatchers.Default) {
            useCase.loadInit(onLoading = { isLoading ->
                setLoading(isLoading)
            }, onResult = {
                updateState(SearchResultViewState(it))
            })
        }
    }

    override fun loadMore() {

    }

    override fun processIntent(intent: SearchResultIntent) {
        when (intent) {
            SearchResultIntent.OpenKeyIn -> {
                sendSideEffect(SearchResultSideEffect.OpenKeyIn(useCase.query.orEmpty()))
            }
        }
    }
}