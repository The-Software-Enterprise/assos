package com.swent.assos

import androidx.activity.compose.setContent
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasAnyChild
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.swent.assos.model.navigation.Destinations
import com.swent.assos.model.navigation.HomeNavigation
import com.swent.assos.screens.HomeScreen
import com.swent.assos.screens.LoginScreen
import com.swent.assos.screens.ProfileScreen
import com.swent.assos.screens.SignupScreen
import com.swent.assos.ui.login.LoginScreen
import com.swent.assos.ui.login.SignUpScreen
import dagger.hilt.android.testing.HiltAndroidTest
import io.github.kakaocup.compose.node.element.ComposeScreen
import io.mockk.confirmVerified
import io.mockk.verify
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
class EndToEnd : SuperTest() {
  override fun setup() {
    super.setup()
    composeTestRule.activity.setContent { LoginScreen(navigationActions = mockNavActions) }
  }

  @Test
  fun signupAndLogout() {
    run {
      ComposeScreen.onComposeScreen<LoginScreen>(composeTestRule) {
        step("Click on signup") {
          signUpButton {
            assertIsDisplayed()
            performClick()
          }
        }

        // check if we are on the signup screen
        verify { mockNavActions.navigateTo(Destinations.SIGN_UP) }
        confirmVerified(mockNavActions)
      }
    }
    composeTestRule.activity.setContent { SignUpScreen(navigationActions = mockNavActions) }

    run {
      ComposeScreen.onComposeScreen<SignupScreen>(composeTestRule) {
        step("Fill the form") {
          emailField {
            assertIsDisplayed()
            performTextInput("marc.pitteloud@epfl.ch")
          }
          passwordField {
            assertIsDisplayed()
            performTextInput("123456")
          }
          confirmPasswordField {
            assertIsDisplayed()
            performTextInput("123456")
          }
        }
        step("Click on signup") {
          signUpButton {
            assertIsDisplayed()
            performClick()
          }

          Thread.sleep(2000)
          // check if we are on the Home screen
          verify { mockNavActions.navigateTo(Destinations.HOME) }
          confirmVerified(mockNavActions)
        }
      }
    }

    composeTestRule.activity.setContent { HomeNavigation(navigationActions = mockNavActions) }

    run {
      ComposeScreen.onComposeScreen<HomeScreen>(composeTestRule) {
        step("Check if the profile is correct") {
          navigationBar {
            assertIsDisplayed()
            // check if child exists
            hasAnyChild(hasTestTag("NavigationBarItem3"))
            // click on child
          }
          composeTestRule.onNodeWithTag("NavigationBarItem3").performClick()
          // check if we are on the profile screen
          profileScreen { assertIsDisplayed() }
        }
        ComposeScreen.onComposeScreen<ProfileScreen>(composeTestRule) {
          step("Click on logout") {
            logoutButton {
              assertIsDisplayed()
              performClick()
            }
          }

          composeTestRule.waitForIdle()

          // check if we are on the logout dialog
          step("is alert out ?") {
            composeTestRule.onNodeWithTag("LogoutDialog").assertIsDisplayed()
          }
          step("cancel") { composeTestRule.onNodeWithTag("LogoutCancelButton").performClick() }

          step("Click on logout") {
            logoutButton {
              assertIsDisplayed()
              performClick()
            }
          }

          step("confirm") {
            logoutConfirmButton {
              composeTestRule.onNodeWithTag("LogoutConfirmButton").performClick()
            }
          }

          step("confirm nav") {
            verify { mockNavActions.navigateTo(Destinations.LOGIN) }
            confirmVerified(mockNavActions)
          }
        }
      }
    }
  }
}