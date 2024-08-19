package com.holidai.jejuway.ui.screen.itinerary.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.holidai.jejuway.R
import com.holidai.jejuway.ui.screen.itinerary.MenuItem
import com.holidai.jejuway.ui.screen.itinerary.MenuItemsState

private val EXIT_FULLSCREEN_ICON
    get() = R.drawable.ic_fullscreen_exit

private val ENTER_FULLSCREEN_ICON
    get() = R.drawable.ic_fullscreen

@Composable
fun ConfigureMenuItems(
    menu: MenuItemsState,
    isFullscreen: Boolean,
    setFullscreen: (Boolean) -> Unit,
) {
    LaunchedEffect(isFullscreen) {
        val menuItems = mutableListOf(
            if (isFullscreen) {
                MenuItem("exit_fs", "Exit fullscreen", EXIT_FULLSCREEN_ICON) {
                    setFullscreen(false)
                }
            } else {
                MenuItem("enter_fs", "Enter fullscreen", ENTER_FULLSCREEN_ICON) {
                    setFullscreen(true)
                }
            }
        )
        menu.setMenuItems("detail_route", menuItems)
    }
}