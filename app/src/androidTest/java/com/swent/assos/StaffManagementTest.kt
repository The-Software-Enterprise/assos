package com.swent.assos

import android.net.Uri
import androidx.activity.compose.setContent
import androidx.compose.runtime.getValue
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.isDisplayed
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.swent.assos.model.data.DataCache
import com.swent.assos.model.data.Event
import com.swent.assos.model.data.User
import com.swent.assos.model.service.impl.serialize
import com.swent.assos.screens.EventDetailsScreen
import com.swent.assos.screens.StaffManagementScreen
import com.swent.assos.ui.screens.assoDetails.EventDetails
import com.swent.assos.ui.screens.manageAsso.StaffManagement
import dagger.hilt.android.testing.HiltAndroidTest
import io.github.kakaocup.compose.node.element.ComposeScreen
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
class StaffManagementTest : SuperTest() {

  private val assoID = "02s16UZba2Bsx5opTcQb"

  private val event1 =
      Event(
          id = "ABCEDF",
          title = "title",
          associationId = assoID,
          image = Uri.EMPTY,
          description = "Description",
      )

  val user1 =
      User(
          id = "11111",
          firstName = "Paul",
          lastName = "Levebre",
          email = "paul.levebre@epfl.ch",
          associations = emptyList(),
          sciper = "330245",
          semester = "GM-BA2")

  val user2 =
      User(
          id = "22222",
          firstName = "Anna",
          lastName = "Yildiran",
          email = "anna.yildiran@epfl.ch",
          associations = listOf(Triple(assoID, "Grand Chef", 1)),
      )

  override fun setup() {

    super.setup()

    FirebaseFirestore.getInstance().collection("users").document(user2.id).set(user2)
    FirebaseFirestore.getInstance()
        .collection("users")
        .document(user2.id)
        .update(
            "associations",
            FieldValue.arrayUnion(
                mapOf("assoId" to assoID, "position" to "Grand Chef", "rank" to 1)))
    FirebaseFirestore.getInstance().collection("events").document(event1.id).set(serialize(event1))
    FirebaseFirestore.getInstance()
        .collection("events")
        .document(event1.id)
        .collection("applicants")
        .add(mapOf("userId" to user1.id, "status" to "pending", "createdAt" to Timestamp.now()))

    DataCache.currentUser.value = user2
  }

  @Test
  fun testStaffList() {

    composeTestRule.activity.setContent {
      EventDetails(eventId = event1.id, assoId = assoID, navigationActions = mockNavActions)
    }

    run {
      ComposeScreen.onComposeScreen<EventDetailsScreen>(composeTestRule) {
        step("Check staff list") {
          composeTestRule.waitUntil(
              condition = { composeTestRule.onNodeWithText("Staff List").isDisplayed() },
              timeoutMillis = 10000)
          composeTestRule.onNodeWithText("Staff List").performClick()
        }
      }
    }

    composeTestRule.activity.setContent() {
      StaffManagement(navigationActions = mockNavActions, eventId = event1.id)
    }

    run {
      ComposeScreen.onComposeScreen<StaffManagementScreen>(composeTestRule) {
        step("Check if the staff list is displayed") {
          staffList { assertIsDisplayed() }

          staffItem { assertIsDisplayed() }
        }
        step("Check the accept button") { composeTestRule.onNodeWithText("Accept").performClick() }

        step("Check if the staff is accepted") {
          composeTestRule.onNodeWithText("Un-Accept").assertIsDisplayed()
          composeTestRule.onNodeWithText("Un-Accept").performClick()
        }
      }
    }
  }
}
