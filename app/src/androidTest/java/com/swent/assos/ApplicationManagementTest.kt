package com.swent.assos

import androidx.activity.compose.setContent
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.isDisplayed
import androidx.compose.ui.test.isNotDisplayed
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.swent.assos.model.data.DataCache
import com.swent.assos.model.data.User
import com.swent.assos.screens.ApplicationManagementScreen
import com.swent.assos.screens.ManageAssoScreen
import com.swent.assos.ui.screens.manageAsso.ApplicationManagement
import com.swent.assos.ui.screens.manageAsso.ManageAssociation
import dagger.hilt.android.testing.HiltAndroidTest
import io.github.kakaocup.compose.node.element.ComposeScreen
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
class ApplicationManagementTest : SuperTest() {

  private val assoID = "02s16UZba2Bsx5opTcQb"
  private val applicantId = "123456"

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
          id = "33333",
          firstName = "Henri",
          lastName = "Duflot",
          email = "henri.duflot@epfl.ch",
          associations = listOf(Triple(assoID, "Director", 1)),
      )

  override fun setup() {

    super.setup()

    FirebaseFirestore.getInstance().collection("users").document(user2.id).set(user2)
    FirebaseFirestore.getInstance()
        .collection("users")
        .document(user2.id)
        .update(
            "associations",
            FieldValue.arrayUnion(mapOf("assoId" to assoID, "position" to "Director", "rank" to 1)))

    FirebaseFirestore.getInstance()
        .collection("associations")
        .document(assoID)
        .collection("applicants")
        .document(applicantId)
        .set(mapOf("userId" to user1.id, "status" to "pending", "createdAt" to Timestamp.now()))

    DataCache.currentUser.value = user2
  }

  @Test
  fun testApplicationList() {

    composeTestRule.activity.setContent {
      ManageAssociation(assoId = assoID, navigationActions = mockNavActions)
    }

    run {
      ComposeScreen.onComposeScreen<ManageAssoScreen>(composeTestRule) {
        step("Check applicant list") {
          composeTestRule.waitUntil(
              condition = { composeTestRule.onNodeWithText("Applications").isDisplayed() },
              timeoutMillis = 10000)
          composeTestRule.onNodeWithText("Applications").performClick()
        }
      }
    }

    composeTestRule.activity.setContent() {
      ApplicationManagement(navigationActions = mockNavActions, assoId = assoID)
    }

    run {
      ComposeScreen.onComposeScreen<ApplicationManagementScreen>(composeTestRule) {
        step("Check if the applications list is displayed") {
          applicationList { assertIsDisplayed() }

          applicationListItem { assertIsDisplayed() }
        }
        step("Check the accept button") { composeTestRule.onNodeWithText("Accept").performClick() }

        step("Check if the staff is accepted") {
          composeTestRule.onNodeWithText("Un-Accept").assertIsDisplayed()
          composeTestRule.onNodeWithText("Un-Accept").performClick()
        }
        step("Check if the garbage button is displayed") {
          composeTestRule.onNodeWithTag("GarbageIcon").assertIsDisplayed()
          composeTestRule.onNodeWithTag("GarbageIcon").performClick()
        }
        step("Check if the staff list is displayed") {
          composeTestRule.waitUntil(
              condition = { composeTestRule.onNodeWithTag("GarbageIcon").isNotDisplayed() },
              timeoutMillis = 10000)
        }
      }
    }
  }
}
