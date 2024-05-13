package com.swent.assos

import androidx.activity.compose.setContent
import androidx.compose.ui.test.hasText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.swent.assos.model.data.DataCache
import com.swent.assos.model.data.User
import com.swent.assos.model.navigation.Destinations
import com.swent.assos.screens.FollowingScreen
import com.swent.assos.ui.screens.profile.Following
import dagger.hilt.android.testing.HiltAndroidTest
import io.github.kakaocup.compose.node.element.ComposeScreen
import io.mockk.confirmVerified
import io.mockk.verify
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
class FollowingTest : SuperTest() {

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
    composeTestRule.activity.setContent { Following(navigationActions = mockNavActions) }
  }

  @Test
  fun followingDisplaysTheCorrectPageTitle() {
    run {
      ComposeScreen.onComposeScreen<FollowingScreen>(composeTestRule) {
        step("Check if page title is displayed") {
          pageTitle {
            assertIsDisplayed()
            assert(hasText("Following", substring = true, ignoreCase = true))
          }
        }
      }
    }
  }

  @Test
  fun followingDisplaysCorrectlyAndClickTriggersNavigation() {
    run {
      ComposeScreen.onComposeScreen<FollowingScreen>(composeTestRule) {
        step("Check if the associations I am following is displayed") {
          associationCard {
            assertIsDisplayed()
            performClick()
          }
        }
        step("Check if we navigate to the correct association") {
          verify { mockNavActions.navigateTo(Destinations.ASSO_DETAILS.route + "/${assoId}") }
          confirmVerified(mockNavActions)
        }
      }
    }
  }

  @Test
  fun goBackButtonNavigatesToProfile() {
    run {
      ComposeScreen.onComposeScreen<FollowingScreen>(composeTestRule) {
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
