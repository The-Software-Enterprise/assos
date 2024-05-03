package com.swent.assos

import androidx.activity.compose.setContent
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.onNodeWithTag
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.swent.assos.model.data.DataCache
import com.swent.assos.model.data.User
import com.swent.assos.model.navigation.Destinations
import com.swent.assos.screens.ProfileScreen
import com.swent.assos.ui.screens.profile.Profile
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
  }

  @Test
  fun profileDisplaysTheCorrectPageTitle() {
    run {
      ComposeScreen.onComposeScreen<ProfileScreen>(composeTestRule) {
        step("Check if page title is displayed") {
          pagetile {
            assertIsDisplayed()
            assert(hasText("Profile", substring = true, ignoreCase = true))
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
          composeTestRule
              .onNodeWithTag("Name")
              .assertExists()
              .assertTextEquals("$firstName $lastName")
        }
      }
    }
  }

  @Test
  fun myAssociationsButtonTriggersNavigation() {
    run {
      ComposeScreen.onComposeScreen<ProfileScreen>(composeTestRule) {
        step("Click on my associations button") {
          myAssociationsButton {
            assertIsDisplayed()
            performClick()
          }
        }
        step("Check if we actually navigate to my associations screen") {
          verify { mockNavActions.navigateTo(Destinations.MY_ASSOCIATIONS) }
          confirmVerified(mockNavActions)
        }
      }
    }
  }

  @Test
  fun followingAssociationsButtonTriggersNavigation() {
    run {
      ComposeScreen.onComposeScreen<ProfileScreen>(composeTestRule) {
        step("Click on following associations button") {
          followingAssociationsButton {
            assertIsDisplayed()
            performClick()
          }
        }
        step("Check if we actually navigate to following associations screen") {
          verify { mockNavActions.navigateTo(Destinations.FOLLOWING) }
          confirmVerified(mockNavActions)
        }
      }
    }
  }

  @Test
  fun logoutButtonIsDisplayed() {
    run {
      ComposeScreen.onComposeScreen<ProfileScreen>(composeTestRule) {
        step("Logout button is displayed") { logoutButton { assertIsDisplayed() } }
      }
    }
  }

  @Test
  fun popUpDisplays() {
    run {
      ComposeScreen.onComposeScreen<ProfileScreen>(composeTestRule) {
        step("Popup is displayed") { logoutButton { performClick() } }
      }
    }
  }
}
