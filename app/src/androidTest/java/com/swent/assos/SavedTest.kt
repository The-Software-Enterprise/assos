package com.swent.assos

import androidx.activity.compose.setContent
import androidx.compose.ui.test.hasText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.swent.assos.model.data.DataCache
import com.swent.assos.model.data.User
import com.swent.assos.screens.SavedScreen
import com.swent.assos.ui.screens.profile.Saved
import dagger.hilt.android.testing.HiltAndroidTest
import io.github.kakaocup.compose.node.element.ComposeScreen
import io.mockk.confirmVerified
import io.mockk.verify
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
class SavedTest : SuperTest() {

  private val profileId = "dxpZJlPsqzWAmBI47qtx3jvGMHX2"
  private val firstName = "Antoine"
  private val lastName = "Marchand"
  private val assoId = "QjAOBhVVcL0P2G1etPgk"

  override fun setup() {
    DataCache.currentUser.value =
        User(
            id = profileId,
            firstName = firstName,
            lastName = lastName,
            email = "antoine.marchand@epfl.ch",
            associations = listOf(Triple(assoId, "Chef de projet", 1)),
            following = mutableListOf(assoId),
            sciper = "330249",
            semester = "GM-BA6")
    super.setup()
    composeTestRule.activity.setContent { Saved(navigationActions = mockNavActions) }
  }

  @Test
  fun savedDisplaysTheCorrectPageTitle() {
    run {
      ComposeScreen.onComposeScreen<SavedScreen>(composeTestRule) {
        step("Check if page title is displayed") {
          pageTitle {
            assertIsDisplayed()
            assert(hasText("Saved", substring = true, ignoreCase = true))
          }
        }
      }
    }
  }

  @Test
  fun goBackButtonNavigatesToProfile() {
    run {
      ComposeScreen.onComposeScreen<SavedScreen>(composeTestRule) {
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
