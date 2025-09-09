package com.yeogibook.search.result.presentation.units.header

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.yeogibook.R
import com.yeogibook.abcmm.presentation.core.LazyItem
import com.yeogibook.abcmm.presentation.ui.ds.padding
import com.yeogibook.abcmm.presentation.ui.ds.token.SpaceToken
import com.yeogibook.abcmm.presentation.ui.onClick
import com.yeogibook.search.result.presentation.vm.intent.SearchResultIntent

class SearchResultHeaderUiItem : LazyItem<SearchResultIntent>() {
    @Composable
    override fun BuildStickyItem(processIntent: (SearchResultIntent) -> Unit) {
        ConstraintLayout(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .background(Color.White)
        ) {
            val (logo, searchBox) = createRefs()
            Image(
                painter = painterResource(R.drawable.token_book_ribbon),
                contentDescription = null,
                modifier = Modifier
                    .height(40.dp)
                    .constrainAs(logo) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start, 16.dp)
                        bottom.linkTo(parent.bottom)
                    })

            Box(
                modifier = Modifier
                    .width(240.dp)
                    .height(40.dp)
                    .padding(end = SpaceToken._40)
                    .background(colorResource(R.color.gray200), CircleShape)
                    .constrainAs(searchBox) {
                        top.linkTo(parent.top)
                        end.linkTo(parent.end)
                        bottom.linkTo(parent.bottom)
                    }
                    .onClick(
                        onClick = {
                            processIntent(SearchResultIntent.OpenKeyIn)
                        }
                    )) {
                Image(
                    painter = painterResource(R.drawable.token_search),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(SpaceToken._10)
                        .align(Alignment.CenterEnd)
                )
            }
        }
    }

    @Composable
    override fun BuildItem(processIntent: (SearchResultIntent) -> Unit) {
        // No Op.
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
private fun Preview() {
    SearchResultHeaderUiItem().BuildStickyItem { }
}