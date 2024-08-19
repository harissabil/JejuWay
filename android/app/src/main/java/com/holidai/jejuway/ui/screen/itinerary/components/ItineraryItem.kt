package com.holidai.jejuway.ui.screen.itinerary.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
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
import com.holidai.jejuway.data.network.upstage_ai.dto.ScheduleItem
import com.holidai.jejuway.domain.models.itinerary.ActivityCategory.Companion.fromValue
import com.holidai.jejuway.utils.shimmerEffect

@Composable
fun ItineraryItem(
    modifier: Modifier = Modifier,
    scheduleItem: ScheduleItem,
) {
    var isExpanded by rememberSaveable { mutableStateOf(false) }

    OutlinedCard(
        modifier = modifier
            .fillMaxWidth(),
        onClick = { isExpanded = !isExpanded },
    ) {
        AnimatedContent(targetState = isExpanded, label = "itenararyItem") { expanded ->
            Column(
                modifier = Modifier.fillMaxWidth(),
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    IconButton(
                        modifier = Modifier
                            .padding(start = 16.dp),
                        onClick = { /*TODO*/ },
                        colors = IconButtonDefaults.iconButtonColors().copy(
                            containerColor = MaterialTheme.colorScheme.secondaryContainer,
                            contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                    ) {
                        Icon(
                            imageVector = scheduleItem.activityCategory.fromValue().icon,
                            contentDescription = scheduleItem.activityCategory
                        )
                    }
                    Column(
                        modifier = Modifier
                            .weight(3f)
                            .padding(vertical = 16.dp),
                        horizontalAlignment = Alignment.Start
                    ) {
                        Text(
                            modifier = Modifier.alpha(0.6f),
                            text = scheduleItem.time,
                            style = MaterialTheme.typography.bodyMedium,
                        )
                        Text(
                            text = scheduleItem.placeName,
                            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = scheduleItem.activityName,
                            style = MaterialTheme.typography.bodyMedium,
                            maxLines = 1
                        )
                    }

                    if (!expanded) {
                        SubcomposeAsyncImage(
                            model = ImageRequest.Builder(context = LocalContext.current)
                                .data(data = scheduleItem.photo_url)
                                .memoryCachePolicy(CachePolicy.ENABLED)
                                .memoryCacheKey(scheduleItem.id)
                                .diskCachePolicy(CachePolicy.ENABLED)
                                .diskCacheKey(scheduleItem.id)
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
                            contentDescription = scheduleItem.placeName,
                            modifier = Modifier
                                .weight(1.5f)
                                .aspectRatio(1f),
                            error = {
                                Box(
                                    modifier = Modifier
                                        .matchParentSize()
                                        .background(Color.Gray.copy(alpha = 0.5F))
                                )
                            }
                        )
                    }
                }

                if (expanded) {
                    SubcomposeAsyncImage(
                        model = ImageRequest.Builder(context = LocalContext.current)
                            .data(data = scheduleItem.photo_url)
                            .memoryCachePolicy(CachePolicy.ENABLED)
                            .memoryCacheKey(scheduleItem.id)
                            .diskCachePolicy(CachePolicy.ENABLED)
                            .diskCacheKey(scheduleItem.id)
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
                        contentDescription = scheduleItem.placeName,
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(1f),
                        error = {
                            Box(
                                modifier = Modifier
                                    .matchParentSize()
                                    .background(Color.Gray.copy(alpha = 0.5F))
                            )
                        }
                    )

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                            .padding(top = 16.dp)
                    ) {
                        Text(
                            text = scheduleItem.details,
                            style = MaterialTheme.typography.bodyMedium
                        )

                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            }
        }
    }
}
//
//@Preview(showBackground = true)
//@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
//@Composable
//private fun ItineraryItemPreview() {
//    JejuAItineraryTheme {
//        ItineraryItem(
//            scheduleItem =
//            ScheduleItem(
//                id = "6",
//                tripDay = 4,
//                date = "2022-10-13",
//                lat = 33.489011,
//                long = 126.498302,
//                placeName = "Seongsan Ilchulbong",
//                address = "284-12, Ilchul-ro, Seongsan-eup, Seogwipo-si, Jeju-do",
//                details = "Hiking and enjoy the sunrise at the UNESCO World Heritage site.",
//                time = "10:00",
//                completed = false,
//                photoUrl = null,
//                activityName = "Visit Seongsan Ilchulbong Peak",
//                activityCategory = "Nature",
//                itineraryId = "unique-id-123",
//                order = 6
//            )
//        )
//    }
//}