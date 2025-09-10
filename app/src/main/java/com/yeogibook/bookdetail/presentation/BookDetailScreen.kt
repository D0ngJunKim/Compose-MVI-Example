package com.yeogibook.bookdetail.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.core.graphics.drawable.toDrawable
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import coil3.request.placeholder
import coil3.size.Scale
import com.google.gson.Gson
import com.yeogibook.R
import com.yeogibook.abcmm.data.entity.BookDocumentDiData
import com.yeogibook.abcmm.presentation.core.AppState
import com.yeogibook.abcmm.presentation.core.rememberAppState
import com.yeogibook.abcmm.presentation.extenstion.toCommaFormatOrNull
import com.yeogibook.abcmm.presentation.extenstion.toDatePatternOf
import com.yeogibook.abcmm.presentation.ui.LocalText
import com.yeogibook.abcmm.presentation.ui.LocalTitleBar
import com.yeogibook.abcmm.presentation.ui.TitleBarButtonUiData
import com.yeogibook.abcmm.presentation.ui.ds.padding
import com.yeogibook.abcmm.presentation.ui.ds.token.SpaceToken
import com.yeogibook.abcmm.presentation.ui.onClick
import com.yeogibook.favorite.presentation.util.FavoriteBookManager

@Composable
fun BookDetailScreen(appState: AppState, data: BookDocumentDiData?) {
    if (data != null) {
        Column(
            Modifier
                .fillMaxSize()
                .padding(horizontal = SpaceToken._16)
        ) {
            Spacer(
                modifier = Modifier.height(
                    WindowInsets.statusBars.asPaddingValues().calculateTopPadding()
                )
            )
            LocalTitleBar(
                text = "",
                leftButton = TitleBarButtonUiData(R.drawable.token_chevron_left, "뒤로가기") {
                    appState.navigateBack()
                })

            Row(Modifier.fillMaxWidth()) {
                Box {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(data.thumbnail)
                            .scale(Scale.FILL)
                            .crossfade(true)
                            .placeholder(colorResource(R.color.gray100).toArgb().toDrawable())
                            .build(),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth(0.4f)
                            .aspectRatio(0.8f)
                    )

                    var isFavorite by remember(data) {
                        mutableStateOf(
                            FavoriteBookManager.isFavoriteBook(data)
                        )
                    }

                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .onClick(
                                onClickLabel = "즐겨찾기",
                                role = Role.Button
                            ) {
                                if (isFavorite) {
                                    FavoriteBookManager.removeFavoriteBook(data)
                                } else {
                                    FavoriteBookManager.addFavoriteBook(data)
                                }
                                isFavorite = isFavorite.not()
                            }
                            .align(Alignment.BottomEnd)
                    ) {
                        Image(
                            painterResource(R.drawable.token_favorite),
                            contentDescription = null,
                            colorFilter = if (isFavorite) ColorFilter.tint(Color.Red) else null,
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }

                }

                Spacer(modifier = Modifier.size(10.dp))

                Column {
                    LocalText(
                        text = data.title.orEmpty(),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .fillMaxWidth()

                    )
                    FlowRow(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = SpaceToken._10),
                        horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.Start)
                    ) {
                        val authors = remember {
                            val text = data.authors?.joinToString(", ")
                            if (text.isNullOrEmpty()) {
                                null
                            } else {
                                "$text (지은이)"
                            }
                        }

                        if (!authors.isNullOrEmpty()) {
                            LocalText(
                                text = authors,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Medium,
                                color = colorResource(R.color.gray600),
                            )
                        }

                        val translators = remember {
                            val text = data.translators?.joinToString(", ")
                            if (text.isNullOrEmpty()) {
                                null
                            } else {
                                "$text (옮긴이)"
                            }
                        }

                        if (!translators.isNullOrEmpty()) {
                            LocalText(
                                text = translators,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Medium,
                                color = colorResource(R.color.gray600),
                            )
                        }

                        LocalText(
                            text = data.publisher.orEmpty(),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium,
                            color = colorResource(R.color.gray600),
                        )

                        val date = remember { data.datetime?.toDatePatternOf("yyyy-MM-dd") }

                        if (!date.isNullOrEmpty()) {
                            LocalText(
                                text = date,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Medium,
                                color = colorResource(R.color.gray600),
                            )
                        }
                    }
                    ConstraintLayout(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = SpaceToken._10)
                    ) {
                        val (vPriceLabel, vPrice, vSalePriceLabel, vSalePrice) = createRefs()
                        val barrier = createEndBarrier(
                            elements = arrayOf(vPriceLabel, vSalePriceLabel),
                            margin = 8.dp
                        )
                        val price = remember { data.price.toCommaFormatOrNull()?.plus("원") }

                        if (!price.isNullOrEmpty()) {
                            LocalText(
                                text = "정가",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Medium,
                                color = colorResource(R.color.gray900),
                                modifier = Modifier
                                    .constrainAs(vPriceLabel) {
                                        top.linkTo(parent.top)
                                        start.linkTo(parent.start)
                                    }
                            )

                            LocalText(
                                text = price,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Medium,
                                color = colorResource(R.color.gray900),
                                textDecoration = TextDecoration.LineThrough,
                                modifier = Modifier.constrainAs(vPrice) {
                                    top.linkTo(parent.top)
                                    start.linkTo(barrier)
                                    end.linkTo(parent.end)
                                    width = Dimension.fillToConstraints
                                }
                            )
                        }

                        val salePrice = remember { data.salePrice.toCommaFormatOrNull()?.plus("원") }

                        if (!salePrice.isNullOrEmpty()) {
                            LocalText(
                                text = "판매가",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = colorResource(R.color.gray900),
                                modifier = Modifier
                                    .constrainAs(vSalePriceLabel) {
                                        top.linkTo(vPriceLabel.bottom, 4.dp, 0.dp)
                                        start.linkTo(parent.start)
                                    }
                            )

                            LocalText(
                                text = salePrice,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = colorResource(R.color.gray900),
                                modifier = Modifier.constrainAs(vSalePrice) {
                                    top.linkTo(vPrice.bottom, 4.dp, 0.dp)
                                    start.linkTo(barrier)
                                    end.linkTo(parent.end)
                                    width = Dimension.fillToConstraints
                                }
                            )
                        }
                    }
                }
            }

            Spacer(
                modifier = Modifier
                    .padding(vertical = SpaceToken._12)
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(color = colorResource(R.color.gray400))
            )

            LocalText(
                text = "기본 정보",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .fillMaxWidth()
            )

            val isbn = remember { "ISBN : ${data.isbn?.replace(" ", ", ")}" }

            LocalText(
                text = isbn,
                fontSize = 12.sp,
                color = colorResource(R.color.gray700),
                modifier = Modifier
                    .padding(top = SpaceToken._10)
                    .fillMaxWidth()
            )

            Spacer(
                modifier = Modifier
                    .padding(vertical = SpaceToken._12)
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(color = colorResource(R.color.gray400))
            )

            LocalText(
                text = "책 소개",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .fillMaxWidth()
            )

            LocalText(
                text = data.contents.orEmpty(),
                fontSize = 12.sp,
                color = colorResource(R.color.gray700),
                lineHeight = 18.sp,
                letterSpacing = 1.sp,
                modifier = Modifier
                    .padding(top = SpaceToken._10)
                    .fillMaxWidth()
            )

            Spacer(
                modifier = Modifier
                    .padding(vertical = SpaceToken._12)
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(color = colorResource(R.color.gray400))
            )

            Spacer(
                modifier = Modifier.height(
                    WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()
                )
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun Preview() {
    val json =
        "{\"authors\":[\"ㄱ의자식들\"],\"contents\":\"시모임[ㄱ의자식들]의첫시집『ㄱ』.각자사는곳도하는일도다른강수경,김태일,김정현,이록현,서윤선,선우원,최영식,한민규8명이모인이들은서로만나추천한8편의시를읽고다시짧은형식의시로그감흥을옮겨보기도하고,각자의자작시들을들여다보고벗겨보고서로담아나눠가졌다.\",\"datetime\":\"2015-12-15T00:00:00.000+09:00\",\"isbn\":\"89619512119788961951210\",\"price\":11000,\"publisher\":\"갈무리\",\"sale_price\":9900,\"status\":\"정상판매\",\"thumbnail\":\"https://search1.kakaocdn.net/thumb/R120x174.q85/?fname=http%3A%2F%2Ft1.daumcdn.net%2Flbook%2Fimage%2F860270%3Ftimestamp%3D20230810210232\",\"title\":\"ㄱ\",\"translators\":[],\"url\":\"https://search.daum.net/search?w=bookpage&bookId=860270&q=%E3%84%B1\"}"
    BookDetailScreen(
        rememberAppState(),
        Gson().fromJson(json, BookDocumentDiData::class.java)
    )
}