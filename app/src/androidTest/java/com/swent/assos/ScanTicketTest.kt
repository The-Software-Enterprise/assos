package com.swent.assos

<<<<<<< NFC-Frontend
import androidx.activity.compose.setContent
import androidx.compose.ui.test.hasText
=======
import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.setContent
import androidx.compose.ui.test.performClick
import androidx.core.content.ContextCompat
>>>>>>> main
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.swent.assos.model.data.DataCache
import com.swent.assos.model.data.User
import com.swent.assos.screens.ScanTicketScreen
import com.swent.assos.ui.screens.ticket.ScanTicket
import dagger.hilt.android.testing.HiltAndroidTest
import io.github.kakaocup.compose.node.element.ComposeScreen
import io.mockk.confirmVerified
<<<<<<< NFC-Frontend
=======
import io.mockk.every
import io.mockk.mockkStatic
>>>>>>> main
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
<<<<<<< NFC-Frontend
=======
    super.setup()
>>>>>>> main
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
<<<<<<< NFC-Frontend
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
=======

    composeTestRule.activity.setContent { ScanTicket(navigationActions = mockNavActions) }
>>>>>>> main
  }

  @Test
  fun goBackButtonNavigatesToMyTickets() {
<<<<<<< NFC-Frontend
=======
    // Mock the permission result to be granted
    mockkStatic(ContextCompat::class)
    every { ContextCompat.checkSelfPermission(any(), Manifest.permission.CAMERA) } returns
        PackageManager.PERMISSION_GRANTED

>>>>>>> main
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
<<<<<<< NFC-Frontend
=======

  @Test
  fun scanButtonIsDisplayed() {
    mockkStatic(ContextCompat::class)
    every { ContextCompat.checkSelfPermission(any(), Manifest.permission.CAMERA) } returns
        PackageManager.PERMISSION_GRANTED

    run {
      ComposeScreen.onComposeScreen<ScanTicketScreen>(composeTestRule) {
        step("Check if the scan button is displayed") { scanButton { assertIsDisplayed() } }
      }
    }
  }
>>>>>>> main
}
