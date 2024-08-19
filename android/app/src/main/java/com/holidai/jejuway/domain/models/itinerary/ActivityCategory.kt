package com.holidai.jejuway.domain.models.itinerary

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiTransportation
import androidx.compose.material.icons.filled.Fastfood
import androidx.compose.material.icons.filled.Hotel
import androidx.compose.material.icons.filled.LocationCity
import androidx.compose.material.icons.filled.Nature
import androidx.compose.material.icons.filled.NaturePeople
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Waves
import androidx.compose.ui.graphics.vector.ImageVector

enum class ActivityCategory(
    val value: String,
    val icon: ImageVector
) {
    Hotel(
        value = "Hotel",
        icon = Icons.Filled.Hotel
    ),
    Transportation(
        value = "Transportation",
        icon = Icons.Filled.EmojiTransportation
    ),
    Food(
        value = "Food",
        icon = Icons.Filled.Fastfood
    ),
    Nature(
        value = "Nature",
        icon = Icons.Filled.Nature
    ),
    Culture(
        value = "Culture",
        icon = Icons.Filled.NaturePeople
    ),
    Shopping(
        value = "Shopping",
        icon = Icons.Filled.ShoppingCart
    ),
    Entertainment(
        value = "Entertainment",
        icon = Icons.Filled.LocationCity
    ),
    City(
        value = "City",
        icon = Icons.Filled.LocationCity
    ),
    Seaside(
        value = "Seaside",
        icon = Icons.Filled.Waves
    );

    companion object {
        fun String.fromValue(): ActivityCategory {
            return when (this) {
                "Hotel" -> Hotel
                "Transportation" -> Transportation
                "Food" -> Food
                "Nature" -> Nature
                "Culture" -> Culture
                "Shopping" -> Shopping
                "Entertainment" -> Entertainment
                "City" -> City
                "Seaside" -> Seaside
                else -> City
            }
        }
    }
}