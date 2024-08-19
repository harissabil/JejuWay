package com.holidai.jejuway.ui.screen.itinerary.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.holidai.jejuway.data.network.upstage_ai.dto.ScheduleItem
import com.holidai.jejuway.utils.toShortDate

@Composable
fun DaysTab(
    modifier: Modifier = Modifier,
    schedules: List<ScheduleItem>,
    onDaySelected: (Int) -> Unit
) {
    val days = schedules.groupBy { it.tripDay }.toList()
    LazyRow(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(vertical = 12.dp)
    ) {
        item {
            Spacer(modifier = Modifier.width(0.dp))
        }
        items(items = days, key = { it.first }) { item ->
            DayItem(
                dayValue = item.first,
                date = item.second[0].date.toShortDate(),
                onDaySelected = onDaySelected
            )
        }
        item {
            Spacer(modifier = Modifier.width(0.dp))
        }
    }
}

@Composable
fun DayItem(
    modifier: Modifier = Modifier,
    dayValue: Int,
    date: String,
    onDaySelected: (Int) -> Unit
) {
    OutlinedCard(
        onClick = { onDaySelected(dayValue) },
    ) {
        Column(
            modifier = modifier
                .widthIn(140.dp)
                .padding(vertical = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = "Day $dayValue",
                style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold)
            )
            Text(
                text = date,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

//@Preview(showBackground = true)
//@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
//@Composable
//private fun DaysTabPreview() {
//    JejuAItineraryTheme {
//        Surface {
//            DaysTab(
//                onDaySelected = {},
//                schedules = listOf(
//                    ScheduleItem(
//                        id = "1",
//                        tripDay = 1,
//                        date = "2022-10-10",
//                        lat = 33.489011,
//                        long = 126.498302,
//                        placeName = "Seongsan Ilchulbong",
//                        address = "284-12, Ilchul-ro, Seongsan-eup, Seogwipo-si, Jeju-do",
//                        details = "Hiking and enjoy the sunrise at the UNESCO World Heritage site.",
//                        time = "10:00",
//                        completed = false,
//                        photoUrl = null,
//                        activityName = "Visit Seongsan Ilchulbong Peak",
//                        activityCategory = "Nature",
//                        itineraryId = "unique-id-123",
//                        order = 1
//                    ),
//                    ScheduleItem(
//                        id = "2",
//                        tripDay = 1,
//                        date = "2022-10-10",
//                        lat = 33.489011,
//                        long = 126.498302,
//                        placeName = "Seongsan Ilchulbong",
//                        address = "284-12, Ilchul-ro, Seongsan-eup, Seogwipo-si, Jeju-do",
//                        details = "Hiking and enjoy the sunrise at the UNESCO World Heritage site.",
//                        time = "10:00",
//                        completed = false,
//                        photoUrl = null,
//                        activityName = "Visit Seongsan Ilchulbong Peak",
//                        activityCategory = "Nature",
//                        itineraryId = "unique-id-123",
//                        order = 2
//                    ),
//                    ScheduleItem(
//                        id = "3",
//                        tripDay = 2,
//                        date = "2022-10-11",
//                        lat = 33.489011,
//                        long = 126.498302,
//                        placeName = "Seongsan Ilchulbong",
//                        address = "284-12, Ilchul-ro, Seongsan-eup, Seogwipo-si, Jeju-do",
//                        details = "Hiking and enjoy the sunrise at the UNESCO World Heritage site.",
//                        time = "10:00",
//                        completed = false,
//                        photoUrl = null,
//                        activityName = "Visit Seongsan Ilchulbong Peak",
//                        activityCategory = "Nature",
//                        itineraryId = "unique-id-123",
//                        order = 3
//                    ),
//                    ScheduleItem(
//                        id = "4",
//                        tripDay = 2,
//                        date = "2022-10-11",
//                        lat = 33.489011,
//                        long = 126.498302,
//                        placeName = "Seongsan Ilchulbong",
//                        address = "284-12, Ilchul-ro, Seongsan-eup, Seogwipo-si, Jeju-do",
//                        details = "Hiking and enjoy the sunrise at the UNESCO World Heritage site.",
//                        time = "10:00",
//                        completed = false,
//                        photoUrl = null,
//                        activityName = "Visit Seongsan Ilchulbong Peak",
//                        activityCategory = "Nature",
//                        itineraryId = "unique-id-123",
//                        order = 4
//                    ),
//                    ScheduleItem(
//                        id = "5",
//                        tripDay = 3,
//                        date = "2022-10-12",
//                        lat = 33.489011,
//                        long = 126.498302,
//                        placeName = "Seongsan Ilchulbong",
//                        address = "284-12, Ilchul-ro, Seongsan-eup, Seogwipo-si, Jeju-do",
//                        details = "Hiking and enjoy the sunrise at the UNESCO World Heritage site.",
//                        time = "10:00",
//                        completed = false,
//                        photoUrl = null,
//                        activityName = "Visit Seongsan Ilchulbong Peak",
//                        activityCategory = "Nature",
//                        itineraryId = "unique-id-123",
//                        order = 5
//                    ),
//                    ScheduleItem(
//                        id = "6",
//                        tripDay = 4,
//                        date = "2022-10-13",
//                        lat = 33.489011,
//                        long = 126.498302,
//                        placeName = "Seongsan Ilchulbong",
//                        address = "284-12, Ilchul-ro, Seongsan-eup, Seogwipo-si, Jeju-do",
//                        details = "Hiking and enjoy the sunrise at the UNESCO World Heritage site.",
//                        time = "10:00",
//                        completed = false,
//                        photoUrl = null,
//                        activityName = "Visit Seongsan Ilchulbong Peak",
//                        activityCategory = "Nature",
//                        itineraryId = "unique-id-123",
//                        order = 6
//                    )
//                )
//            )
//        }
//    }
//}