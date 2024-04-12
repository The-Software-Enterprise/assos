package com.swent.assos.model.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.swent.assos.model.data.Association
import com.swent.assos.ui.login.SignUpScreen
import com.swent.assos.ui.manageAssos.CreateEvent
import com.swent.assos.ui.manageAssos.CreateNews
import com.swent.assos.ui.screens.AssoDigest

@Composable
fun NavigationGraph() {
  val navController = rememberNavController()
  val navigationActions = NavigationActions(navController = navController)

  NavHost(navController = navController, startDestination = Destinations.HOME.route) {
    composable(Destinations.LOGIN.route) {
      // LoginScreen(navController = navController)
    }
    composable(Destinations.SIGN_UP.route) { SignUpScreen(navigationActions = navigationActions) }
    composable(Destinations.HOME.route) { HomeNavigation(navigationActions = navigationActions) }
    navigation(startDestination = Destinations.HOME.route, route = "DisplayAssociations") {
      composable(Destinations.HOME.route) { HomeNavigation(navigationActions = navigationActions) }
      composable(Destinations.ASSOCIATION_PAGE.route + "/{id}/{acronym}/{fullname}/{url}") {
          backStackEntry ->
        val association =
            Association(
                id = backStackEntry.arguments?.getString("id") ?: "",
                acronym = backStackEntry.arguments?.getString("acronym") ?: "",
                fullname = backStackEntry.arguments?.getString("fullname") ?: "",
                url = backStackEntry.arguments?.getString("url") ?: "")
        AssoDigest(asso = association, navigationActions = navigationActions)
      }
      composable(Destinations.CREATE_NEWS.route + "/{assoId}") { backStackEntry ->
        CreateNews(
            navigationActions = navigationActions,
            associationId = backStackEntry.arguments?.getString("assoId") ?: "")
      }
      composable(Destinations.CREATE_EVENT.route) {
        CreateEvent(navigationActions = navigationActions)
      }
    }
  }
}

enum class Destinations(val route: String) {
  LOGIN("Login"),
  HOME("Home"),
  ASSOCIATION_PAGE("AssociationPage"),
  SIGN_UP("SignUp"),
  CREATE_NEWS("CreateNews"),
  CREATE_EVENT("CreateEvent"),
}
