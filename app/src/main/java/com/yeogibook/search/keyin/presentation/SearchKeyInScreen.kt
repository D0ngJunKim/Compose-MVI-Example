package com.yeogibook.search.keyin.presentation

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.yeogibook.abcmm.presentation.core.AppState
import com.yeogibook.abcmm.presentation.ui.LocalText
import kotlinx.coroutines.delay

@Composable
fun SearchKeyInScreen(
    appState: AppState,
    extra: SearchKeyInExtra? = null,
    onDismiss: () -> Unit = {},
) {
    var showBottomSheetContent by remember { mutableStateOf(false) }
    val interactionSource = remember { MutableInteractionSource() }

    LaunchedEffect(Unit) {
        showBottomSheetContent = true
    }

    // showBottomSheetContent가 false가 되면 애니메이션 완료 후 onDismissRequest 호출
    LaunchedEffect(showBottomSheetContent) {
        if (!showBottomSheetContent) {
            delay(300) // 애니메이션 지속 시간 (300ms)만큼 기다린 후
            onDismiss() // MainScreen에 닫기 요청을 보냄
        }
    }

    BackHandler {
        showBottomSheetContent = false // exit 애니메이션 트리거
    }

    // 전체 화면 Box, 배경 클릭 처리 및 바텀 시트 콘텐츠 포함
    Box(
        modifier = Modifier
            .fillMaxSize()
            // 배경을 클릭하면 바텀 시트가 닫히도록 합니다.
            .clickable(
                onClick = { showBottomSheetContent = false },
                indication = null,
                interactionSource = interactionSource
            )
    ) {
        // 배경 딤 애니메이션
        AnimatedVisibility(
            visible = showBottomSheetContent,
            enter = fadeIn(animationSpec = tween(300)),
            exit = fadeOut(animationSpec = tween(300))
        ) {
            // 전체 화면을 덮는 어두운 반투명 배경
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.5f))
            )
        }

        // 바텀 시트 콘텐츠 애니메이션
        AnimatedVisibility(
            visible = showBottomSheetContent,
            modifier = Modifier.align(Alignment.BottomCenter),
            enter = slideInVertically(
                initialOffsetY = { fullHeight -> fullHeight },
                animationSpec = tween(300)
            ),
            exit = slideOutVertically(
                targetOffsetY = { fullHeight -> fullHeight },
                animationSpec = tween(300)
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .height(300.dp)
                    // 이 Column 내부의 클릭은 부모 Box의 clickable에 영향을 주지 않도록
                    // `clickable` 모디파이어를 추가하고, 클릭 이벤트를 소비합니다.
                    .clickable(
                        onClick = {}, // 아무 동작 없음, 이벤트 소비 목적
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    )
            ) {
                // 여기에 SearchKeyInScreen의 실제 UI 컴포넌트들을 추가합니다.
                LocalText(text = "Search Key In Screen Content", color = Color.Black)
                extra?.let {
                    LocalText(text = "Query from extra: ${it.query}", color = Color.Black)
                }
            }
        }
    }
}

@Stable
data class SearchKeyInExtra(
    val query: String,
)