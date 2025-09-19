package com.yeogibook.search.result.presentation.units.item

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
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
import com.yeogibook.R
import com.yeogibook.abcmm.data.entity.BookDocumentDiData
import com.yeogibook.abcmm.presentation.core.LazyItem
import com.yeogibook.abcmm.presentation.core.ListSpan
import com.yeogibook.abcmm.presentation.ui.LocalText
import com.yeogibook.abcmm.presentation.ui.card
import com.yeogibook.abcmm.presentation.ui.ds.padding
import com.yeogibook.abcmm.presentation.ui.ds.token.SpaceToken
import com.yeogibook.abcmm.presentation.ui.onClick
import com.yeogibook.favorite.presentation.util.FavoriteBookManager
import com.yeogibook.search.result.presentation.vm.intent.SearchResultIntent

data class SearchResultItemUiItem(
    private val imageUrl: String?,
    private val saleStatus: String?,
    private val title: String?,
    private val subTitle: String?,
    private val originPrice: String?,
    private val displayPrice: String?,
    private val origin: BookDocumentDiData,
) : LazyItem<SearchResultIntent>(ListSpan.SINGLE_FOR_ALL) {

    override fun itemKey(): Int {
        return origin.isbn?.replace(" ", "")?.toIntOrNull() ?: super.itemKey()
    }

    @Composable
    override fun BuildItem(processIntent: (SearchResultIntent) -> Unit) {
        val isInspectionMode = LocalInspectionMode.current

        ConstraintLayout(
            modifier = Modifier
                .padding(vertical = SpaceToken._4)
                .fillMaxWidth()
                .card(
                    shape = RoundedCornerShape(10.dp),
                    shadow = Shadow(
                        radius = 2.dp,
                        spread = 0.dp,
                        offset = DpOffset(0.dp, 1.dp),
                        color = colorResource(R.color.black_alpha30)
                    )
                )
                .padding(horizontal = SpaceToken._16, vertical = SpaceToken._10)
                .onClick {
                    processIntent(SearchResultIntent.OpenDetail(origin))
                }
        ) {
            val (vThumbnail, vStatus, vTitle, vSubTitle, vOriginPrice, vDispPrice, btnFavorite) = createRefs()
            val leftBarrier = createEndBarrier(margin = 10.dp, elements = arrayOf(vThumbnail))
            val rightBarrier = createStartBarrier(margin = (-4).dp, elements = arrayOf(btnFavorite))

            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(imageUrl)
                    .scale(Scale.FILL)
                    .crossfade(true)
                    .placeholder(colorResource(R.color.gray100).toArgb().toDrawable())
                    .build(),
                contentDescription = null,
                modifier = Modifier
                    .width(80.dp)
                    .height(100.dp)
                    .constrainAs(vThumbnail) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                    }
            )

            if (!saleStatus.isNullOrEmpty()) {
                Box(
                    modifier = Modifier
                        .background(colorResource(R.color.black_alpha10))
                        .constrainAs(vStatus) {
                            width = Dimension.fillToConstraints
                            start.linkTo(vThumbnail.start)
                            end.linkTo(vThumbnail.end)
                            bottom.linkTo(vThumbnail.bottom)
                        }) {
                    LocalText(
                        text = saleStatus,
                        fontSize = 10.sp,
                        color = colorResource(R.color.gray900),
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(SpaceToken._2)
                    )
                }
            }

            LocalText(
                text = title.orEmpty(),
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = colorResource(R.color.gray900),
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.constrainAs(vTitle) {
                    start.linkTo(leftBarrier)
                    top.linkTo(parent.top)
                    end.linkTo(rightBarrier)
                    width = Dimension.fillToConstraints
                })

            LocalText(
                text = subTitle.orEmpty(),
                fontSize = 10.sp,
                color = colorResource(R.color.gray600),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.constrainAs(vSubTitle) {
                    start.linkTo(leftBarrier)
                    top.linkTo(vTitle.bottom)
                    end.linkTo(rightBarrier)
                    width = Dimension.fillToConstraints
                })

            LocalText(
                text = displayPrice.orEmpty(),
                fontSize = 14.sp,
                color = colorResource(R.color.gray900),
                fontWeight = FontWeight.SemiBold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.constrainAs(vDispPrice) {
                    start.linkTo(leftBarrier)
                    top.linkTo(vSubTitle.bottom, 8.dp)
                    end.linkTo(vDispPrice.start)
                })

            if (!originPrice.isNullOrEmpty()) {
                LocalText(
                    text = originPrice,
                    fontSize = 10.sp,
                    color = colorResource(R.color.gray500),
                    fontWeight = FontWeight.Medium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    textDecoration = TextDecoration.LineThrough,
                    modifier = Modifier.constrainAs(vOriginPrice) {
                        start.linkTo(vDispPrice.end, 4.dp)
                        top.linkTo(vDispPrice.top)
                        bottom.linkTo(vDispPrice.bottom)
                        end.linkTo(rightBarrier)
                        horizontalBias = 0f
                    })
            }
            var isFavorite by remember(origin) {
                mutableStateOf(
                    if (isInspectionMode) false else FavoriteBookManager.isFavoriteBook(origin)
                )
            }

            if (isInspectionMode.not()) {
                DisposableEffect(origin) {
                    val listener = {
                        isFavorite = FavoriteBookManager.isFavoriteBook(origin)
                    }
                    FavoriteBookManager.addListener(listener)

                    onDispose {
                        FavoriteBookManager.removeListener(listener)
                    }
                }
            }

            Box(
                modifier = Modifier
                    .size(32.dp)
                    .onClick(
                        onClickLabel = "즐겨찾기",
                        role = Role.Button
                    ) {
                        if (isFavorite) {
                            FavoriteBookManager.removeFavoriteBook(origin)
                        } else {
                            FavoriteBookManager.addFavoriteBook(origin)
                        }
                    }
                    .constrainAs(btnFavorite) {
                        top.linkTo(parent.top)
                        end.linkTo(parent.end)
                    }
            ) {
                Image(
                    painterResource(R.drawable.token_favorite),
                    contentDescription = null,
                    colorFilter = if (isFavorite) ColorFilter.tint(Color.Red) else null,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    SearchResultItemUiItem(
        imageUrl = "",
        saleStatus = "정상판매",
        title = "미\n움\n받\n을 용기",
        subTitle = "권정열",
        originPrice = "120,000원",
        displayPrice = "100,000원",
        origin = BookDocumentDiData()
    ).BuildItem { }
}