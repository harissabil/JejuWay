package com.holidai.jejuway.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.ripple.LocalRippleTheme
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.holidai.jejuway.ui.theme.NoRippleTheme

@Composable
fun FullscreenLoading(modifier: Modifier = Modifier) {
    CompositionLocalProvider(LocalRippleTheme provides NoRippleTheme) {
        Box(modifier = modifier
            .fillMaxSize()
            .background(Color.Gray.copy(alpha = 0.3f))
            .clickable(enabled = true) { }
            .then(modifier),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    }
}