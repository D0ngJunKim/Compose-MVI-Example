package com.yeogibook.abcmm.presentation.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.yeogibook.R
import com.yeogibook.abcmm.presentation.ui.ds.padding
import com.yeogibook.abcmm.presentation.ui.ds.token.SpaceToken

@Composable
fun LocalTitleBar(
    title: String,
    modifier: Modifier = Modifier,
    leftButton: TitleBarButtonUiData? = null,
    rightButtons: List<TitleBarButtonUiData> = listOf()
) {
    val buttonSize = 28.dp
    val leftButtonWidth = if (leftButton != null) buttonSize else 0.dp
    val rightButtonsWidth = buttonSize * rightButtons.size
    val maxPadding = maxOf(leftButtonWidth, rightButtonsWidth)

    Box(
        modifier
            .padding(
                start = SpaceToken._8,
                end = SpaceToken._8
            )
            .fillMaxWidth()
            .height(40.dp)
    ) {
        if (leftButton != null) {
            Image(
                painter = painterResource(leftButton.iconResId),
                contentDescription = leftButton.iconDescription,
                modifier = Modifier
                    .size(buttonSize)
                    .padding(2.dp)
                    .align(Alignment.CenterStart)
                    .clickable(onClick = leftButton.onClick),
            )
        }

        LocalText(
            text = title,
            modifier = Modifier
                .align(Alignment.Center)
                .padding(horizontal = maxPadding),
            fontWeight = FontWeight.Medium,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        Row(
            modifier = Modifier
                .height(buttonSize)
                .align(Alignment.CenterEnd)
        ) {
            for (rightButton in rightButtons) {
                Image(
                    painter = painterResource(rightButton.iconResId),
                    contentDescription = rightButton.iconDescription,
                    modifier = Modifier
                        .size(buttonSize)
                        .padding(SpaceToken._2)
                        .clickable(onClick = rightButton.onClick)
                )
            }
        }
    }
}

@Immutable
data class TitleBarButtonUiData(
    val iconResId: Int,
    val iconDescription: String,
    val onClick: () -> Unit
)

@Preview(showBackground = true)
@Composable
private fun Preview() {
    LocalTitleBar(
        "타이틀바 일이삼사오육칠팔구 일이삼사오육칠팔구 일이삼사오육칠팔구 일이삼사오육칠팔구 일이삼사오육칠팔구 일이삼사오육칠팔구",
        leftButton = TitleBarButtonUiData(R.drawable.token_chevron_left, "") {},
        rightButtons = listOf(
            TitleBarButtonUiData(R.drawable.token_sort, "") {},
            TitleBarButtonUiData(R.drawable.token_favorite, "") {}
        )
    )
}