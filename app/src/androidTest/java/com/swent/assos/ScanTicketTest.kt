package com.swent.assos

import androidx.activity.compose.setContent
import androidx.compose.ui.test.hasText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.swent.assos.model.data.DataCache
import com.swent.assos.model.data.User
import com.swent.assos.screens.ScanTicketScreen
import com.swent.assos.ui.screens.ticket.ScanTicket
import dagger.hilt.android.testing.HiltAndroidTest
import io.github.kakaocup.compose.node.element.ComposeScreen
import io.mockk.confirmVerified
import io.mockk.verify
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
class ScanTicketTest : SuperTest() {

  private val profileId = "dxpZJlPsqzWAmBI47qtx3jvGMHX2"
  private val firstName = "Antoine"
  private val lastName = "Marchand"

  private val eventId = "4sS18EaaF6qknAFqxHX2"

  override fun setup() {
    DataCache.currentUser.value =
        User(
            id = profileId,
            firstName = firstName,
            lastName = lastName,
            email = "antoine.marchand@epfl.ch",
            associations = listOf(Triple("QjAOBhVVcL0P2G1etPgk", "Chef de projet", 1)),
            sciper = "330249",
            semester = "GM-BA6",
            tickets = listOf("aY826AKyHh6DOjbsI1Vi"))
    super.setup()
    composeTestRule.activity.setContent { ScanTicket(navigationActions = mockNavActions) }
  }

  @Test
  fun myTicketsDisplaysTheCorrectPageTitle() {
    run {
      ComposeScreen.onComposeScreen<ScanTicketScreen>(composeTestRule) {
        step("Check if page title is displayed") {
          pageTitle {
            assertIsDisplayed()
            assert(hasText("Scan a ticket", substring = true, ignoreCase = true))
          }
        }
      }
    }
  }

  @Test
  fun goBackButtonNavigatesToMyTickets() {
    run {
      ComposeScreen.onComposeScreen<ScanTicketScreen>(composeTestRule) {
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
}
