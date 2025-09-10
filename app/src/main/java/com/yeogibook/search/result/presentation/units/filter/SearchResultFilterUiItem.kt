package com.yeogibook.search.result.presentation.units.filter

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.yeogibook.R
import com.yeogibook.abcmm.presentation.core.LazyItem
import com.yeogibook.abcmm.presentation.ui.LocalText
import com.yeogibook.abcmm.presentation.ui.RadioButton
import com.yeogibook.abcmm.presentation.ui.ds.padding
import com.yeogibook.abcmm.presentation.ui.ds.token.SpaceToken
import com.yeogibook.abcmm.presentation.ui.onClick
import com.yeogibook.search.result.data.constants.SearchResultSorts
import com.yeogibook.search.result.presentation.vm.intent.SearchResultIntent

data class SearchResultFilterUiItem(val sort: SearchResultSorts) : LazyItem<SearchResultIntent>() {
    override fun hasSticky(): Boolean = true

    @Composable
    override fun BuildStickyItem(processIntent: (SearchResultIntent) -> Unit) {
        var isExpanded by remember { mutableStateOf(false) }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(40.dp)
                .background(Color.White)
                .onClick {}
        ) {
            Row(
                modifier = Modifier
                    .padding(end = SpaceToken._16)
                    .align(Alignment.CenterEnd)
                    .border(
                        width = 1.dp,
                        color = colorResource(R.color.gray900),
                        shape = CircleShape
                    )
                    .padding(horizontal = SpaceToken._10, vertical = SpaceToken._4)
                    .onClick {
                        isExpanded = true
                    }
            ) {
                LocalText(
                    text = sort.typeName,
                    fontSize = 12.sp,
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                )

                Image(
                    painter = painterResource(R.drawable.token_sort),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(start = SpaceToken._2)
                        .size(16.dp)
                        .align(Alignment.CenterVertically)
                )
            }
        }

        if (isExpanded) {
            Dialog(onDismissRequest = {
                isExpanded = false
            }) {
                Column(
                    modifier = Modifier
                        .background(Color.White)
                        .width(180.dp)
                        .padding(horizontal = SpaceToken._16, vertical = SpaceToken._8),
                    verticalArrangement = Arrangement.spacedBy(4.dp, Alignment.Top)
                ) {
                    SearchResultSorts.entries.forEach { entry ->
                        val isSelected = entry == sort
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .padding(vertical = SpaceToken._8)
                                .onClick {
                                    isExpanded = false
                                    processIntent(SearchResultIntent.Sort(entry))
                                }) {
                            RadioButton(
                                selected = isSelected,
                                onClick = {
                                    isExpanded = false
                                    processIntent(SearchResultIntent.Sort(entry))
                                },
                                modifier = Modifier.align(Alignment.CenterVertically)
                            )

                            LocalText(
                                text = entry.typeName,
                                modifier = Modifier
                                    .padding(SpaceToken._4)
                                    .align(Alignment.CenterVertically)
                            )
                        }
                    }
                }
            }
        }
    }

    @Composable
    override fun BuildItem(processIntent: (SearchResultIntent) -> Unit) {
        // No Op.
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    SearchResultFilterUiItem(SearchResultSorts.ACCURACY).BuildStickyItem { }
}