package com.yeogibook.abcmm.presentation.ui

import android.os.VibrationEffect
import android.os.Vibrator
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.getSystemService
import com.yeogibook.R
import kotlin.math.roundToInt

@Composable
fun PullToRefreshIndicator(state: PullToRefreshState) {
    val context = LocalContext.current

    LaunchedEffect(state.contentState.value) {
        if (state.contentState.value == PullToRefreshContentState.REACHED_THRESHOLD) {
            context.getSystemService<Vibrator>()?.run {
                vibrate(VibrationEffect.createOneShot(15, 100))
            }
        }
    }

    Box {
        val offset = with(LocalDensity.current) { 100.dp.toPx() } / 2f
        val topMargin = (state.contentOffsetState.value / 2.0f - offset).roundToInt()

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .offset { IntOffset(x = 0, y = topMargin) }
                .background(colorResource(R.color.gray150))
        ) {
            LocalText(
                text = "당겨서 새로고침",
                fontSize = 12.sp,
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}