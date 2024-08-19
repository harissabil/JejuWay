package com.holidai.jejuway.ui.screen.home_navigator.trips.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.holidai.jejuway.data.network.upstage_ai.dto.TestingLlmResponse
import com.holidai.jejuway.ui.theme.spacing
import com.holidai.jejuway.utils.shimmerEffect

@Composable
fun TripsItem(
    modifier: Modifier = Modifier,
    testingLlmResponse: TestingLlmResponse,
    onItemClick: (testingLlmResponse: TestingLlmResponse) -> Unit,
    onMoreClick: (testingLlmResponse: TestingLlmResponse) -> Unit,
) {
    val context = LocalContext.current

    val randomImages = listOf(
        "https://cf.bstatic.com/xdata/images/hotel/max1024x768/91419386.jpg?k=f130919945dc8eaf16d026f7dfcfef44b9785e294f594a3088443d7fb7fcb7fd&o=&hp=1",
        "https://jejutourism.wordpress.com/wp-content/uploads/2014/08/49.jpg",
        "https://dynamic-media-cdn.tripadvisor.com/media/photo-o/09/d2/f7/fd/rajmahal-indian-restaurant.jpg?w=1600&h=-1&s=1",
        "https://tong.visitkorea.or.kr/cms/resource/55/2406855_image2_1.jpg",
        "https://api.cdn.visitjeju.net/photomng/imgpath/202008/21/60e6d2a4-064c-49b3-919a-dbb812c86969.JPG",
        "https://media-cdn.tripadvisor.com/media/photo-m/1280/18/37/3f/06/kimchi-stew.jpg",
        "https://www.korvia.com/wp-content/uploads/2015/08/korvia-image-header-tapdong-jeju-emerging-indie-art-district-bay.jpg",
        "https://i0.wp.com/jejutourism.wordpress.com/wp-content/uploads/2014/09/img_5568.jpg?ssl=1",
        "https://upload.wikimedia.org/wikipedia/commons/9/9d/Hallasan_Above.jpg"
    )

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.large)
            .background(color = MaterialTheme.colorScheme.secondaryContainer)
            .clickable { onItemClick(testingLlmResponse) }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .animateContentSize()
                .padding(MaterialTheme.spacing.medium),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium)
        ) {
            SubcomposeAsyncImage(
                model = ImageRequest.Builder(context = context)
                    .data(data = randomImages.random())
                    .memoryCachePolicy(CachePolicy.ENABLED)
                    .diskCachePolicy(CachePolicy.ENABLED)
                    .allowHardware(false)
                    .allowRgb565(true)
                    .crossfade(enable = true)
                    .build(),
                contentScale = ContentScale.Crop,
                colorFilter = ColorFilter.colorMatrix(
                    colorMatrix = ColorMatrix().apply {
                        setToSaturation(sat = 0.85F)
                    }
                ),
                loading = {
                    Box(
                        modifier = Modifier
                            .matchParentSize()
                            .background(Color.Gray.copy(alpha = 0.5F))
                            .shimmerEffect()
                    )
                },
                filterQuality = FilterQuality.Medium,
                alignment = Alignment.Center,
                contentDescription = testingLlmResponse.itineraryId ?: "N/A",
                modifier = Modifier
                    .size(width = 100.dp, height = 100.dp)
                    .clip(MaterialTheme.shapes.large),
                error = {
                    Box(
                        modifier = Modifier
                            .matchParentSize()
                            .background(Color.Gray.copy(alpha = 0.5F))
                    )
                }
            )

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small),
                horizontalAlignment = Alignment.Start
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.Top
                ) {
                    Column(
                        modifier = Modifier.weight(1f),
                    ) {
                        Text(
                            text = "Created at ${testingLlmResponse.createdAt}",
                            style = MaterialTheme.typography.labelMedium,
                            overflow = TextOverflow.Ellipsis,
                            maxLines = 1,
                            modifier = Modifier
                                .alpha(0.5f)
                                .padding(end = MaterialTheme.spacing.medium)
                        )
                        Row(modifier = Modifier.fillMaxWidth()) {
                            Column(modifier = Modifier.padding(end = MaterialTheme.spacing.small)) {
                                Text(
                                    text = "Start",
                                    style = MaterialTheme.typography.titleMedium,
                                    overflow = TextOverflow.Ellipsis,
                                    maxLines = 1,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Text(
                                    text = "End",
                                    style = MaterialTheme.typography.titleMedium,
                                    overflow = TextOverflow.Ellipsis,
                                    maxLines = 1,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                            Column(modifier = Modifier.fillMaxWidth()) {
                                Text(
                                    text = ": ${testingLlmResponse.startDate}",
                                    style = MaterialTheme.typography.titleMedium,
                                    overflow = TextOverflow.Ellipsis,
                                    maxLines = 2,
                                    fontWeight = FontWeight.SemiBold,
                                    modifier = Modifier.padding(end = MaterialTheme.spacing.medium)
                                )
                                Text(
                                    text = ": ${testingLlmResponse.endDate}",
                                    style = MaterialTheme.typography.titleMedium,
                                    overflow = TextOverflow.Ellipsis,
                                    maxLines = 2,
                                    fontWeight = FontWeight.SemiBold,
                                    modifier = Modifier.padding(end = MaterialTheme.spacing.medium)
                                )
                            }
                        }
                    }
                    Icon(
                        imageVector = Icons.Default.MoreHoriz,
                        contentDescription = "More options",
                        modifier = Modifier
                            .alpha(0.5f)
                            .clip(RoundedCornerShape(percent = 100))
                            .clickable { onMoreClick(testingLlmResponse) },
                    )
                }

                Row(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(end = MaterialTheme.spacing.small)) {
                        Text(
                            text = "Number of People",
                            style = MaterialTheme.typography.titleSmall,
                            overflow = TextOverflow.Ellipsis,
                            maxLines = 1,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = ": ${testingLlmResponse.numberOfPeople}",
                            style = MaterialTheme.typography.titleSmall,
                            overflow = TextOverflow.Ellipsis,
                            maxLines = 1,
                        )
                    }
                }
            }
        }
    }
}