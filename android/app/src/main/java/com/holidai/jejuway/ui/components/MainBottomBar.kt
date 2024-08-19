package com.holidai.jejuway.ui.components

import android.content.res.Configuration
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.holidai.jejuway.ui.theme.JejuAItineraryTheme

@Composable
fun MainBottomBar(
    modifier: Modifier = Modifier,
    items: List<BottomBarItem>,
    selectedIndex: Int,
    onItemSelected: (Int) -> Unit,
) {
    NavigationBar(modifier = modifier.fillMaxWidth()) {
        items.forEachIndexed { index, bottomBarItem ->
            if (index == 2) {
                Box(modifier = Modifier.size(32.dp))
                return@forEachIndexed
            }
            NavigationBarItem(
                selected = index == selectedIndex,
                onClick = { onItemSelected(index) },
                icon = {
                    (if (index == selectedIndex) bottomBarItem.selectedIcon else bottomBarItem.unselectedIcon)?.let {
                        Icon(
                            imageVector = it,
                            contentDescription = bottomBarItem.title,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                }
            )
        }
    }
}

data class BottomBarItem(
    val title: String?,
    val unselectedIcon: ImageVector?,
    val selectedIcon: ImageVector?,
)

@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun MainBottomBarPreview() {
    JejuAItineraryTheme {
        Surface {
            MainBottomBar(
                items = listOf(
                    BottomBarItem(
                        title = "Home",
                        unselectedIcon = Icons.Outlined.Home,
                        selectedIcon = Icons.Filled.Home,
                    ),
                    BottomBarItem(
                        null, null, null
                    ),
                    BottomBarItem(
                        title = "Settings",
                        unselectedIcon = Icons.Outlined.Settings,
                        selectedIcon = Icons.Filled.Settings,
                    ),
                ),
                selectedIndex = 0,
                onItemSelected = { }
            )
        }
    }
}