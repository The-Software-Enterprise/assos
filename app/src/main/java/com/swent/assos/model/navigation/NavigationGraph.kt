package com.swent.assos.model.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun NavigationGraph() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "home") {
        composable(Destinations.home) {
            //HomeScreen(navController = navController)
        }
        composable(Destinations.explore) {
            //ProfileScreen(navController = navController)
        }
        composable(Destinations.calendar) {
            //CalendarScreen(navController = navController)
        }
        composable(Destinations.qrCode) {
            //QRCodeScreen(navController = navController)
        }
        composable(Destinations.profile) {
            //ProfileScreen(navController = navController)
        }
    }
}

data class Destinations(
    val home: String = "home",
    val explore: String = "explore",
    val calendar: String = "calendar",
    val qrCode: String = "qrCode",
    val profile: String = "profile"
)