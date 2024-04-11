package com.swent.assos.model.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.swent.assos.ui.login.LoginScreen
import com.swent.assos.ui.login.SignUpScreen
import com.swent.assos.ui.screens.Overview

@Composable
fun NavigationGraph() {
  val navController = rememberNavController()
  NavHost(navController = navController, startDestination = Destinations.LOGIN.route) {
    composable(Destinations.LOGIN.route) { LoginScreen(navController) }
    composable(Destinations.SIGN_UP.route) { SignUpScreen(navController = navController) }
    composable(Destinations.HOME.route) { HomeNavigation() }
    composable(Destinations.ASSOCIATION_PAGE.route) {
      Overview(overviewViewModel = hiltViewModel(), navController = navController)
    }
  }
}

enum class Destinations(val route: String) {
  LOGIN("Login"),
  HOME("Home"),
  ASSOCIATION_PAGE("AssociationPage"),
  SIGN_UP("SignUp"),
}
