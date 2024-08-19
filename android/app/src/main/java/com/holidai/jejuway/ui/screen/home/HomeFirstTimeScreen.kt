package com.holidai.jejuway.ui.screen.home

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.holidai.jejuway.R
import com.holidai.jejuway.ui.screen.home.components.ExoVideoPlayer
import com.holidai.jejuway.ui.theme.spacing

@Composable
fun HomeFirstTimeScreen(
    modifier: Modifier = Modifier,
    onNavigateToItineraryBuilder: () -> Unit,
) {
    Scaffold { innerPadding ->
        HomeFirstTimeContent(
            modifier = modifier.padding(
                start = innerPadding.calculateStartPadding(LayoutDirection.Ltr),
                end = innerPadding.calculateEndPadding(LayoutDirection.Ltr),
                bottom = innerPadding.calculateBottomPadding(),
            ),
            onNavigateToItineraryBuilder = onNavigateToItineraryBuilder
        )
    }
}

@Composable
fun HomeFirstTimeContent(
    modifier: Modifier = Modifier,
    onNavigateToItineraryBuilder: () -> Unit,
) {
    Box(modifier = modifier.fillMaxSize()) {
        ExoVideoPlayer(modifier = Modifier.align(Alignment.TopCenter))

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Gray.copy(alpha = 0.5f))
        )

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(fraction = 0.6f)
                .align(Alignment.BottomCenter),
            shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
            colors = CardColors(
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.onSurface,
                disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                disabledContentColor = MaterialTheme.colorScheme.onSurfaceVariant
            )
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(MaterialTheme.spacing.large))
                Text(
                    modifier = Modifier
                        .animateContentSize()
                        .padding(horizontal = MaterialTheme.spacing.large),
                    text = "Plan Your Best Trip To The Jeju Island With Us.",
                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.weight(1f))
                FloatingActionButton(
                    shape = CircleShape,
                    onClick = onNavigateToItineraryBuilder,
                    modifier = Modifier.wrapContentSize()
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_solar_ai),
                        contentDescription = "Get Started",
                        modifier = Modifier.size(102.dp).padding(8.dp)
                    )
                }
                Spacer(modifier = Modifier.weight(1f))

                Text(
                    modifier = Modifier
                        .animateContentSize()
                        .padding(horizontal = MaterialTheme.spacing.large),
                    text = "Let's Get Started!",
                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Center,
                    fontSize = 20.sp
                )
                Spacer(modifier = Modifier.height(MaterialTheme.spacing.large))
            }
        }
    }
}