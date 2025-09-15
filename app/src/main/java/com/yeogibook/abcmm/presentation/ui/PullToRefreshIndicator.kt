package com.yeogibook.abcmm.presentation.ui

import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.getSystemService
import com.yeogibook.R

@Composable
fun PullToRefreshIndicator(state: PullToRefreshState) {
    val context = LocalContext.current

    LaunchedEffect(state.contentState.value) {
        if (state.contentState.value == PullToRefreshContentState.REACHED_THRESHOLD) {
            context.getSystemService<Vibrator>()?.run {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    vibrate(VibrationEffect.createOneShot(15, 100))
                } else {
                    vibrate(15)
                }
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .background(colorResource(R.color.gray150))
    ) {
        LocalText(
            text = "당겨서 새로고침",
            fontSize = 12.sp,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}