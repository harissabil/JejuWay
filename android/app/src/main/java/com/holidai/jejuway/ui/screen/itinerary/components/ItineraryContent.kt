package com.holidai.jejuway.ui.screen.itinerary.components

import android.content.res.Configuration
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.model.LatLng
import com.holidai.jejuway.data.network.upstage_ai.dto.ScheduleItem
import com.holidai.jejuway.ui.screen.itinerary.LocalMenuItemState
import java.util.UUID

@Composable
fun ItineraryContent(
    modifier: Modifier = Modifier,
    schedules: List<ScheduleItem>,
    lazyListState: LazyListState,
    onDismissedToEnd: (ScheduleItem) -> Unit,
    onDismissedToStart: (ScheduleItem) -> Unit,
) {
    var selectedTab by rememberSaveable { mutableIntStateOf(1) }
    val selectedItems = schedules.filter { it.tripDay == selectedTab }
    val isPortrait = LocalConfiguration.current.orientation == Configuration.ORIENTATION_PORTRAIT
    var isFullscreen by rememberSaveable { mutableStateOf(false) }
    val mapSize: Float by animateFloatAsState(if (isFullscreen) 1f else 2.7f, label = "mapSize")
    val menu = LocalMenuItemState.current

    ConfigureMenuItems(menu, isFullscreen) {
        isFullscreen = it
    }

    val mapModifier = if (isPortrait) {
        Modifier
            .fillMaxWidth()
            .height(LocalConfiguration.current.screenHeightDp.div(mapSize).dp)
    } else {
        Modifier
            .fillMaxHeight()
            .width(LocalConfiguration.current.screenWidthDp.div(mapSize).dp)
    }

    Column(modifier = modifier.fillMaxWidth()) {
        DestinationMap(
            destinationCoordinates = LatLng(33.489011, 126.498302),
            items = selectedItems,
            modifier = mapModifier
        )
        Column {
            DaysTab(
                schedules = schedules,
                onDaySelected = { selectedTab = it }
            )
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                state = lazyListState,
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(
                    start = 16.dp, end = 16.dp,
                    top = 0.dp, bottom = 12.dp
                )
            ) {
                items(items = selectedItems, key = { UUID.randomUUID() }) { schedule ->

                    SwipeToFinishContainer(
                        item = schedule,
                        onDismissedToEnd = onDismissedToEnd,
                        onDismissedToStart = {
                            onDismissedToStart(it)
                        },
                        content = {
                            ItineraryItem(
                                scheduleItem = schedule,
                            )
                        },
                    )
                }

                item { Spacer(modifier = Modifier.height(64.dp)) }
            }
        }
    }
}