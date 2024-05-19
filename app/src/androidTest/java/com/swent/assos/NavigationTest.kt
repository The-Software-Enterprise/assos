package com.swent.assos

import androidx.activity.compose.setContent
import androidx.navigation.NavHostController
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.swent.assos.model.navigation.Destinations
import com.swent.assos.model.navigation.NavigationGraph
import dagger.hilt.android.testing.HiltAndroidTest
import io.mockk.impl.annotations.RelaxedMockK
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
class NavigationTest : SuperTest() {
  @RelaxedMockK lateinit var navHostController: NavHostController

  override fun setup() {
    super.setup()
    composeTestRule.activity.setContent { NavigationGraph(navHostController) }
  }

  @Test
  fun testNavigation() {
    navHostController.navigate(Destinations.LOGIN.route)
    navHostController.navigate(Destinations.SIGN_UP.route)
    navHostController.navigate(Destinations.HOME.route)
    navHostController.navigate(Destinations.TICKET_DETAILS.route + "/eventId")
    navHostController.navigate(Destinations.SCAN_TICKET.route)
    navHostController.navigate(Destinations.ASSO_DETAILS.route + "/assoId")
    navHostController.navigate(Destinations.EVENT_DETAILS.route + "/eventId" + "/assoId")
    navHostController.navigate(Destinations.NEWS_DETAILS.route + "/newsId")
    navHostController.navigate(Destinations.STAFF_MANAGEMENT.route + "/eventId")
    navHostController.navigate(Destinations.APPLICATION_MANAGEMENT.route + "/assoId")
    navHostController.navigate(Destinations.CREATE_NEWS.route + "/assoId")
    navHostController.navigate(Destinations.CREATE_EVENT.route + "/assoId")
    navHostController.navigate(Destinations.ASSO_MODIFY_PAGE.route + "/assoId")
    navHostController.navigate(Destinations.SETTINGS.route)
    navHostController.navigate(Destinations.APPEARANCE.route)
    navHostController.navigate(Destinations.FOLLOWING.route)
    navHostController.navigate(Destinations.NOTIFICATION_SETTINGS.route)
    navHostController.navigate(Destinations.APPEARANCE.route)
    navHostController.navigate(Destinations.MY_ASSOCIATIONS.route)
    navHostController.navigate(Destinations.CREATE_TICKET.route + "/eventId")
  }
}
