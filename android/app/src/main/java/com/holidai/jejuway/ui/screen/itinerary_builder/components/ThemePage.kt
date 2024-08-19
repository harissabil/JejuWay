package com.holidai.jejuway.ui.screen.itinerary_builder.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.holidai.jejuway.ui.screen.itinerary_builder.JejuTheme
import com.holidai.jejuway.ui.theme.spacing

@Composable
fun ThemePage(
    modifier: Modifier = Modifier,
    state: LazyGridState,
    onThemeSelected: (JejuTheme) -> Unit,
) {
    LazyVerticalGrid(
        modifier = Modifier
            .fillMaxWidth()
            .then(modifier),
        state = state,
        columns = GridCells.Adaptive(140.dp),
        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium),
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium),
    ) {
        items(items = JejuTheme.entries.toTypedArray(), key = { it }) { theme ->
            ThemeItem(
                jejuTheme = theme,
                onClick = onThemeSelected
            )
        }
    }
}