package com.swent.assos

import androidx.activity.compose.setContent
import androidx.compose.ui.test.hasText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.swent.assos.model.data.DataCache
import com.swent.assos.model.data.User
import com.swent.assos.model.navigation.Destinations
import com.swent.assos.screens.MyAssociationsScreen
import com.swent.assos.ui.screens.profile.MyAssociations
import dagger.hilt.android.testing.HiltAndroidTest
import io.github.kakaocup.compose.node.element.ComposeScreen
import io.mockk.confirmVerified
import io.mockk.verify
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
class MyAssociationsTest : SuperTest() {

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
            sciper = "330249",
            semester = "GM-BA6")
    super.setup()
    composeTestRule.activity.setContent { MyAssociations(navigationActions = mockNavActions) }
  }

  @Test
  fun myAssociationsDisplaysTheCorrectPageTitle() {
    run {
      ComposeScreen.onComposeScreen<MyAssociationsScreen>(composeTestRule) {
        step("Check if page title is displayed") {
          pageTitle {
            assertIsDisplayed()
            assert(hasText("My Associations", substring = true, ignoreCase = true))
          }
        }
      }
    }
  }

  @Test
  fun myAssociationsDisplaysMyAssociationsAndClickTriggersNavigation() {
    run {
      ComposeScreen.onComposeScreen<MyAssociationsScreen>(composeTestRule) {
        step("Check if my associations are displayed") {
          associationCard {
            assertIsDisplayed()
            assert(hasText("Rocket Team", substring = true, ignoreCase = true))
            performClick()
          }
        }
        step("Check if we navigate to the correct association") {
          verify { mockNavActions.navigateTo(Destinations.ASSO_MODIFY_PAGE.route + "/${assoId}") }
          confirmVerified(mockNavActions)
        }
      }
    }
  }

  @Test
  fun goBackButtonNavigatesToProfile() {
    run {
      ComposeScreen.onComposeScreen<MyAssociationsScreen>(composeTestRule) {
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
