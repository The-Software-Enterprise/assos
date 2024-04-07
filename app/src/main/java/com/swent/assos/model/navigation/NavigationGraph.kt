package com.swent.assos.model.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController

@Composable
fun NavigationGraph() {
  val navController = rememberNavController()
  NavHost(navController = navController, startDestination = Destinations.HOME.route) {
    composable(Destinations.LOGIN.route) {
      // LoginScreen(navController = navController)
    }
    composable(Destinations.HOME.route) { HomeNavigation() }
    navigation(startDestination = Destinations.HOME.route, route = "DisplayAssociations") {
      composable(Destinations.HOME.route) { HomeNavigation() }
      composable(Destinations.ASSOCIATION_PAGE.route) {
        // AssociationPageScreen(navController = navController)
      }
    }
  }
}

enum class Destinations(val route: String) {
  LOGIN("Login"),
  HOME("Home"),
  ASSOCIATION_PAGE("AssociationPage"),
}
