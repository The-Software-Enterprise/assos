package com.swent.assos

import androidx.activity.compose.setContent
import androidx.compose.ui.test.hasText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.swent.assos.model.navigation.Destinations
import com.swent.assos.screens.ProfileScreen
import com.swent.assos.ui.screens.Profile
import dagger.hilt.android.testing.HiltAndroidTest
import io.github.kakaocup.compose.node.element.ComposeScreen
import io.mockk.confirmVerified
import io.mockk.verify
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
class ProfileTest : SuperTest() {

  override fun setup() {
    super.setup()
    composeTestRule.activity.setContent { Profile(navigationActions = mockNavActions) }
  }

  @Test
  fun settingsButtonTriggersNavigation() {
    run {
      ComposeScreen.onComposeScreen<ProfileScreen>(composeTestRule) {
        step("Click on settings button") {
          settingsButton {
            assertIsDisplayed()
            performClick()
          }
        }
        step("Check if we actually navigate to settings screen") {
          verify { mockNavActions.navigateTo(Destinations.SETTINGS) }
          confirmVerified(mockNavActions)
        }
      }
    }
  }

  @Test
  fun myAssociationsSectionTitleHasRightContent() {
    run {
      ComposeScreen.onComposeScreen<ProfileScreen>(composeTestRule) {
        step("Check if my associations section title contains text") {
          myAssociationSectionTitle {
            assertIsDisplayed()
            assert(hasText("My associations", substring = true, ignoreCase = true))
          }
        }
      }
    }
  }

  @Test
  fun followedAssociationSectionTitleHasRightContent() {
    run {
      ComposeScreen.onComposeScreen<ProfileScreen>(composeTestRule) {
        step("Check if followed associations section title contains text") {
          followedAssociationSectionTitle {
            assertIsDisplayed()
            assert(hasText("Associations followed", substring = true, ignoreCase = true))
          }
        }
      }
    }
  }
}
