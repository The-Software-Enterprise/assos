package com.swent.assos

import androidx.activity.compose.setContent
import androidx.compose.ui.test.hasText
import androidx.test.ext.junit.runners.AndroidJUnit4
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

  override fun setup() {
    super.setup()
    composeTestRule.activity.setContent { CreateTicket(navigationActions = mockNavActions) }
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
}
