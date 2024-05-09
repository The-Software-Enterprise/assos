package com.swent.assos

import androidx.activity.compose.setContent
import androidx.compose.ui.test.hasText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.firebase.firestore.FirebaseFirestore
import com.swent.assos.model.data.DataCache
import com.swent.assos.model.data.Ticket
import com.swent.assos.model.data.User
import com.swent.assos.screens.TicketDetailsScreen
import com.swent.assos.ui.screens.ticket.TicketDetails
import dagger.hilt.android.testing.HiltAndroidTest
import io.github.kakaocup.compose.node.element.ComposeScreen
import io.mockk.confirmVerified
import io.mockk.verify
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
class TicketDetailsTest : SuperTest() {

  private val profileId = "dxpZJlPsqzWAmBI47qtx3jvGMHX2"

  private val eventId = "4sS18EaaF6qknAFqxHX2"

  override fun setup() {
    DataCache.currentUser.value = User(id = profileId, tickets = listOf("aY826AKyHh6DOjbsI1Vi"))
    FirebaseFirestore.getInstance()
        .collection("tickets")
        .add(
            Ticket(
                id = "1",
                eventId = eventId,
                userId = profileId,
            ))
    composeTestRule.activity.setContent {
      TicketDetails(eventId = eventId, navigationActions = mockNavActions)
    }
  }

  @Test
  fun ticketDetailsDisplaysTheCorrectPageTitle() {
    run {
      ComposeScreen.onComposeScreen<TicketDetailsScreen>(composeTestRule) {
        step("Check if page title is displayed") {
          pageTitle {
            assertIsDisplayed()
            assert(hasText("Rocket team meeting", substring = true, ignoreCase = true))
          }
        }
      }
    }
  }

  @Test
  fun goBackButtonNavigatesToMyTickets() {
    run {
      ComposeScreen.onComposeScreen<TicketDetailsScreen>(composeTestRule) {
        step("Go back") {
          goBackButton {
            assertIsDisplayed()
            performClick()
          }
        }
        step("Check if we really navigate back to my tickets") {
          verify { mockNavActions.goBack() }
          confirmVerified(mockNavActions)
        }
      }
    }
  }

  @Test
  fun ticketItemsAreDisplayed() {
    run {
      ComposeScreen.onComposeScreen<TicketDetailsScreen>(composeTestRule) {
        step("Check if ticket items are displayed") {
          eventImage { assertIsDisplayed() }
          startTime {
            assertIsDisplayed()
            assert(hasText("Start time: 4 MAY 2024, 16:44", substring = true, ignoreCase = true))
          }
          endTime {
            assertIsDisplayed()
            assert(hasText("End time: 4 MAY 2024, 16:44", substring = true, ignoreCase = true))
          }
          description {
            assertIsDisplayed()
            assert(hasText("Rocket team meeting", substring = true, ignoreCase = true))
          }
        }
      }
    }
  }
}
