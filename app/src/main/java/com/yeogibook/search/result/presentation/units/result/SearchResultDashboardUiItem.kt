package com.yeogibook.search.result.presentation.units.result

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.yeogibook.abcmm.presentation.core.LazyItem
import com.yeogibook.abcmm.presentation.ui.LocalText
import com.yeogibook.abcmm.presentation.ui.ds.padding
import com.yeogibook.abcmm.presentation.ui.ds.token.SpaceToken
import com.yeogibook.search.result.presentation.vm.intent.SearchResultIntent

data class SearchResultDashboardUiItem(private val query: AnnotatedString) :
    LazyItem<SearchResultIntent>() {
    @Composable
    override fun BuildItem(processIntent: (SearchResultIntent) -> Unit) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = SpaceToken._10)
        ) {
            LocalText(
                text = query,
                fontSize = 12.sp
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    SearchResultDashboardUiItem(AnnotatedString("'미움' 검색 결과 175건")).BuildItem { }
}