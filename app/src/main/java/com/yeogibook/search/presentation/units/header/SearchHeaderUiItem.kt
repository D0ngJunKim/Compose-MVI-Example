package com.yeogibook.search.presentation.units.header

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.yeogibook.abcmm.presentation.core.LazyItem
import com.yeogibook.abcmm.presentation.ui.LocalText
import com.yeogibook.search.presentation.vm.SearchIntent

class SearchHeaderUiItem : LazyItem<SearchIntent>() {
    @Composable
    override fun BuildItem(processIntent: (SearchIntent) -> Unit) {
        Column(modifier = Modifier.fillMaxWidth()) {
            LocalText("rkrkrk")
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    SearchHeaderUiItem()
}