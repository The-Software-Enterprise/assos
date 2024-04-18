package com.swent.assos.model.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.swent.assos.ui.login.LoginScreen
import com.swent.assos.ui.login.SignUpScreen
import com.swent.assos.ui.screens.Settings
import com.swent.assos.ui.screens.followAssos.AssoDetails
import com.swent.assos.ui.screens.manageAssos.CreateEvent
import com.swent.assos.ui.screens.manageAssos.CreateNews

@Composable
fun NavigationGraph() {
  val navController = rememberNavController()
  val navigationActions = NavigationActions(navController = navController)

  NavHost(navController = navController, startDestination = Destinations.HOME.route) {
    composable(Destinations.LOGIN.route) { LoginScreen(navigationActions = navigationActions) }
    composable(Destinations.SIGN_UP.route) { SignUpScreen(navigationActions = navigationActions) }
    composable(Destinations.HOME.route) { HomeNavigation(navigationActions = navigationActions) }
    composable(Destinations.ASSO_DETAILS.route + "/{assoId}") { backStackEntry ->
      AssoDetails(
          assoId = backStackEntry.arguments?.getString("assoId").toString(),
          navigationActions = navigationActions)
    }
    composable(Destinations.CREATE_NEWS.route + "/{assoId}") { backStackEntry ->
      CreateNews(
          navigationActions = navigationActions,
          associationId = backStackEntry.arguments?.getString("assoId") ?: "")
    }
    composable(Destinations.CREATE_EVENT.route) {
      CreateEvent(navigationActions = navigationActions)
    }
    composable(Destinations.SETTINGS.route) { Settings(navigationActions = navigationActions) }
  }
}

enum class Destinations(val route: String) {
  LOGIN("Login"),
  HOME("Home"),
  ASSO_DETAILS("AssoDetails"),
  SIGN_UP("SignUp"),
  CREATE_NEWS("CreateNews"),
  CREATE_EVENT("CreateEvent"),
  SETTINGS("Settings"),
  ASSO_PAGE("AssoPage"),
  ASSO_MODIFY_PAGE("AssoModifyPage")
}
