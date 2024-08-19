package com.holidai.jejuway.ui.screen.home.components

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player.REPEAT_MODE_ALL
import com.google.android.exoplayer2.ui.PlayerView
import com.holidai.jejuway.R

@SuppressLint("OpaqueUnitKey")
@Composable
fun ExoVideoPlayer(modifier: Modifier = Modifier) {
    val context = LocalContext.current

    val mediaItem = MediaItem.Builder()
        .setUri("android.resource://" + LocalContext.current.packageName + "/" + R.raw.vid_jeju_opening)
        .build()
    val exoPlayer = remember(context, mediaItem) {
        ExoPlayer.Builder(context)
            .build()
            .also { exoPlayer ->
                exoPlayer.setMediaItem(mediaItem)
                exoPlayer.prepare()
                exoPlayer.playWhenReady = true
                exoPlayer.repeatMode = REPEAT_MODE_ALL
            }
    }

    DisposableEffect(
        AndroidView(
            modifier = modifier.fillMaxWidth(),
            factory = {
                PlayerView(context).apply {
                    useController = false
                    player = exoPlayer
                }
            })
    ) {
        onDispose { exoPlayer.release() }
    }
}