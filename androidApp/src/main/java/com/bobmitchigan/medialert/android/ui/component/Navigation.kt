package com.bobmitchigan.medialert.android.ui.component

import androidx.navigation.NavController
import com.bobmitchigan.medialert.domain.Destination

/**
 * Navigates to the given destination, ensuring that there is always single instance of this
 * destination at the top of the navigation stack.
 *
 * @param destination The destination to navigate to.
 */
fun NavController.navigateSingleTop(destination: Destination) {
    navigate(destination.destination()) {
        launchSingleTop = true
    }
}

/**
 * Navigates to the given destination.
 *
 * @param destination The destination to navigate to.
 */
fun NavController.navigate(destination: Destination) = navigate(
    destination.destination()
)
