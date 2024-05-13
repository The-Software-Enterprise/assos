package com.swent.assos

import androidx.activity.compose.setContent
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.isDisplayed
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.swent.assos.model.data.Association
import com.swent.assos.model.data.User
import com.swent.assos.screens.CreateTicketScreen
import com.swent.assos.screens.MyAssociationsScreen
import com.swent.assos.ui.screens.ticket.CreateTicket
import dagger.hilt.android.testing.HiltAndroidTest
import io.github.kakaocup.compose.node.element.ComposeScreen
import io.mockk.confirmVerified
import io.mockk.verify
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
class CreateTicketTest : SuperTest() {
  private val assoID = "02s16UZba2Bsx5opTcQb"
  private val profileId = "1"
  private val firstName = "Antoine"
  private val lastName = "Marchand"
  private val memberAssociation = Association(assoID)

  private val user1 =
      User(
          id = profileId,
          firstName = firstName,
          lastName = lastName,
          email = "antoine.marchand@epfl.ch",
          associations = listOf(Triple(memberAssociation.id, "Chef de projet", 1)),
          sciper = "330249",
          semester = "GM-BA6")

  private val user2 =
      User(
          id = "2",
          firstName = "John",
          lastName = "Doe",
          email = "john.doe@epfl.ch",
          associations = listOf(),
          sciper = "330250",
          semester = "GM-BA6")

  override fun setup() {
    super.setup()
    composeTestRule.activity.setContent {
      CreateTicket(navigationActions = mockNavActions, eventId = memberAssociation.id)
    }
      composeTestRule.waitForIdle()
  }

  @Test
  fun createTicketDisplaysTheCorrectPageTitle() {
    run {

      ComposeScreen.onComposeScreen<CreateTicketScreen>(composeTestRule) {
        step("Check if page title is displayed") {
          pageTitle {
            assertIsDisplayed()
            assert(hasText("Create a ticket", substring = true, ignoreCase = true))
          }
        }
      }
    }
  }

  @Test
  fun goBackButtonNavigatesToEventDetails() {
    run {
      ComposeScreen.onComposeScreen<MyAssociationsScreen>(composeTestRule) {
        step("Go back") {
          goBackButton {
            assertIsDisplayed()
            performClick()
          }
        }
        step("Check if we really navigate back to event details screen") {
          verify { mockNavActions.goBack() }
          confirmVerified(mockNavActions)
        }
      }
    }
  }

  @Test
  fun enterWrongEmail() {

    run {
      ComposeScreen.onComposeScreen<CreateTicketScreen>(composeTestRule) {
        step("Enter wrong email") {
          emailField {
            assertIsDisplayed()
            performTextInput("notjohn.doe@epfl.ch")
          }
        }
        step("click") { composeTestRule.onNodeWithText("Submit").performClick() }

        composeTestRule.waitForIdle()
        // wait until test "Ok" is displayed
        composeTestRule.waitUntil(
            condition = { composeTestRule.onNodeWithText("Ok").isDisplayed() },
            timeoutMillis = 5000)

        step("dismiss") { composeTestRule.onNodeWithText("Ok").performClick() }
      }
    }
  }

  @Test
  fun enterCorrectEmail() {
    // set user2 into the database
    Firebase.firestore.collection("users").document(user2.id).set(user2)
    run {
      ComposeScreen.onComposeScreen<CreateTicketScreen>(composeTestRule) {
        step("Enter correct email") {
          emailField {
            assertIsDisplayed()
            performTextInput(user2.email)
          }
        }
        step("click") { composeTestRule.onNodeWithText("Submit").performClick() }
        composeTestRule.waitForIdle()
        // wait until test "Ok" is displayed
        composeTestRule.waitUntil(
            condition = { composeTestRule.onNodeWithText("Ok").isDisplayed() },
            timeoutMillis = 5000)

        step("dismiss") { composeTestRule.onNodeWithText("Ok").performClick() }

        val tickets = Firebase.firestore.collection("tickets").document().get()

      }
    }
  }
}
