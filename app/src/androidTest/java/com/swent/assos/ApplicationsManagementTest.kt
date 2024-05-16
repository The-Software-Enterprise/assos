package com.swent.assos

import androidx.activity.compose.setContent
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.swent.assos.model.navigation.Destinations
import com.swent.assos.screens.ApplicationManagementScreen
import com.swent.assos.screens.AssoDetailsScreen
import com.swent.assos.screens.LoginScreen
import com.swent.assos.screens.ManageAssoScreen
import com.swent.assos.screens.MyAssociationsScreen
import com.swent.assos.screens.SignupScreen
import com.swent.assos.ui.login.SignUpScreen
import com.swent.assos.ui.screens.assoDetails.AssoDetails
import com.swent.assos.ui.screens.manageAsso.ApplicationManagement
import com.swent.assos.ui.screens.manageAsso.ManageAssociation
import com.swent.assos.ui.screens.profile.Following
import com.swent.assos.ui.screens.profile.MyAssociations
import dagger.hilt.android.testing.HiltAndroidTest
import io.github.kakaocup.compose.node.element.ComposeScreen
import io.mockk.confirmVerified
import io.mockk.verify
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
class ApplicationsManagementTest : SuperTest() {

  override fun setup() {
    super.setup()
    composeTestRule.activity.setContent { Following(navigationActions = mockNavActions) }
  }

  @Test
  fun ApplyAndVerifyApplicationDisplay() {
    run {
      ComposeScreen.onComposeScreen<LoginScreen>(composeTestRule) {
        step("Click on signup") {
          signUpButton {
            assertIsDisplayed()
            performClick()
          }
        }
        verify { mockNavActions.navigateTo(Destinations.SIGN_UP) }
        confirmVerified(mockNavActions)
      }
    }
    composeTestRule.activity.setContent { SignUpScreen(navigationActions = mockNavActions) }
    run {
      ComposeScreen.onComposeScreen<SignupScreen>(composeTestRule) {
        step("Fill the Sign Up form") {
          emailField {
            assertIsDisplayed()
            performTextInput("anna.yildiran@epfl.ch")
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

    composeTestRule.activity.setContent {
      AssoDetails("9hciFZwTKU9rWg4r0B2A", navigationActions = mockNavActions)
    }

    run {
      ComposeScreen.onComposeScreen<AssoDetailsScreen>(composeTestRule) {
        step("Join association") {
          joinButton {
            assertIsDisplayed()
            performClick()
          }
        }
      }
    }

    composeTestRule.activity.setContent {
      ManageAssociation(navigationActions = mockNavActions, assoId = "9hciFZwTKU9rWg4r0B2A")
    }

    run {
      ComposeScreen.onComposeScreen<ManageAssoScreen>(composeTestRule) {
        step("Go to the list of applications") {
          applicationsButton {
            assertIsDisplayed()
            performClick()
          }

          verify { mockNavActions.navigateTo(Destinations.APPLICATION_MANAGEMENT) }
          confirmVerified(mockNavActions)
        }
      }
    }

    composeTestRule.activity.setContent {
      ApplicationManagement(assoId = "9hciFZwTKU9rWg4r0B2A", navigationActions = mockNavActions)
    }

    run {
      ComposeScreen.onComposeScreen<ApplicationManagementScreen>(composeTestRule) {
        step("Check if the application is displayed") {
          applicationList { assertIsDisplayed() }
          applicationListItem {
            assertIsDisplayed()
            assertTextContains(value = "Yildiran", substring = true, ignoreCase = true)
          }

          acceptButton {
            assertIsDisplayed()
            performClick()
          }
        }
      }
    }

    composeTestRule.activity.setContent { MyAssociations(navigationActions = mockNavActions) }

    run {
      ComposeScreen.onComposeScreen<MyAssociationsScreen>(composeTestRule) {
        step("Check if the association is displayed") {
          associationCard {
            assertIsDisplayed()
            performClick()
          }
        }
      }
    }
  }
}
