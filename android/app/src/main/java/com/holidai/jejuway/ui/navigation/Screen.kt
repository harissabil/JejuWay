package com.holidai.jejuway.ui.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class Screen {
    companion object {
        fun fromRoute(route: String): Screen? {
            return Screen::class.sealedSubclasses.firstOrNull {
                route.contains(it.qualifiedName.toString())
            }?.objectInstance
        }
    }

    @Serializable
    data object Auth : Screen()

    @Serializable
    data object MainNavigation : Screen()

    @Serializable
    data object Home : Screen()

    @Serializable
    data class ItineraryFirst(
        val content: String,
    ) : Screen()

    @Serializable
    data object ItineraryBuilder : Screen()

    @Serializable
    data object HomeNavigation : Screen()

    @Serializable
    data object HomeNavigator : Screen()

    @Serializable
    data object HomeMain : Screen()

    @Serializable
    data object Trips : Screen()

    @Serializable
    data class Itinerary(
        val itineraryId: String,
    ) : Screen()

    @Serializable
    data object Album : Screen()

    @Serializable
    data object Profile : Screen()
}