package com.swent.assos.model.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.swent.assos.model.view.AppViewModel
import com.swent.assos.ui.login.LoginScreen
import com.swent.assos.ui.login.SignUpScreen
import com.swent.assos.ui.screens.assoDetails.AssoDetails
import com.swent.assos.ui.screens.assoDetails.EventDetails
import com.swent.assos.ui.screens.assoDetails.NewsDetails
import com.swent.assos.ui.screens.manageAsso.CreateEvent
import com.swent.assos.ui.screens.manageAsso.CreateNews
import com.swent.assos.ui.screens.manageAsso.ManageAssociation
import com.swent.assos.ui.screens.profile.Appearance
import com.swent.assos.ui.screens.profile.Following
import com.swent.assos.ui.screens.profile.MyAssociations
import com.swent.assos.ui.screens.profile.NotificationSettings
import com.swent.assos.ui.screens.profile.Settings

@Composable
fun NavigationGraph() {
  val navController = rememberNavController()
  val navigationActions = NavigationActions(navController = navController)
  val appViewModel: AppViewModel = hiltViewModel()

  NavHost(navController = navController, startDestination = Destinations.HOME.route) {
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
    composable(Destinations.APPEARANCE.route) { Appearance(navigationActions = navigationActions) }

    composable(Destinations.FOLLOWING.route) { Following(navigationActions = navigationActions) }

    composable(Destinations.NOTIFICATION_SETTINGS.route) {
      NotificationSettings(navigationActions = navigationActions)
    }
    composable(Destinations.APPEARANCE.route) { Appearance(navigationActions = navigationActions) }
    composable(Destinations.MY_ASSOCIATIONS.route) {
      MyAssociations(navigationActions = navigationActions)
    }
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
  NOTIFICATION_SETTINGS("NotificationSettings"),
  APPEARANCE("Appearance"),
  MY_ASSOCIATIONS("MyAssociations"),
  FOLLOWING("Following"),
}
