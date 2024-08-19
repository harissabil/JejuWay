package com.holidai.jejuway.ui.screen.itinerary.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxState
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> SwipeToFinishContainer(
    modifier: Modifier = Modifier,
    item: T,
    onDismissedToEnd: (T) -> Unit,
    onDismissedToStart: (T) -> Unit,
    animationDuration: Int = 500,
    content: @Composable (T) -> Unit,
) {
    var isFinished by rememberSaveable { mutableStateOf(false) }
    var isCamera by rememberSaveable { mutableStateOf(false) }
    val dismissState = rememberSwipeToDismissBoxState(
        confirmValueChange = { value ->
            when (value) {
                SwipeToDismissBoxValue.EndToStart -> {
//                    isFinished = true
                    onDismissedToStart(item)
                    false
                }

                SwipeToDismissBoxValue.StartToEnd -> {
//                    isCamera = true
                    onDismissedToEnd(item)
                    false
                }

                else -> {
                    false
                }
            }
        }
    )

//    LaunchedEffect(key1 = isFinished || isCamera) {
//        if (isFinished) {
//            delay(animationDuration.toLong())
//            onDismissedToStart(item)
//            dismissState.reset()
//            isFinished = false
//        } else if (isCamera) {
//            delay(animationDuration.toLong())
//            onDismissedToEnd(item)
//            dismissState.reset()
//            isCamera = false
//        }
//    }

    SwipeToDismissBox(
        modifier = modifier,
        state = dismissState,
        backgroundContent = {
            FinishBackground(
                swipeToDismissBoxState = dismissState
            )
        },
        content = { content(item) },
    )

//    AnimatedVisibility(
//        modifier = modifier,
//        visible = !isFinished || !isCamera,
//        exit = shrinkVertically(
//            animationSpec = tween(durationMillis = animationDuration),
//            shrinkTowards = Alignment.Top
//        ) + fadeOut()
//    ) {
//
//    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FinishBackground(
    modifier: Modifier = Modifier,
    swipeToDismissBoxState: SwipeToDismissBoxState,
) {
    val color = when (swipeToDismissBoxState.dismissDirection) {
        SwipeToDismissBoxValue.EndToStart -> {
            MaterialTheme.colorScheme.secondary
        }

        SwipeToDismissBoxValue.StartToEnd -> {
            MaterialTheme.colorScheme.tertiary
        }

        else -> {
            Color.Transparent
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color)
            .padding(16.dp)
            .then(modifier),
        contentAlignment = if (swipeToDismissBoxState.dismissDirection == SwipeToDismissBoxValue.EndToStart) {
            Alignment.CenterEnd
        } else {
            Alignment.CenterStart
        }
    ) {
        Icon(
            imageVector = if (swipeToDismissBoxState.dismissDirection == SwipeToDismissBoxValue.EndToStart) {
                Icons.Default.Check
            } else {
                Icons.Default.CameraAlt
            },
            contentDescription = if (swipeToDismissBoxState.dismissDirection == SwipeToDismissBoxValue.EndToStart) {
                "Finish"
            } else {
                "Take a photo"
            },
            tint = if (swipeToDismissBoxState.dismissDirection == SwipeToDismissBoxValue.EndToStart) {
                MaterialTheme.colorScheme.onSecondary
            } else {
                MaterialTheme.colorScheme.onTertiary
            }
        )
    }
}