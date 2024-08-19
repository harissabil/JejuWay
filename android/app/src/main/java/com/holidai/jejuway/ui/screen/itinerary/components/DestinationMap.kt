package com.holidai.jejuway.ui.screen.itinerary.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.holidai.jejuway.data.network.upstage_ai.dto.ScheduleItem

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun DestinationMap(
    destinationCoordinates: LatLng,
    items: List<ScheduleItem>,
    modifier: Modifier = Modifier,
) {
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(destinationCoordinates, 15f)
    }
    val boundsBuilder = LatLngBounds.builder()
    items.forEach { item ->
        if (item.lat != null && item.long != null)
            boundsBuilder.include(LatLng(item.lat, item.long))
    }
    val bounds = boundsBuilder.build()

    LaunchedEffect(bounds) {
        cameraPositionState.animate(CameraUpdateFactory.newLatLngBounds(bounds, 200), 1_000)
    }

    var isMyLocationEnabled by rememberSaveable { mutableStateOf(false) }

    val locationPermissions = rememberMultiplePermissionsState(
        permissions = listOf(
            android.Manifest.permission.ACCESS_FINE_LOCATION,
            android.Manifest.permission.ACCESS_COARSE_LOCATION
        )
    )

    if (locationPermissions.allPermissionsGranted) {
        isMyLocationEnabled = true
    } else {
        LaunchedEffect(Unit) {
            locationPermissions.launchMultiplePermissionRequest()
        }
    }

    GoogleMap(
        modifier = modifier,
        cameraPositionState = cameraPositionState,
        properties = MapProperties(isMyLocationEnabled = isMyLocationEnabled),
        uiSettings = MapUiSettings(
            myLocationButtonEnabled = false,
        )
    ) {
        items.forEach { item ->
            if (item.lat != null && item.long != null)
                Marker(
                    state = MarkerState(position = LatLng(item.lat, item.long)),
                    title = item.placeName,
                )
        }
    }
}