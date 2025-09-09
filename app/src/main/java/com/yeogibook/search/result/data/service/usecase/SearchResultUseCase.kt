package com.yeogibook.search.result.data.service.usecase

import com.google.gson.JsonObject
import com.yeogibook.abcmm.data.request.constants.Params
import com.yeogibook.abcmm.presentation.core.LazyItem
import com.yeogibook.network.constants.ResponseState
import com.yeogibook.search.result.data.constants.SearchResultSorts
import com.yeogibook.search.result.data.constants.SearchResultTargets
import com.yeogibook.search.result.data.entity.SearchBookDiData
import com.yeogibook.search.result.data.entity.SearchBookMetaDiData
import com.yeogibook.search.result.data.service.repository.SearchResultRepository
import com.yeogibook.search.result.presentation.vm.intent.SearchResultIntent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.withContext

class SearchResultUseCase(val repository: SearchResultRepository) {
    private val networkInfo = NetworkInfo()
    private val apiProcess = SearchResultUseCaseProcess()
    var query: String?
        set(value) {
            networkInfo.query = value
        }
        get() = networkInfo.query

    fun setTarget(target: SearchResultTargets?) {
        networkInfo.target = target
    }

    fun setSort(sort: SearchResultSorts) {
        networkInfo.sort = sort
    }

    suspend fun loadInit(
        onResult: (List<LazyItem<SearchResultIntent>>) -> Unit,
        onLoading: (Boolean) -> Unit,
    ) {
        networkInfo.reset()

        return repository.requestApi(networkInfo.toParams())
            .onStart {
                withContext(Dispatchers.Main) {
                    onLoading(true)
                }
            }
            .onCompletion {
                withContext(Dispatchers.Main) {
                    onLoading(false)
                }
            }
            .collect { result ->
                networkInfo.update(result.data?.meta)

                when (result) {
                    is ResponseState.Success<SearchBookDiData> -> {
                        onResult(apiProcess.processInit(result.data?.documents))
                    }

                    is ResponseState.Error<SearchBookDiData> -> {
                        onResult(apiProcess.processInit(null))
                    }
                }
            }
    }

    class NetworkInfo {
        private var page: Int = 1
        var hasNext: Boolean = true
            private set
        var query: String? = null
        var target: SearchResultTargets? = null
        var sort: SearchResultSorts = SearchResultSorts.ACCURACY

        fun toParams(): JsonObject {
            return JsonObject().apply {
                addProperty(Params.PAGE, page)
                addProperty(Params.QUERY, query)
                addProperty(Params.SORT, sort.value)
                addProperty(Params.TARGET, target?.value)
            }
        }

        fun update(data: SearchBookMetaDiData?) {
            if (data != null) {
                hasNext = data.isEnd == false
                page += 1
            } else {
                hasNext = false
            }

        }

        fun reset() {
            page = 1
            hasNext = true
            query = "가"
            target = null
            sort = SearchResultSorts.ACCURACY
        }
    }
}