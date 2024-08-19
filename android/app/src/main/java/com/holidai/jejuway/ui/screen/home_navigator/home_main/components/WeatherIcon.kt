package com.holidai.jejuway.ui.screen.home_navigator.home_main.components

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.holidai.jejuway.domain.models.weather.WeatherCode
import com.holidai.jejuway.domain.models.weather.getWeatherIcon
import com.holidai.jejuway.ui.theme.JejuAItineraryTheme
import com.holidai.jejuway.ui.theme.spacing

@Composable
fun WeatherIcon(
    weatherCode: WeatherCode?,
    isDay: Boolean?,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (weatherCode != null) {
            Icon(
                painter = painterResource(
                    id = getWeatherIcon(
                        weatherCode = weatherCode,
                        isDay = isDay ?: true
                    )
                ), contentDescription = weatherCode.description,
                modifier = Modifier.size(42.dp)
            )
        } else {
            Text(
                text = "N/A",
                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.SemiBold),
            )
        }
        Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))
        Text(
            text = weatherCode?.description ?: "N/A",
            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Normal),
            overflow = TextOverflow.Ellipsis,
            maxLines = 2,
            textAlign = TextAlign.Center
        )
    }
}

@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun WeatherIconPrev() {
    JejuAItineraryTheme {
        Surface {
            WeatherIcon(WeatherCode.CLEAR_SKY, true)
        }
    }
}