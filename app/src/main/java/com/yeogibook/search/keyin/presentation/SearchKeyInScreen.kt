package com.yeogibook.search.keyin.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.clearText
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.skydoves.flexible.bottomsheet.material3.FlexibleBottomSheet
import com.skydoves.flexible.core.FlexibleSheetSize
import com.skydoves.flexible.core.FlexibleSheetState
import com.skydoves.flexible.core.FlexibleSheetValue
import com.skydoves.flexible.core.rememberFlexibleBottomSheetState
import com.yeogibook.R
import com.yeogibook.abcmm.presentation.core.AppState
import com.yeogibook.abcmm.presentation.core.rememberAppState
import com.yeogibook.abcmm.presentation.ui.LocalText
import com.yeogibook.abcmm.presentation.ui.LocalTextField
import com.yeogibook.abcmm.presentation.ui.ds.padding
import com.yeogibook.abcmm.presentation.ui.ds.token.SpaceToken
import com.yeogibook.abcmm.presentation.ui.ds.token.TextStyleToken
import com.yeogibook.abcmm.presentation.ui.onClick
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun SearchKeyInScreen(
    appState: AppState,
    extra: SearchKeyInExtra? = null,
    querySaveKey: String,
    keyboardType: KeyboardType = KeyboardType.Text,
    hintText: String = "검색어를 입력해 주세요.",
    onDismissRequest: () -> Unit
) {
    val focusRequester = remember { FocusRequester() }
    val sheetState = rememberFlexibleBottomSheetState(
        flexibleSheetSize = FlexibleSheetSize(
            fullyExpanded = 0.90f
        ),
        skipSlightlyExpanded = true,
        skipIntermediatelyExpanded = true,
        isModal = true
    )
    FlexibleBottomSheet(
        onDismissRequest = {
            onDismissRequest()
        },
        sheetState = sheetState,
        containerColor = Color.White,
        dragHandle = { Spacer(Modifier.height(20.dp)) },
        onTargetChanges = { value ->
            if (value == FlexibleSheetValue.FullyExpanded) {
                focusRequester.requestFocus()
            }
        }
    ) {
        BottomSheetContents(
            appState,
            extra,
            focusRequester,
            sheetState,
            querySaveKey,
            keyboardType,
            hintText,
            onDismissRequest
        )
    }
}

@Composable
private fun BottomSheetContents(
    appState: AppState,
    extra: SearchKeyInExtra?,
    focusRequester: FocusRequester,
    sheetState: FlexibleSheetState,
    querySaveKey: String,
    keyboardType: KeyboardType = KeyboardType.Text,
    hintText: String = "검색어를 입력해 주세요.",
    onDismissRequest: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = SpaceToken._16)
    ) {
        val queryState = rememberTextFieldState(extra?.query.orEmpty())
        val keyboardController = LocalSoftwareKeyboardController.current

        Box(modifier = Modifier.fillMaxWidth()) {
            LocalTextField(
                state = queryState,
                textStyle = TextStyleToken.Default.merge(
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium
                ),
                lineLimits = TextFieldLineLimits.SingleLine,
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        width = 1.dp,
                        color = colorResource(R.color.gray900),
                        shape = RoundedCornerShape(6.dp)
                    )
                    .padding(
                        start = SpaceToken._10,
                        top = SpaceToken._16,
                        end = SpaceToken._60,
                        bottom = SpaceToken._16
                    )
                    .focusRequester(focusRequester)
                    .onFocusChanged { state ->
                        if (state.isFocused) {
                            keyboardController?.show()
                        } else {
                            keyboardController?.hide()
                        }
                    },
                onKeyboardAction = {
                    appState.navController.currentBackStackEntry?.savedStateHandle?.set(
                        querySaveKey,
                        queryState.text
                    )
                    appState.scope.launch(Dispatchers.Main.immediate) {
                        sheetState.hide()
                        onDismissRequest()
                    }
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = keyboardType,
                    imeAction = ImeAction.Search
                ),
                decorator = { innerTextField ->
                    if (queryState.text.isEmpty()) {
                        LocalText(
                            text = hintText,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium,
                            color = colorResource(R.color.gray500),
                            includeFontPadding = false
                        )
                    } else {
                        innerTextField()
                    }
                }
            )

            if (queryState.text.isNotEmpty()) {
                Image(
                    painter = painterResource(R.drawable.token_cancel),
                    contentDescription = "입력한 검색어 삭제",
                    modifier = Modifier
                        .padding(end = SpaceToken._40)
                        .size(20.dp)
                        .align(Alignment.CenterEnd)
                        .onClick {
                            queryState.clearText()
                        },
                    colorFilter = ColorFilter.tint(colorResource(R.color.gray600))
                )
            }

            Image(
                painter = painterResource(R.drawable.token_search),
                contentDescription = null,
                modifier = Modifier
                    .padding(end = SpaceToken._10)
                    .size(20.dp)
                    .align(Alignment.CenterEnd)
            )
        }

    }
}

@Stable
data class SearchKeyInExtra(
    val query: String,
)

@Preview(showBackground = true)
@Composable
private fun Preview() {
    BottomSheetContents(
        rememberAppState(),
        SearchKeyInExtra("검색어를 입력해 주세요."),
        remember { FocusRequester() },
        rememberFlexibleBottomSheetState(),
        "",
    ) { }
}