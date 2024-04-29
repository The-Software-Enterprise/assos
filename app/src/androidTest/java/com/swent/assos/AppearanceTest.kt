package com.swent.assos

import androidx.activity.compose.setContent
import androidx.compose.ui.test.hasText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.swent.assos.model.data.DataCache
import com.swent.assos.model.data.User
import com.swent.assos.screens.NotificationSettingsScreen
import com.swent.assos.ui.screens.profile.Appearance
import dagger.hilt.android.testing.HiltAndroidTest
import io.github.kakaocup.compose.node.element.ComposeScreen
import io.mockk.confirmVerified
import io.mockk.verify
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
class AppearanceTest : SuperTest() {

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
    composeTestRule.activity.setContent { Appearance(navigationActions = mockNavActions) }
  }

  @Test
  fun appearanceDisplaysTheCorrectPageTitle() {
    run {
      ComposeScreen.onComposeScreen<NotificationSettingsScreen>(composeTestRule) {
        step("Check if page title is displayed") {
          pageTitle {
            assertIsDisplayed()
            assert(hasText("Appearance", substring = true, ignoreCase = true))
          }
        }
      }
    }
  }

  @Test
  fun goBackButtonNavigatesToProfile() {
    run {
      ComposeScreen.onComposeScreen<NotificationSettingsScreen>(composeTestRule) {
        step("Go back") {
          goBackButton {
            assertIsDisplayed()
            performClick()
          }
        }
        step("Check if we really navigate back to profile") {
          verify { mockNavActions.goBack() }
          confirmVerified(mockNavActions)
        }
      }
    }
  }
}
