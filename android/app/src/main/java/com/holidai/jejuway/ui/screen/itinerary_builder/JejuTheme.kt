package com.holidai.jejuway.ui.screen.itinerary_builder

import com.holidai.jejuway.R

enum class JejuTheme(
    val value: String,
    val photoRes: Int,
) {
    NATURE(
        "Nature",
        R.drawable.jeju_nature
    ),
    CULTURE(
        "Culture",
        R.drawable.jeju_culture
    ),
    FOOD(
        "Food",
        R.drawable.jeju_food
    ),
    MARKET(
        "Market",
        R.drawable.jeju_market
    ),
    CITY(
        "City",
        R.drawable.jeju_city
    ),
    SEASIDE(
        "Seaside",
        R.drawable.jeju_seaside
    );

    companion object {
        fun String.toJejuTheme(): JejuTheme {
            return when (this) {
                "Nature" -> NATURE
                "Culture" -> CULTURE
                "Food" -> FOOD
                "Market" -> MARKET
                "City" -> CITY
                "Seaside" -> SEASIDE
                else -> throw IllegalArgumentException("Invalid theme: $this")
            }
        }
    }
}