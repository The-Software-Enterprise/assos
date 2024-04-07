package com.swent.assos.model.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination

class NavigationActions(private val navController: NavController) {

  fun navigateTo(destination: Destinations) {
    navController.navigate(destination.route) {
      popUpTo(navController.graph.findStartDestination().id) { saveState = true}

      launchSingleTop = true

      restoreState = true
    }
  }

  fun navigateTo(destination: String) {
    navController.navigate(destination)
  }

  fun goBack() {
    navController.popBackStack()
  }
}