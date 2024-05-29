package com.swent.assos.model.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.swent.assos.model.view.AppViewModel
import com.swent.assos.ui.login.LoginScreen
import com.swent.assos.ui.login.SignUpScreen
import com.swent.assos.ui.screens.assoDetails.AssoDetails
import com.swent.assos.ui.screens.assoDetails.EventDetails
import com.swent.assos.ui.screens.assoDetails.NewsDetails
import com.swent.assos.ui.screens.manageAsso.ApplicationManagement
import com.swent.assos.ui.screens.manageAsso.CreateNews
import com.swent.assos.ui.screens.manageAsso.CreatePosition
import com.swent.assos.ui.screens.manageAsso.ManageAssociation
import com.swent.assos.ui.screens.manageAsso.StaffManagement
import com.swent.assos.ui.screens.manageAsso.createEvent.CreateEvent
import com.swent.assos.ui.screens.manageAsso.createEvent.PosDetails
import com.swent.assos.ui.screens.profile.Appearance
import com.swent.assos.ui.screens.profile.Applications
import com.swent.assos.ui.screens.profile.Following
import com.swent.assos.ui.screens.profile.MyAssociations
import com.swent.assos.ui.screens.profile.NotificationSettings
import com.swent.assos.ui.screens.profile.Settings
import com.swent.assos.ui.screens.ticket.CreateTicket
import com.swent.assos.ui.screens.ticket.ScanTicket
import com.swent.assos.ui.screens.ticket.TicketDetails

@Composable
fun NavigationGraph(navController: NavHostController = rememberNavController()) {
  val navigationActions = NavigationActions(navController = navController)
  val appViewModel: AppViewModel = hiltViewModel()
  val user by appViewModel.getAuthUser().collectAsState()

  val startDestinations =
      if (user.id == "") {
        Destinations.LOGIN.route
      } else {
        Destinations.HOME.route
      }

  NavHost(
      navController = navController,
      startDestination = startDestinations,
      modifier = Modifier.testTag("NavHost")) {
        composable(Destinations.LOGIN.route) { LoginScreen(navigationActions = navigationActions) }
        composable(Destinations.SIGN_UP.route) {
          SignUpScreen(navigationActions = navigationActions)
        }
        composable(Destinations.HOME.route) {
          HomeNavigation(navigationActions = navigationActions)
        }
        composable(Destinations.TICKET_DETAILS.route + "/{eventId}") { backStackEntry ->
          TicketDetails(
              eventId = backStackEntry.arguments?.getString("eventId").toString(),
              navigationActions = navigationActions)
        }
        composable(Destinations.SCAN_TICKET.route) {
          ScanTicket(navigationActions = navigationActions)
        }
        composable(Destinations.ASSO_DETAILS.route + "/{assoId}") { backStackEntry ->
          AssoDetails(
              assoId = backStackEntry.arguments?.getString("assoId").toString(),
              navigationActions = navigationActions)
        }
        composable(Destinations.EVENT_DETAILS.route + "/{eventId}" + "/{assoId}") { backStackEntry
          ->
          EventDetails(
              eventId = backStackEntry.arguments?.getString("eventId").toString(),
              assoId = backStackEntry.arguments?.getString("assoId").toString(),
              navigationActions = navigationActions)
        }
        composable(Destinations.NEWS_DETAILS.route + "/{newsId}" + "/{assoId}") { backStackEntry ->
          NewsDetails(
              newsId = backStackEntry.arguments?.getString("newsId").toString(),
              assoId = backStackEntry.arguments?.getString("assoId").toString(),
              navigationActions = navigationActions)
        }
        composable(Destinations.POS_DETAILS.route + "/{assoId}" + "/{posId}") { backStackEntry ->
          PosDetails(
              posId = backStackEntry.arguments?.getString("posId").toString(),
              assoId = backStackEntry.arguments?.getString("assoId").toString(),
              navigationActions = navigationActions)
        }
        composable(Destinations.STAFF_MANAGEMENT.route + "/{eventId}") { backStackEntry ->
          StaffManagement(
              eventId = backStackEntry.arguments?.getString("eventId").toString(),
              navigationActions = navigationActions)
        }
        composable(Destinations.APPLICATION_MANAGEMENT.route + "/{assoId}") { backStackEntry ->
          ApplicationManagement(
              assoId = backStackEntry.arguments?.getString("assoId").toString(),
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
              assoId = backStackEntry.arguments?.getString("assoId") ?: "",
              navigationActions = navigationActions)
        }
        composable(Destinations.CREATE_POSITION.route + "/{assoId}") { backStackEntry ->
          CreatePosition(
              assoId = backStackEntry.arguments?.getString("assoId") ?: "",
              navigationActions = navigationActions)
        }
        composable(Destinations.SETTINGS.route) { Settings(navigationActions = navigationActions) }
        composable(Destinations.APPEARANCE.route) {
          Appearance(navigationActions = navigationActions)
        }

        composable(Destinations.FOLLOWING.route) {
          Following(navigationActions = navigationActions)
        }

        composable(Destinations.NOTIFICATION_SETTINGS.route) {
          NotificationSettings(navigationActions = navigationActions)
        }
        composable(Destinations.APPEARANCE.route) {
          Appearance(navigationActions = navigationActions)
        }
        composable(Destinations.MY_ASSOCIATIONS.route) {
          MyAssociations(navigationActions = navigationActions)
        }
        composable(Destinations.APPLICATIONS.route) {
          Applications(navigationActions = navigationActions)
        }
        composable(Destinations.CREATE_TICKET.route + "/{eventId}") { backStackEntry ->
          CreateTicket(
              navigationActions = navigationActions,
              eventId = backStackEntry.arguments?.getString("eventId") ?: "")
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
  STAFF_MANAGEMENT("StaffManagement"),
  APPLICATION_MANAGEMENT("ApplicationManagement"),
  TICKET_DETAILS("TicketDetails"),
  SCAN_TICKET("ScanTicket"),
  CREATE_TICKET("CreateTicket"),
  APPLICATIONS("Applications"),
  CREATE_POSITION("CreatePosition"),
  POS_DETAILS("PosDetails")
}
