package com.holidai.jejuway.ui.screen.itinerary_builder.components

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.holidai.jejuway.ui.screen.itinerary_builder.JejuTheme
import com.holidai.jejuway.ui.theme.JejuAItineraryTheme
import com.holidai.jejuway.ui.theme.onSurfaceDark
import com.holidai.jejuway.ui.theme.spacing

@Composable
fun ThemeItem(
    modifier: Modifier = Modifier,
    jejuTheme: JejuTheme,
    onClick: (jejuTheme: JejuTheme) -> Unit,
) {
    var isSelected by rememberSaveable {
        mutableStateOf(false)
    }

    val gradient = Brush.verticalGradient(
        colors = listOf(
            Color.Black.copy(alpha = 0.7F),
            Color.Black
        )
    )

    Box(
        modifier = Modifier
            .size(width = 140.dp, height = 140.dp)
            .clip(MaterialTheme.shapes.large)
            .clickable {
                isSelected = !isSelected
                onClick(jejuTheme)
            }
            .then(modifier),
    ) {
        Image(
            painter = painterResource(id = jejuTheme.photoRes),
            contentDescription = jejuTheme.value,
            contentScale = ContentScale.Crop,
            alignment = Alignment.Center,
            modifier = Modifier.matchParentSize(),
        )

        Box(
            Modifier
                .fillMaxWidth()
                .align(Alignment.BottomStart)
        ) {
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .background(gradient)
            )
            Text(
                text = jejuTheme.value,
                style = MaterialTheme.typography.titleSmall.copy(
                    fontWeight = FontWeight.SemiBold
                ),
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                color = onSurfaceDark,
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(
                        vertical = MaterialTheme.spacing.small,
                        horizontal = MaterialTheme.spacing.medium
                    )
            )
        }

        if (!isSelected) {
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .background(Color.Black.copy(alpha = 0.5F))
            )
        }
    }
}

@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun ThemeItemPreview() {
    JejuAItineraryTheme {
        Surface {
            ThemeItem(
                jejuTheme = JejuTheme.FOOD,
                onClick = {}
            )
        }
    }
}