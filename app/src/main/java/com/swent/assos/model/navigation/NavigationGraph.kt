package com.swent.assos.model.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHost
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.swent.assos.model.view.AppViewModel
import com.swent.assos.ui.login.LoginScreen
import com.swent.assos.ui.login.SignUpScreen
import com.swent.assos.ui.screens.Settings
import com.swent.assos.ui.screens.assoDetails.AssoDetails
import com.swent.assos.ui.screens.assoDetails.EventDetails
import com.swent.assos.ui.screens.assoDetails.NewsDetails
import com.swent.assos.ui.screens.manageAsso.CreateEvent
import com.swent.assos.ui.screens.manageAsso.CreateNews
import com.swent.assos.ui.screens.manageAsso.ManageAssociation

@Composable
fun NavigationGraph() {
  val navController = rememberNavController()
  val navigationActions = NavigationActions(navController = navController)
  val appViewModel: AppViewModel = hiltViewModel()
  val user by appViewModel.getAuthUser().collectAsState()

  val startDestinations =
      if (user.id == "") {
        Destinations.LOGIN.route
      } else {
        Destinations.HOME.route
      }

  NavHost(navController = navController, startDestination = startDestinations) {
    composable(Destinations.LOGIN.route) { LoginScreen(navigationActions = navigationActions) }
    composable(Destinations.SIGN_UP.route) { SignUpScreen(navigationActions = navigationActions) }
    composable(Destinations.HOME.route) { HomeNavigation(navigationActions = navigationActions) }
    composable(Destinations.ASSO_DETAILS.route + "/{assoId}") { backStackEntry ->
      AssoDetails(
          assoId = backStackEntry.arguments?.getString("assoId").toString(),
          navigationActions = navigationActions)
    }
    composable(Destinations.EVENT_DETAILS.route + "/{eventId}") { backStackEntry ->
      EventDetails(
          eventId = backStackEntry.arguments?.getString("eventId").toString(),
          navigationActions = navigationActions)
    }
    composable(Destinations.NEWS_DETAILS.route + "/{newsId}") { backStackEntry ->
      NewsDetails(
          newsId = backStackEntry.arguments?.getString("newsId").toString(),
          navigationActions = navigationActions)
    }
    composable(Destinations.CREATE_NEWS.route + "/{assoId}") { backStackEntry ->
      CreateNews(
          navigationActions = navigationActions,
          assoId = backStackEntry.arguments?.getString("assoId") ?: "")
    }
    composable(Destinations.CREATE_EVENT.route + "/{assoId}") { backStackEntry ->
      CreateEvent(
          navigationActions = navigationActions,
          assoId = backStackEntry.arguments?.getString("assoId") ?: "")
    }
    composable(Destinations.ASSO_MODIFY_PAGE.route + "/{assoId}") { backStackEntry ->
      ManageAssociation(
          assoId = backStackEntry.arguments?.getString("assoId").toString(),
          navigationActions = navigationActions)
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
  ASSO_MODIFY_PAGE("AssoModifyPage"),
  EVENT_DETAILS("EventDetails"),
  NEWS_DETAILS("NewsDetails"),
}
