package com.swent.assos.model.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.swent.assos.ui.login.LoginScreen

@Composable
fun NavigationGraph() {
  val navController = rememberNavController()
  NavHost(navController = navController, startDestination = Destinations.LOGIN.route) {
    composable(Destinations.LOGIN.route) {
      LoginScreen()
    }
    composable(Destinations.HOME.route) { HomeNavigation() }
    navigation(startDestination = Destinations.LOGIN.route, route = "DisplayAssociations") {
      composable(Destinations.LOGIN.route) {
        LoginScreen()
      }
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
  SIGN_UP("SignUp"),
}
