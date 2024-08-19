package com.holidai.jejuway.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.holidai.jejuway.ui.screen.auth.AuthScreen
import com.holidai.jejuway.ui.screen.home.HomeFirstTimeScreen
import com.holidai.jejuway.ui.screen.itinerary.ItineraryFirstScreen
import com.holidai.jejuway.ui.screen.itinerary_builder.ItineraryBuilderScreen

@Composable
fun NavGraph(
    modifier: Modifier = Modifier,
    startDestination: Screen,
) {
    val navController = rememberNavController()

    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination
    ) {
        composable<Screen.Auth> {
            AuthScreen(
                onNavigateToMainScreen = {
                    navController.navigate(Screen.MainNavigation)
                }
            )
        }

        navigation<Screen.MainNavigation>(startDestination = Screen.Home) {
            composable<Screen.Home> {
//                HomeScreen()
                HomeFirstTimeScreen(
                    onNavigateToItineraryBuilder = { navController.navigate(Screen.ItineraryBuilder) }
                )
            }
            composable<Screen.Trips> { }
            composable<Screen.Profile> { }
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

        navigation<Screen.HomeNavigation>(startDestination = Screen.HomeNavigator) {
            composable<Screen.HomeNavigator> {
                HomeNavigator()
            }
        }
    }
}