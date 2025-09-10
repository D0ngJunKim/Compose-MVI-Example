package com.yeogibook.search.result.presentation.units.header

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.yeogibook.R
import com.yeogibook.abcmm.presentation.core.LazyItem
import com.yeogibook.abcmm.presentation.ui.LocalText
import com.yeogibook.abcmm.presentation.ui.ds.padding
import com.yeogibook.abcmm.presentation.ui.ds.token.SpaceToken
import com.yeogibook.abcmm.presentation.ui.onClick
import com.yeogibook.search.result.presentation.vm.intent.SearchResultIntent

data class SearchResultHeaderUiItem(private val query: String?) : LazyItem<SearchResultIntent>() {
    @Composable
    override fun BuildItem(processIntent: (SearchResultIntent) -> Unit) {
        ConstraintLayout(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .background(Color.White)
        ) {
            val (logo, searchBox) = createRefs()
            Row(
                modifier = Modifier
                    .height(40.dp)
                    .constrainAs(logo) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start, 16.dp)
                        bottom.linkTo(parent.bottom)
                    }) {
                Image(
                    painter = painterResource(R.drawable.token_book_ribbon),
                    contentDescription = null,
                    modifier = Modifier.align(Alignment.CenterVertically)
                )
            }


            Row(
                modifier = Modifier
                    .height(40.dp)
                    .padding(end = SpaceToken._40)
                    .background(colorResource(R.color.gray200), CircleShape)
                    .constrainAs(searchBox) {
                        top.linkTo(parent.top)
                        start.linkTo(logo.end, 16.dp)
                        end.linkTo(parent.end)
                        bottom.linkTo(parent.bottom)
                        width = Dimension.fillToConstraints
                    }
                    .onClick {
                        processIntent(SearchResultIntent.OpenKeyIn)
                    }
            ) {
                LocalText(
                    text = query.orEmpty(),
                    fontSize = 12.sp,
                    color = colorResource(R.color.gray900),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(SpaceToken._10)
                        .align(Alignment.CenterVertically)
                        .weight(1f)
                )

                Image(
                    painter = painterResource(R.drawable.token_search),
                    contentDescription = null,
                    modifier = Modifier.Companion
                        .padding(SpaceToken._10)
                        .align(Alignment.CenterVertically)
                )
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
private fun Preview() {
    SearchResultHeaderUiItem("감자").BuildItem { }
}