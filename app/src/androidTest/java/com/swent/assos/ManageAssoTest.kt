package com.swent.assos

import androidx.activity.compose.setContent
import androidx.compose.ui.test.hasText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.swent.assos.model.navigation.Destinations
import com.swent.assos.screens.ManageAssoScreen
import com.swent.assos.ui.screens.manageAsso.ManageAssociation
import dagger.hilt.android.testing.HiltAndroidTest
import io.github.kakaocup.compose.node.element.ComposeScreen
import io.mockk.confirmVerified
import io.mockk.verify
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
class ManageAssoTest : SuperTest() {
  val assoId = "SHJfKFTgplnrMzoBdDGS"
  val acronym = "Swiss Solar Boat"
  val fullname = "Association participating in the Monaco Solar & Energy Boat Challenge – MAKE"

  override fun setup() {
    super.setup()
    composeTestRule.activity.setContent {
      ManageAssociation(assoId = assoId, navigationActions = mockNavActions)
    }
  }

  @Test
  fun descriptionContainsFullname() {
    run {
      ComposeScreen.onComposeScreen<ManageAssoScreen>(composeTestRule) {
        step("Check if description contains full name") {
          description {
            assertIsDisplayed()
            assert(hasText(fullname, substring = true, ignoreCase = true))
          }
        }
      }
    }
  }

  @Test
  fun addEventNavigatesToEventScreen() {
    run {
      ComposeScreen.onComposeScreen<ManageAssoScreen>(composeTestRule) {
        step("Add event") {
          addEventButton {
            assertIsDisplayed()
            performClick()
          }
        }
        step("Check if we actually navigate to event screen") {
          verify { mockNavActions.navigateTo(Destinations.CREATE_EVENT.route + "/${assoId}") }
          confirmVerified(mockNavActions)
        }
      }
    }
  }

  @Test
  fun goBackButtonNavigatesToMyAssociations() {
    run {
      ComposeScreen.onComposeScreen<ManageAssoScreen>(composeTestRule) {
        step("Go back") {
          goBackButton {
            assertIsDisplayed()
            performClick()
          }
        }
        step("Check if we really navigate back to my associations") {
          verify { mockNavActions.goBack() }
          confirmVerified(mockNavActions)
        }
      }
    }
  }
}
