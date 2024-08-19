package com.holidai.jejuway.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CardTravel
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PhotoAlbum
import androidx.compose.material.icons.outlined.CardTravel
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.PhotoAlbum
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.holidai.jejuway.R
import com.holidai.jejuway.ui.components.BottomBarItem
import com.holidai.jejuway.ui.components.MainBottomBar
import com.holidai.jejuway.ui.screen.home_navigator.home_main.HomeMainScreen
import com.holidai.jejuway.ui.screen.home_navigator.trips.TripsScreen
import com.holidai.jejuway.ui.screen.itinerary.ItineraryFirstScreen
import com.holidai.jejuway.ui.screen.itinerary.ItineraryScreen
import com.holidai.jejuway.ui.screen.itinerary_builder.ItineraryBuilderScreen

@Composable
fun HomeNavigator(modifier: Modifier = Modifier) {

    val bottomBarItems = listOf(
        BottomBarItem(
            title = "Home",
            unselectedIcon = Icons.Outlined.Home,
            selectedIcon = Icons.Filled.Home,
        ),
        BottomBarItem(
            title = "Trips",
            unselectedIcon = Icons.Outlined.CardTravel,
            selectedIcon = Icons.Filled.CardTravel,
        ),
        BottomBarItem(null, null, null),
        BottomBarItem(
            title = "Album",
            unselectedIcon = Icons.Outlined.PhotoAlbum,
            selectedIcon = Icons.Filled.PhotoAlbum,
        ),
        BottomBarItem(
            title = "Profile",
            unselectedIcon = Icons.Outlined.Person,
            selectedIcon = Icons.Filled.Person,
        )
    )

    val navController = rememberNavController()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = remember(navBackStackEntry) {
        Screen.fromRoute(navBackStackEntry?.destination?.route ?: "")
    }
    var selectedItem by rememberSaveable { mutableIntStateOf(0) }
    selectedItem = when (currentRoute) {
        Screen.HomeMain -> 0
        Screen.Trips -> 1
        Screen.Album -> 3
        Screen.Profile -> 4
        else -> 0
    }

    var isBottomBarVisible by rememberSaveable { mutableStateOf(true) }
    isBottomBarVisible = when (currentRoute) {
        is Screen.Itinerary -> false
        null -> false
        else -> true
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        bottomBar = {
            if (isBottomBarVisible) {
                MainBottomBar(
                    items = bottomBarItems,
                    selectedIndex = selectedItem,
                    onItemSelected = {
                        when (it) {
                            0 -> navigateToTab(navController, Screen.HomeMain)

                            1 -> navigateToTab(navController, Screen.Trips)

                            3 -> navController.navigate(Screen.Album)
                            4 -> navController.navigate(Screen.Profile)
                        }
                    }
                )
            }
        },
        floatingActionButtonPosition = FabPosition.Center,
        floatingActionButton = {
            if (isBottomBarVisible) {
                Box {
                    FloatingActionButton(
                        onClick = {
                            navController.navigate(Screen.ItineraryBuilder)
                        },
                        shape = CircleShape,
                        modifier = Modifier
                            .align(Alignment.Center)
                            .size(60.dp)
                            .offset(y = 52.dp),
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_solar_ai),
                            contentDescription = "Build Itinerary",
                            modifier = Modifier.padding(8.dp)
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            modifier = Modifier.padding(bottom = innerPadding.calculateBottomPadding()),
            navController = navController,
            startDestination = Screen.HomeMain
        ) {
            composable<Screen.HomeMain> {
                HomeMainScreen()
            }

            composable<Screen.Trips> {
                TripsScreen(
                    onItemClick = {
                        navController.navigate(Screen.Itinerary(it))
                    }
                )
            }

            composable<Screen.Itinerary> {
                val args = it.toRoute<Screen.Itinerary>()
                ItineraryScreen(itineraryId = args.itineraryId,
                    onNavigateUp = {
                        navController.navigateUp()
                    }
                )
            }

            composable<Screen.ItineraryBuilder> {
                ItineraryBuilderScreen(
                    onNavigateToItineraryFirst = { content ->
                        navController.navigate(Screen.ItineraryFirst(content))
                    }
                )
            }
            composable<Screen.ItineraryFirst> {
                val args = it.toRoute<Screen.ItineraryFirst>()
                ItineraryFirstScreen(
                    content = args.content,
                    onNavigateUp = {
                        navController.navigate(Screen.HomeNavigation) {
                            popUpTo(Screen.MainNavigation) { inclusive = true }
                        }
                    }
                )
            }
        }
    }
}

private fun navigateToTab(navController: NavController, screen: Screen) {
    navController.navigate(screen) {
        popUpTo(navController.graph.findStartDestination().id) {
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }
}