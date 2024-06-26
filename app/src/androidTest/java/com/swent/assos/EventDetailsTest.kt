package com.swent.assos

import android.net.Uri
import androidx.activity.compose.setContent
import androidx.compose.ui.test.isDisplayed
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.swent.assos.model.data.DataCache
import com.swent.assos.model.data.Event
import com.swent.assos.model.data.User
import com.swent.assos.model.serialize
import com.swent.assos.screens.AssoDetailsScreen
import com.swent.assos.screens.EventDetailsScreen
import com.swent.assos.ui.screens.assoDetails.AssoDetails
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
      Event("123456", "description", assoID, Uri.EMPTY, "description", isStaffingEnabled = true)

  private val event3 =
      Event("12345678", "title", assoID, Uri.EMPTY, "description", isStaffingEnabled = true)

  val applicatioId1 = "1"
  val applicatioId2 = "2"
  val applicatioId3 = "3"

  // users that are applicants to the event

  private val profileId = "theIDofJoe"
  private val firstName = "Joe"
  private val lastName = "Rubber"

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
          appliedStaffing = listOf(event3.id),
      )

  private val event2 =
      Event(
          "123457",
          "title",
          memberAssociationId,
          Uri.EMPTY,
          "description",
          isStaffingEnabled = true)

  val applicant1 =
      User(
          id = "userapp1",
          email = "",
          appliedStaffing = listOf(event2.id),
          associations = emptyList(),
          savedEvents = emptyList())
  val applicant2 =
      User(
          id = "userapp2",
          email = "",
          appliedStaffing = listOf(event2.id),
          associations = emptyList(),
          savedEvents = emptyList())
  val applicant3 =
      User(
          id = "userapp3",
          email = "",
          appliedStaffing = listOf(event2.id),
          associations = emptyList(),
          savedEvents = emptyList())

  val otherFakeUser =
      User(
          id = "SomeOtherUserId",
          email = "someone.notImportant@epflch",
          savedEvents = listOf(event1.id))

  override fun setup() {
    FirebaseFirestore.getInstance().collection("users").document(user.id).set(serialize(user))
    FirebaseFirestore.getInstance().collection("events").document(event1.id).set(serialize(event1))
    FirebaseFirestore.getInstance().collection("events").document(event2.id).set(serialize(event2))
    FirebaseFirestore.getInstance().collection("events").document(event3.id).set(serialize(event2))
    FirebaseFirestore.getInstance()
        .collection("users")
        .document(applicant1.id)
        .set(serialize(applicant1))
    FirebaseFirestore.getInstance()
        .collection("users")
        .document(applicant1.id)
        .set(serialize(applicant1))
    FirebaseFirestore.getInstance()
        .collection("users")
        .document(applicant2.id)
        .set(serialize(applicant2))
    FirebaseFirestore.getInstance()
        .collection("users")
        .document(applicant3.id)
        .set(serialize(applicant3))

    FirebaseFirestore.getInstance()
        .collection("events")
        .document(event2.id)
        .collection("applicants")
        .document("323232")
        .set(
            mapOf("userId" to applicant1.id, "status" to "pending", "createdAt" to Timestamp.now()))

    FirebaseFirestore.getInstance()
        .collection("events")
        .document(event2.id)
        .collection("applicants")
        .document(applicatioId1)
        .set(
            mapOf("userId" to applicant2.id, "status" to "pending", "createdAt" to Timestamp.now()))

    FirebaseFirestore.getInstance()
        .collection("events")
        .document(event2.id)
        .collection("applicants")
        .document(applicatioId2)
        .set(
            mapOf("userId" to applicant3.id, "status" to "pending", "createdAt" to Timestamp.now()))

    FirebaseFirestore.getInstance()
        .collection("events")
        .document(event2.id)
        .collection("applicants")
        .document(applicatioId3)
        .set(mapOf("userId" to user.id, "status" to "pending", "createdAt" to Timestamp.now()))
    DataCache.currentUser.value = user
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
          eventId = event3.id, navigationActions = mockNavActions, assoId = memberAssociationId)
    }

    run {
      ComposeScreen.onComposeScreen<EventDetailsScreen>(composeTestRule) {
        composeTestRule.waitUntil(timeoutMillis = 10000) {
          composeTestRule.onNodeWithText("Add Ticket").isDisplayed()
        }
        step("I want to create a ticket") {
          composeTestRule.onNodeWithText("Add Ticket").performClick()
        }
      }
    }
  }

  @Test
  fun deleteEvent() {
    composeTestRule.activity.setContent {
      EventDetails(event2.id, navigationActions = mockNavActions, assoId = event2.associationId)
    }
    run {
      ComposeScreen.onComposeScreen<EventDetailsScreen>(composeTestRule) {
        step("Delete Event") {
          composeTestRule.waitUntil(10000) {
            composeTestRule.onNodeWithTag("DeleteButton").isDisplayed()
          }
          composeTestRule.onNodeWithTag("DeleteButton").performClick()
        }
        composeTestRule.waitUntil(10000) { composeTestRule.onNodeWithText("No").isDisplayed() }
        step("cancel deletion") { composeTestRule.onNodeWithText("No").performClick() }
        step("Delete Event") { composeTestRule.onNodeWithTag("DeleteButton").performClick() }
        step("confirm deletion") { composeTestRule.onNodeWithText("Yes").performClick() }
        step("check if we really delete the event") {
          verify { mockNavActions.goBack() }
          confirmVerified(mockNavActions)
        }
      }
    }
    composeTestRule.activity.setContent {
      AssoDetails(assoId = memberAssociationId, navigationActions = mockNavActions)
    }
    run {
      ComposeScreen.onComposeScreen<AssoDetailsScreen>(composeTestRule) {
        step("is event still there ?") {
          composeTestRule.onNodeWithText(event2.title).assertDoesNotExist()
        }
      }
    }
  }

  @Test
  fun saveEvent() {
    DataCache.currentUser.value.savedEvents = emptyList()
    composeTestRule.activity.setContent {
      EventDetails(event3.id, navigationActions = mockNavActions, assoId = event3.associationId)
    }
    run {
      ComposeScreen.onComposeScreen<EventDetailsScreen>(composeTestRule) {
        step("Save news") {
          savedIcon {
            assertIsDisplayed()
            performClick()
            assert(DataCache.currentUser.value.savedEvents.contains(event3.id))
          }
        }
      }
    }
  }

  @Test
  fun unSaveEvent() {
    FirebaseFirestore.getInstance()
        .collection("users")
        .document(otherFakeUser.id)
        .set(serialize(otherFakeUser))
    DataCache.currentUser.value = otherFakeUser
    DataCache.currentUser.value.savedEvents = listOf(event1.id)
    composeTestRule.activity.setContent {
      EventDetails(
          eventId = event1.id, navigationActions = mockNavActions, assoId = event1.associationId)
    }
    run {
      ComposeScreen.onComposeScreen<EventDetailsScreen>(composeTestRule) {
        step("Unsave event") {
          savedIcon {
            assertIsDisplayed()
            performClick()
            composeTestRule.waitUntil {
              !DataCache.currentUser.value.savedEvents.contains(event1.id)
            }
          }
        }
      }
    }
  }
}
