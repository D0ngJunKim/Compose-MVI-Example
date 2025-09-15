package com.yeogibook.abcmm.presentation.ui

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clipScrollableContainer
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.Velocity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@Composable
fun rememberPullToRefreshState(
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    maxDragOffset: Dp? = null,
    onRefreshListener: PullToRefreshState.() -> Unit,
): PullToRefreshState {
    val density = LocalDensity.current
    val maxDragOffsetPx: Float? = remember(key1 = maxDragOffset, key2 = density) {
        with(density) {
            maxDragOffset?.toPx()
        }
    }

    return remember { PullToRefreshState(coroutineScope, maxDragOffsetPx, onRefreshListener) }
}

@Composable
fun PullToRefresh(
    refreshIndicator: @Composable PullToRefreshState.() -> Unit,
    state: PullToRefreshState,
    modifier: Modifier = Modifier,
    dragEfficiency: Float = 0.35f,
    isSupportNonScrollableContent: Boolean = false,
    canDragWhileRefreshing: Boolean = false,
    content: @Composable () -> Unit,
) {
    val nestedScrollConnection = rememberPullToRefreshNestedScrollConnection(
        state,
        dragEfficiency,
        canDragWhileRefreshing
    )

    Layout(
        content = {
            state.refreshIndicator()
            if (isSupportNonScrollableContent) {
                Box(modifier = Modifier.verticalScroll(rememberScrollState())) {
                    content()
                }
            } else {
                content()
            }
        },
        modifier = modifier
            .nestedScroll(nestedScrollConnection)
            .clipScrollableContainer(Orientation.Vertical)
    ) { measurableList, constraints ->
        val contentPlaceable = measurableList[1].measure(constraints.copy(minWidth = 0, minHeight = 0))
        val refreshContentPlaceable = measurableList[0].measure(
            Constraints(
                maxWidth = contentPlaceable.width,
                maxHeight = Constraints.Infinity,
            )
        )
        if (state.contentThresholdState.floatValue == 0f) {
            state.contentThresholdState.floatValue = refreshContentPlaceable.height.toFloat()
        }

        layout(contentPlaceable.width, contentPlaceable.height) {
            val offset = state.contentOffsetState.value.roundToInt()

            refreshContentPlaceable.placeRelative(
                x = 0,
                y = 0
            )
            contentPlaceable.placeRelative(
                x = 0,
                y = offset
            )
        }
    }
}

@Stable
data class PullToRefreshState(
    private val coroutineScope: CoroutineScope,
    private val maxDragOffsetPx: Float? = null,
    private val onRefreshListener: PullToRefreshState.() -> Unit,
) {
    val contentState = mutableStateOf(PullToRefreshContentState.STOP)
    val contentOffsetState = Animatable(0f)
    val contentThresholdState = mutableFloatStateOf(0f)

    private suspend fun setState(state: PullToRefreshContentState) {
        if (contentState.value == state) return

        when (state) {
            PullToRefreshContentState.STOP -> {
                contentState.value = PullToRefreshContentState.STOP
                contentOffsetState.animateTo(0f, tween(200))
            }

            PullToRefreshContentState.DRAGGING -> {
                contentState.value = PullToRefreshContentState.DRAGGING
            }

            PullToRefreshContentState.REACHED_THRESHOLD -> {
                contentState.value = PullToRefreshContentState.REACHED_THRESHOLD
            }

            PullToRefreshContentState.REFRESH -> {
                contentState.value = PullToRefreshContentState.REFRESH
                onRefreshListener()
                setState(PullToRefreshContentState.STOP)
            }
        }
    }

    fun offset(refreshContentOffset: Float) {
        coroutineScope.launch {
            val maxValue = maxDragOffsetPx ?: contentThresholdState.floatValue
            val targetValue = (contentOffsetState.value + refreshContentOffset).coerceAtMost(contentThresholdState.floatValue)

            if (targetValue > 0f && targetValue < maxValue) {
                setState(PullToRefreshContentState.DRAGGING)
            } else if (targetValue >= maxValue) {
                setState(PullToRefreshContentState.REACHED_THRESHOLD)
            } else if (targetValue <= 0f) {
                setState(PullToRefreshContentState.STOP)
            }
            contentOffsetState.snapTo(targetValue)
        }
    }

    fun offsetHoming() {
        coroutineScope.launch {
            if (contentState.value == PullToRefreshContentState.REACHED_THRESHOLD) {
                setState(PullToRefreshContentState.REFRESH)
            } else {
                setState(PullToRefreshContentState.STOP)
            }
        }
    }
}

@Composable
private fun rememberPullToRefreshNestedScrollConnection(
    state: PullToRefreshState,
    dragEfficiency: Float,
    canDragWhileRefreshing: Boolean,
): NestedScrollConnection =
    remember {
        PullToRefreshNestedScrollConnection(
            state,
            dragEfficiency,
            canDragWhileRefreshing
        )
    }

private class PullToRefreshNestedScrollConnection(
    private val state: PullToRefreshState,
    private val dragEfficiency: Float,
    private val canDragWhileRefreshing: Boolean,
) : NestedScrollConnection {

    override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
        if (!canDragWhileRefreshing && state.contentState.value == PullToRefreshContentState.REFRESH) {
            return Offset(0f, available.y)
        }

        val contentOffset = state.contentOffsetState.value
        if (source == NestedScrollSource.UserInput) {
            if (available.y < 0 && contentOffset > 0) {
                var consumptive = available.y
                if (-available.y > contentOffset) {
                    consumptive = available.y - contentOffset
                }
                state.offset(consumptive * dragEfficiency)
                return Offset(0f, consumptive)
            }
        }
        return Offset.Zero
    }

    override fun onPostScroll(consumed: Offset, available: Offset, source: NestedScrollSource): Offset {
        if (!canDragWhileRefreshing && state.contentState.value == PullToRefreshContentState.REFRESH) {
            return Offset.Zero
        }

        if (source == NestedScrollSource.UserInput) {
            val value = available.y
            if (value > 0) {
                if (value > 0.01f) {
                    state.offset(value * dragEfficiency)
                }
                return Offset(0f, value)
            }
        }
        return Offset.Zero
    }

    override suspend fun onPreFling(available: Velocity): Velocity {
        if (!canDragWhileRefreshing && state.contentState.value == PullToRefreshContentState.REFRESH) {
            return available
        }

        if (state.contentOffsetState.value != 0f) {
            state.offsetHoming()
            return available
        }
        return Velocity.Zero
    }
}

enum class PullToRefreshContentState {
    STOP,
    DRAGGING,
    REACHED_THRESHOLD,
    REFRESH,
}