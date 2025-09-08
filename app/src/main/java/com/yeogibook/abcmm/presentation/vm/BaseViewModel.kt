package com.yeogibook.abcmm.presentation.vm

import androidx.annotation.CallSuper
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.coroutineScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

abstract class BaseViewModel<ViewState : Any, SideEffect : Any, Intent : Any> : ViewModel() {
    abstract val repository: IRepository?
    abstract fun loadInit()
    abstract fun processIntent(intent: Intent)
    open fun loadMore() {}

    private val _viewState: MutableStateFlow<ViewState?> by lazy {
        MutableStateFlow(null)
    }
    val viewState: StateFlow<ViewState?>
        get() = _viewState

    private val _sideEffects = Channel<SideEffect>(Channel.UNLIMITED)
    val sideEffects: Flow<SideEffect>
        get() = _sideEffects.receiveAsFlow()

    private val _isLoading: MutableStateFlow<Boolean> by lazy {
        MutableStateFlow(false)
    }
    val isLoading: StateFlow<Boolean>
        get() = _isLoading

    fun cancelNetwork() {
        repository?.cancelAll()
        viewModelScope.coroutineContext.cancelChildren()
    }

    @CallSuper
    open fun refreshData() {
        cancelNetwork()
        _viewState.value = null
        loadInit()
    }

    protected fun sendSideEffect(sideEffect: SideEffect) {
        _sideEffects.trySend(sideEffect)
    }

    protected fun updateState(updated: ViewState): Boolean {
        _viewState.value = updated
        return _viewState.value != updated
    }
}

inline fun <SideEffect : Any> BaseViewModel<*, SideEffect, *>.observeSideEffects(
    lifecycle: Lifecycle,
    crossinline sideEffectFunction: (SideEffect) -> Unit
): Job {
    return lifecycle.coroutineScope.launch {
        lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
            sideEffects.collect {
                sideEffectFunction(it)
            }
        }
    }
}