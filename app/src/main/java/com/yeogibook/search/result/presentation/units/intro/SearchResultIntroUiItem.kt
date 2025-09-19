package com.yeogibook.search.result.presentation.units.intro

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
import com.yeogibook.search.result.presentation.vm.intent.SearchResultIntent

class SearchResultIntroUiItem : LazyItem<SearchResultIntent>() {

    @Composable
    override fun BuildItem(processIntent: (SearchResultIntent) -> Unit) {
        ConstraintLayout(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = SpaceToken._60)
        ) {
            val (image, text) = createRefs()
            Image(
                painter = painterResource(R.drawable.token_globe_book),
                contentDescription = null,
                modifier = Modifier.constrainAs(image) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(text.top)
                }
            )

            LocalText(
                text = "원하시는 도서를 검색해보세요.",
                fontSize = 13.sp,
                color = colorResource(R.color.gray900),
                modifier = Modifier.constrainAs(text) {
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
    SearchResultIntroUiItem().BuildItem { }
}