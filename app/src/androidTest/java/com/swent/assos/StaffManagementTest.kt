package com.swent.assos

import android.net.Uri
import androidx.activity.compose.setContent
import androidx.compose.runtime.getValue
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.swent.assos.model.data.DataCache
import com.swent.assos.model.data.Event
import com.swent.assos.model.data.User
import com.swent.assos.model.timestampToLocalDateTime
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
    FirebaseFirestore.getInstance().collection("events").document(event1.id).set(event1)
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
        step("Check staff list") { composeTestRule.onNodeWithText("Staff List").performClick() }
      }
    }

    composeTestRule.activity.setContent() {
      StaffManagement(navigationActions = mockNavActions, eventId = event1.id)
    }

    run {
      ComposeScreen.onComposeScreen<StaffManagementScreen>(composeTestRule) {
        step("Check if the staff list is displayed") {
          composeTestRule
              .onNodeWithText("Levebre", substring = true, ignoreCase = true)
              .assertIsDisplayed()
        }
      }
    }
  }

  /*composeTestRule.activity.setContent {
      StaffManagement(navigationActions = mockNavActions, eventId = event1.id)
  }

  run {
      ComposeScreen.onComposeScreen<StaffManagementScreen>(composeTestRule){

          step("Check if the staff list is displayed") {

              staffList { assertIsDisplayed()}

              staffItemName {
                  assertIsDisplayed()
                  assertTextContains(value = "Marchand", substring = true, ignoreCase = true)
              }

              staffItemAcceptButton {
                  assertIsDisplayed()
                  performClick()
              }

              FirebaseFirestore.getInstance().collection("events").document(event1.id)
                  .collection("applicants").get().result.toList()
                  .find { it["userId"] == user.id}?.let { it1 -> assert(it1.exists()) }
          }
      }
  }*/

  /*@Test
  fun ApplyAndVerifyApplicationDisplay() {
      run {
          ComposeScreen.onComposeScreen<LoginScreen>(composeTestRule) {
              step("Click on signup") {
                  signUpButton {
                      assertIsDisplayed()
                      performClick()
                  }
              }
              verify { mockNavActions.navigateTo(Destinations.SIGN_UP) }
              confirmVerified(mockNavActions)
          }
      }
      composeTestRule.activity.setContent { SignUpScreen(navigationActions = mockNavActions) }
      run {
          ComposeScreen.onComposeScreen<SignupScreen>(composeTestRule) {
              step("Fill the Sign Up form") {
                  emailField {
                      assertIsDisplayed()
                      performTextInput("anna.yildiran@epfl.ch")
                  }
                  passwordField {
                      assertIsDisplayed()
                      performTextInput("123456")
                  }
                  confirmPasswordField {
                      assertIsDisplayed()
                      performTextInput("123456")
                  }
              }
              step("Click on signup") {
                  signUpButton {
                      assertIsDisplayed()
                      performClick()
                  }

                  Thread.sleep(2000)
                  // check if we are on the Home screen
                  verify { mockNavActions.navigateTo(Destinations.HOME) }
                  confirmVerified(mockNavActions)
              }
          }
      }

      FirebaseFirestore.getInstance().collection("events").add(event1)


      composeTestRule.activity.setContent {
          EventDetails(eventId = "123456789", assoId = assoId, navigationActions = mockNavActions)
      }

      run {
          ComposeScreen.onComposeScreen<EventDetailsScreen>(composeTestRule) {
              step("Apply to staff for an event") {
                  eventStaffListButton {
                      assertIsDisplayed()
                      performClick()
                  }
              }
          }
      }



      composeTestRule.activity.setContent {
          ManageAssociation(navigationActions = mockNavActions, assoId = "9hciFZwTKU9rWg4r0B2A")}

      run {
          ComposeScreen.onComposeScreen<ManageAssoScreen>(composeTestRule) {
              step("Go to the list of applications") {
                  applicationsButton {
                      assertIsDisplayed()
                      performClick()
                  }

                  verify { mockNavActions.navigateTo(Destinations.APPLICATION_MANAGEMENT) }
                  confirmVerified(mockNavActions)
              }
          }
      }

      composeTestRule.activity.setContent {
          ApplicationManagement(assoId = "9hciFZwTKU9rWg4r0B2A", navigationActions = mockNavActions)
      }

      run{
          ComposeScreen.onComposeScreen<ApplicationManagementScreen>(composeTestRule) {
              step("Check if the application is displayed") {
                  applicationList {
                      assertIsDisplayed()
                  }
                  applicationListItem {
                      assertIsDisplayed()
                      assertTextContains(value ="Yildiran", substring = true, ignoreCase = true)
                  }

                  acceptButton {
                      assertIsDisplayed()
                      performClick()
                  }
              }
          }
      }

      composeTestRule.activity.setContent {
          MyAssociations( navigationActions = mockNavActions)
      }

      run {
          ComposeScreen.onComposeScreen<MyAssociationsScreen>(composeTestRule) {
              step("Check if the association is displayed") {
                  associationCard {
                      assertIsDisplayed()
                      performClick()
                  }
              }
          }
      }
  }*/

  private fun deserializeEvent(doc: DocumentSnapshot): Event {
    return Event(
        id = doc.id,
        title = doc.getString("title") ?: "",
        description = doc.getString("description") ?: "",
        associationId = doc.getString("associationId") ?: "",
        image = Uri.parse(doc.getString("image") ?: ""),
        startTime = timestampToLocalDateTime(doc.getTimestamp("startTime")),
        endTime = timestampToLocalDateTime(doc.getTimestamp("endTime")),
        documentSnapshot = doc)
  }
}
