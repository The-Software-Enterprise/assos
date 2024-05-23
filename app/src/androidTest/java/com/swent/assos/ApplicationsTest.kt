package com.swent.assos

import androidx.activity.compose.setContent
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.isDisplayed
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.swent.assos.model.data.Applicant
import com.swent.assos.model.data.DataCache
import com.swent.assos.model.data.Event
import com.swent.assos.model.data.User
import com.swent.assos.model.serialize
import com.swent.assos.screens.ApplicationsScreen
import com.swent.assos.ui.screens.profile.Applications
import dagger.hilt.android.testing.HiltAndroidTest
import io.github.kakaocup.compose.node.element.ComposeScreen
import java.time.LocalDateTime
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
class ApplicationsTest : SuperTest() {

  private val assoId = "02s16UZba2Bsx5opTcQb"
  private val eventId = "eventId"
  private val user =
      User(
          id = "11111",
          firstName = "Paul",
          lastName = "Levebre",
          email = "test@epfl.ch",
          associations = emptyList(),
          sciper = "330245",
          semester = "GM-BA2",
          following = emptyList(),
          tickets = emptyList(),
          appliedAssociation = listOf(assoId),
          appliedStaffing = listOf(eventId))

  override fun setup() {
    super.setup()
    DataCache.currentUser.value = user
    Firebase.firestore
        .collection("associations/$assoId/applicants")
        .document(user.id)
        .set(
            serialize(
                Applicant(
                    userId = user.id,
                    id = "applicantId",
                    status = "pending",
                    createdAt = LocalDateTime.now())))

    Firebase.firestore
        .collection("events")
        .document(eventId)
        .set(
            serialize(
                Event(
                    id = eventId,
                    title = "test event",
                )))
    Firebase.firestore
        .collection("events/$eventId/applicants")
        .document(user.id)
        .set(
            serialize(
                Applicant(
                    userId = user.id,
                    id = "applicantId",
                    status = "accepted",
                    createdAt = LocalDateTime.now())))
    composeTestRule.activity.setContent { Applications(mockNavActions) }
  }

  @Test
  fun testAssociationApplication() {
    run {
      ComposeScreen.onComposeScreen<ApplicationsScreen>(composeTestRule) {
        step("Check if the application titles are displayed") {
          associationsApplicationsTitle { assertIsDisplayed() }
          staffingApplicationsTitle { assertIsDisplayed() }
        }
        composeTestRule.waitUntil {
          composeTestRule.onNodeWithText("Polylan").isDisplayed() &&
              composeTestRule.onNodeWithText("test event").isDisplayed()
        }
        step("Check if the association application is displayed") {
          composeTestRule.onNodeWithText("Polylan").assertIsDisplayed()
          composeTestRule.onNodeWithText("Status: pending").assertIsDisplayed()
        }
        step("Check if the staffing application is displayed") {
          composeTestRule.onNodeWithText("test event").assertIsDisplayed()
          composeTestRule.onNodeWithText("Status: accepted").assertIsDisplayed()
        }
      }
    }
  }
}
