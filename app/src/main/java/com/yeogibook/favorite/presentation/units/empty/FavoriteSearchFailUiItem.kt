package com.yeogibook.favorite.presentation.units.empty

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.yeogibook.R
import com.yeogibook.abcmm.presentation.core.LazyItem
import com.yeogibook.abcmm.presentation.ui.LocalText
import com.yeogibook.abcmm.presentation.ui.ds.padding
import com.yeogibook.abcmm.presentation.ui.ds.token.SpaceToken
import com.yeogibook.favorite.presentation.vm.intent.FavoriteIntent

class FavoriteSearchFailUiItem : LazyItem<FavoriteIntent>() {

    @Composable
    override fun BuildItem(processIntent: (FavoriteIntent) -> Unit) {
        ConstraintLayout(
            modifier = Modifier.Companion
                .fillMaxWidth()
                .padding(horizontal = SpaceToken._16, vertical = SpaceToken._60)
        ) {
            val (image, text) = createRefs()
            Image(
                painter = painterResource(R.drawable.token_sentiment_dissatisfied),
                contentDescription = null,
                modifier = Modifier.Companion.constrainAs(image) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(text.top)
                }
            )

            LocalText(
                text = "검색된 책이 없습니다.",
                fontSize = 13.sp,
                color = colorResource(R.color.gray900),
                modifier = Modifier.Companion.constrainAs(text) {
                    top.linkTo(image.bottom, SpaceToken._4.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                },
                maxLines = 1
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    FavoriteSearchFailUiItem().BuildItem { }
}