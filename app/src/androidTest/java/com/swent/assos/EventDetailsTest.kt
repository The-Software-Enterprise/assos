package com.swent.assos

import android.net.Uri
import androidx.activity.compose.setContent
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.firebase.firestore.FirebaseFirestore
import com.swent.assos.model.data.Association
import com.swent.assos.model.data.DataCache
import com.swent.assos.model.data.Event
import com.swent.assos.model.data.User
import com.swent.assos.model.navigation.Destinations
import com.swent.assos.screens.EventDetailsScreen
import com.swent.assos.ui.screens.assoDetails.EventDetails
import dagger.hilt.android.testing.HiltAndroidTest
import io.github.kakaocup.compose.node.element.ComposeScreen
import io.mockk.confirmVerified
import io.mockk.verify
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
class EventDetailsTest : SuperTest() {
  private val assoID = "02s16UZba2Bsx5opTcQb"
  private val event1 = Event("123456", "description", assoID, Uri.EMPTY, "assoId")

  private val profileId = "dxpZJlPsqzWAmBI47qtx3jvGMHX2"
  private val firstName = "Antoine"
  private val lastName = "Marchand"

  private val memberAssociation = Association("QjAOBhVVcL0P2G1etPgk")

  val user =
      User(
          id = profileId,
          firstName = firstName,
          lastName = lastName,
          email = "antoine.marchand@epfl.ch",
          associations = listOf(Triple(memberAssociation.id, "Chef de projet", 1)),
          sciper = "330249",
          semester = "GM-BA6")

  private val event2 = Event("123457", "title", memberAssociation.id, Uri.EMPTY, "description")

  override fun setup() {

    DataCache.currentUser.value = user

    FirebaseFirestore.getInstance().collection("events").add(event1)
    FirebaseFirestore.getInstance().collection("events").add(event2)
    FirebaseFirestore.getInstance().collection("users").add(user)
  }

  @Test
  fun testEventDetails() {

    composeTestRule.activity.setContent {
      EventDetails(eventId = event1.id, assoId = assoID, navigationActions = mockNavActions)
    }

    run {
      ComposeScreen.onComposeScreen<EventDetailsScreen>(composeTestRule) {
        step("I want to join") { composeTestRule.onNodeWithText("Become Staff").performClick() }
      }
      step("I changed my mind") { composeTestRule.onNodeWithText("No").performClick() }

      step("I want to join again") { composeTestRule.onNodeWithText("Become Staff").performClick() }
      step("Confirm") { composeTestRule.onNodeWithText("Yes").performClick() }
    }
  }

  @Test
  fun testCreateTicketButton() {

    composeTestRule.activity.setContent {
      EventDetails(
          eventId = event2.id, navigationActions = mockNavActions, assoId = memberAssociation.id)
    }

    run {
      ComposeScreen.onComposeScreen<EventDetailsScreen>(composeTestRule) {
        step("I want to create a ticket") {
          composeTestRule.onNodeWithText("Create ticket").performClick()
        }
        step("Check if we actually navigate to create a ticket screen") {
          verify { mockNavActions.navigateTo(Destinations.CREATE_TICKET.route) }
          confirmVerified(mockNavActions)
        }
      }
    }
  }
}
