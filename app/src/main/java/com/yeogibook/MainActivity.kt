package com.yeogibook

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.yeogibook.abcmm.presentation.ui.LocalText
import com.yeogibook.abcmm.presentation.ui.ds.padding
import com.yeogibook.abcmm.presentation.ui.ds.token.SpaceToken
import com.yeogibook.search.presentation.SearchScreen

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MainScreen()
        }
    }
}

@Composable
private fun MainScreen() {
    val tabs = listOf("검색", "즐겨찾기")
    val pagerState = rememberPagerState { tabs.size }
    val scope = rememberCoroutineScope()

    Column(modifier = Modifier.fillMaxSize()) {
        Spacer(
            modifier = Modifier.height(
                WindowInsets.statusBars.asPaddingValues().calculateTopPadding()
            )
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .fillMaxSize()
            ) { page ->
                if (LocalInspectionMode.current.not()) {
                    when (page) {
                        0 -> SearchScreen()
                    }
                }
            }

            Column(
                modifier = Modifier
                    .padding(
                        start = SpaceToken._16,
                        end = SpaceToken._16,
                        bottom = SpaceToken._32
                    )
                    .align(Alignment.BottomCenter)
            ) {
                Row(
                    modifier = Modifier
                        .shadow(elevation = 4.dp, shape = CircleShape)
                        .background(Color.White, shape = CircleShape)
                ) {
                    for (tab in tabs) {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(40.dp)
                        ) {
                            LocalText(
                                text = tab,
                                modifier = Modifier.align(Alignment.Center)
                            )
                        }
                    }
                }

                Spacer(
                    modifier = Modifier.height(
                        WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()
                    )
                )
            }
        }
    }
}

@Preview(
    showBackground = true,
    showSystemUi = true
)
@Composable
private fun Preview() {
    MainScreen()
}