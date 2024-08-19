package com.holidai.jejuway.ui.screen.itinerary_builder.components

import android.content.res.Configuration
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.DateRangePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.holidai.jejuway.ui.theme.JejuAItineraryTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePage(
    modifier: Modifier = Modifier,
    state: DateRangePickerState,
) {
    DateRangePicker(
        modifier = modifier.fillMaxWidth(),
        state = state,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun DatePagePreview() {
    JejuAItineraryTheme {
        Surface {
            DatePage(state = rememberDateRangePickerState())
        }
    }
}