package com.swent.assos

import androidx.activity.compose.setContent
import androidx.compose.ui.test.hasText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.firebase.firestore.FirebaseFirestore
import com.swent.assos.model.data.DataCache
import com.swent.assos.model.data.ParticipationStatus
import com.swent.assos.model.data.Ticket
import com.swent.assos.model.data.User
import com.swent.assos.model.navigation.Destinations
import com.swent.assos.screens.MyTicketsScreen
import com.swent.assos.ui.screens.ticket.MyTickets
import dagger.hilt.android.testing.HiltAndroidTest
import io.github.kakaocup.compose.node.element.ComposeScreen
import io.mockk.confirmVerified
import io.mockk.verify
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
class MyTicketsTest : SuperTest() {

  private val profileId = "liddejsp"
  private val eventId = "soireeVendee"
  private val ticketId = "le85estunbeaudepartment"

  override fun setup() {

    DataCache.currentUser.value = User(id = profileId, tickets = listOf(ticketId))
    FirebaseFirestore.getInstance()
        .collection("tickets")
        .document("le85estunbeaudepartment")
        .set(Ticket(id = ticketId, eventId = eventId, userId = profileId, status = ParticipationStatus.Participant))
    FirebaseFirestore.getInstance()
        .collection("users")
        .document(profileId)
      .collection("tickets").document(ticketId).set(Ticket(id = ticketId, eventId = eventId, userId = profileId, status = ParticipationStatus.Participant))

    composeTestRule.activity.setContent { MyTickets(navigationActions = mockNavActions) }
  }

  @Test
  fun myTicketsDisplaysTheCorrectPageTitle() {
    run {
      ComposeScreen.onComposeScreen<MyTicketsScreen>(composeTestRule) {
        step("Check if page title is displayed") {
          pageTitle {
            assertIsDisplayed()
            assert(hasText("My tickets", substring = true, ignoreCase = true))
          }
        }
      }
    }
  }

  @Test
  fun clickOnMyTicketRedirectsToTicketDetails() {
    run {
      ComposeScreen.onComposeScreen<MyTicketsScreen>(composeTestRule) {
        step("Check if my ticket is displayed and perform click") {
          ticketItem {
            assertIsDisplayed()
            performClick()
          }
        }
      }
      verify { mockNavActions.navigateTo(Destinations.TICKET_DETAILS.route + "/${eventId}") }
      confirmVerified(mockNavActions)
    }
  }

  @Test
  fun clickOnFloatingActionButtonRedirectsToScanTicket() {
    run {
      ComposeScreen.onComposeScreen<MyTicketsScreen>(composeTestRule) {
        step("Check if floating action button is displayed and perform click") {
          addImages {
            assertIsDisplayed()
            performClick()
          }
        }
      }
      verify { mockNavActions.navigateTo(Destinations.SCAN_TICKET.route) }
      confirmVerified(mockNavActions)
    }
  }
}
