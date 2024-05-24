package com.swent.assos

import android.net.Uri
import androidx.activity.compose.setContent
import androidx.compose.ui.test.isDisplayed
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.swent.assos.model.data.DataCache
import com.swent.assos.model.data.Event
import com.swent.assos.model.data.User
import com.swent.assos.model.navigation.Destinations
import com.swent.assos.model.serialize
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
  private val event1 =
      Event("123456", "description", assoID, Uri.EMPTY, "assoId", isStaffingEnabled = true)

  private val event3 =
      Event("12345678", "title", assoID, Uri.EMPTY, "description", isStaffingEnabled = true)

  private val profileId = "dxpZJlPsqzWAmBI47qtx3jvGMHX2"
  private val firstName = "Antoine"
  private val lastName = "Marchand"

  private val memberAssociationId = "1GysfTi14xSiW4Te9fUH"

  val user =
      User(
          id = profileId,
          firstName = firstName,
          lastName = lastName,
          email = "antoine.marchand@epfl.ch",
          associations = listOf(Triple(memberAssociationId, "Chef de projet", 1)),
          sciper = "330249",
          semester = "GM-BA6",
          appliedStaffing = listOf(event3.id))

  private val event2 =
      Event(
          "123457",
          "title",
          memberAssociationId,
          Uri.EMPTY,
          "description",
          isStaffingEnabled = true)

  override fun setup() {
    DataCache.currentUser.value = user
    FirebaseFirestore.getInstance().collection("users").document(user.id).set(serialize(user))
    FirebaseFirestore.getInstance().collection("events").document(event1.id).set(serialize(event1))
    FirebaseFirestore.getInstance().collection("events").document(event2.id).set(serialize(event2))
    FirebaseFirestore.getInstance().collection("events").document(event3.id).set(serialize(event2))
    FirebaseFirestore.getInstance()
        .collection("events")
        .document(event3.id)
        .collection("applicants")
        .document("323232")
        .set(mapOf("userId" to user.id, "status" to "pending", "createdAt" to Timestamp.now()))
  }

  @Test
  fun staffIsDisplayed() {

    composeTestRule.activity.setContent {
      EventDetails(event1.id, navigationActions = mockNavActions, assoId = event1.associationId)
    }

    run {
      ComposeScreen.onComposeScreen<EventDetailsScreen>(composeTestRule) {
        composeTestRule.waitUntil(timeoutMillis = 5000) {
          composeTestRule.onNodeWithText("Apply for staffing").isDisplayed()
        }
        step("I want to staff") {
          composeTestRule.onNodeWithText("Apply for staffing").performClick()
        }
      }
    }
    // }

    // @Test
    // fun unApplyStaffIsDisplayed() {

    composeTestRule.activity.setContent {
      EventDetails(event3.id, navigationActions = mockNavActions, assoId = event3.associationId)
    }

    run {
      ComposeScreen.onComposeScreen<EventDetailsScreen>(composeTestRule) {
        composeTestRule.waitUntil(timeoutMillis = 5000) {
          composeTestRule.onNodeWithText("Remove staff application").isDisplayed()
        }
        step("I want to staff") {
          composeTestRule.onNodeWithText("Remove staff application").performClick()
        }
      }
    }
  }

  @Test
  fun testCreateTicketButton() {
    composeTestRule.activity.setContent {
      EventDetails(
          eventId = event2.id, navigationActions = mockNavActions, assoId = memberAssociationId)
    }

    run {
      ComposeScreen.onComposeScreen<EventDetailsScreen>(composeTestRule) {
        composeTestRule.waitUntil(timeoutMillis = 5000) {
          composeTestRule.onNodeWithText("Create ticket").isDisplayed()
        }
        step("I want to create a ticket") {
          composeTestRule.onNodeWithText("Create ticket").performClick()
        }
        step("Check if we actually navigate to create a ticket screen") {
          verify { mockNavActions.navigateTo(Destinations.CREATE_TICKET.route + "/${event2.id}") }
          confirmVerified(mockNavActions)
        }
      }
    }
  }
}
