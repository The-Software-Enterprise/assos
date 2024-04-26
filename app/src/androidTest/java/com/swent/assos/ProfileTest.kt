package com.swent.assos

import androidx.activity.compose.setContent
import androidx.compose.ui.test.hasText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.swent.assos.model.data.DataCache
import com.swent.assos.model.data.User
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

  private val profileId = "dxpZJlPsqzWAmBI47qtx3jvGMHX2"
  private val firstName = "Antoine"
  private val lastName = "Marchand"

  override fun setup() {
    DataCache.currentUser.value =
        User(
            id = profileId,
            firstName = firstName,
            lastName = lastName,
            email = "antoine.marchand@epfl.ch",
            associations = listOf(Triple("QjAOBhVVcL0P2G1etPgk", "Chef de projet", 1)),
            sciper = "330249",
            semester = "GM-BA6")
    super.setup()
    composeTestRule.activity.setContent { Profile(navigationActions = mockNavActions) }
    // signup

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
  fun signoutButtonTriggersNavigation() {

    run {
      ComposeScreen.onComposeScreen<ProfileScreen>(composeTestRule) {
        step("Click on signout button") { signoutButton { assertIsDisplayed() } }
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

  @Test
  fun profileDisplaysMyAssociations() {
    run {
      ComposeScreen.onComposeScreen<ProfileScreen>(composeTestRule) {
        step("Check if my associations are displayed") {
          myAssociationItem {
            assertIsDisplayed()
            assert(hasText("Rocket Team", substring = true, ignoreCase = true))
          }
        }
      }
    }
  }

  @Test
  fun profileDisplaysUsername() {
    run {
      ComposeScreen.onComposeScreen<ProfileScreen>(composeTestRule) {
        step("Check if username is displayed") {
          name {
            assertIsDisplayed()
            assert(hasText("$firstName $lastName", substring = true, ignoreCase = true))
          }
        }
      }
    }
  }
}
