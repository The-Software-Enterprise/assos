package com.swent.assos

import androidx.activity.compose.setContent
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.firebase.auth.FirebaseAuth
import com.kaspersky.components.composesupport.config.withComposeSupport
import com.kaspersky.kaspresso.kaspresso.Kaspresso
import com.kaspersky.kaspresso.testcases.api.testcase.TestCase
import com.swent.assos.model.navigation.NavigationActions
import com.swent.assos.screens.LoginScreen
import com.swent.assos.screens.SignupScreen
import com.swent.assos.ui.login.LoginScreen
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import io.github.kakaocup.compose.node.element.ComposeScreen
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit4.MockKRule
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
class SignupTest : TestCase(kaspressoBuilder = Kaspresso.Builder.withComposeSupport()) {

  @get:Rule(order = 1) var hiltRule = HiltAndroidRule(this)

  @get:Rule(order = 2) var composeTestRule = createAndroidComposeRule<MainActivity>()

  // This rule automatic initializes lateinit properties with @MockK, @RelaxedMockK, etc.
  @get:Rule(order = 3) val mockkRule = MockKRule(this)

  // Relaxed mocks methods have a default implementation returning values
  @RelaxedMockK lateinit var mockNavActions: NavigationActions

  @Before
  fun setup() {
    hiltRule.inject()
    composeTestRule.activity.setContent { LoginScreen(navigationActions = mockNavActions) }
    val firebaseAuth = FirebaseAuth.getInstance()
    firebaseAuth.signOut()
  }

  @Test
  fun navigateSignup() = run {
    ComposeScreen.onComposeScreen<LoginScreen>(composeTestRule) {
      step("Open Signup Screen") {
        signUpButton {
          assertIsDisplayed()
          performClick()
        }
      }
      step("assert signup screen") {
        ComposeScreen.onComposeScreen<SignupScreen>(composeTestRule) {
          signupScreen { assertIsDisplayed() }
        }
        step("Open Login Screen") {
          loginButton {
            assertIsDisplayed()
            performClick()
          }
          step("assert login screen") {
            ComposeScreen.onComposeScreen<LoginScreen>(composeTestRule) {
              loginScreen { assertIsDisplayed() }
            }
          }
          step("Open Signup Screen") {
            signUpButton {
              assertIsDisplayed()
              performClick()
            }
          }
          // we are not supposed to be in signup screen
          step("assert signup screen") {
            ComposeScreen.onComposeScreen<SignupScreen>(composeTestRule) {
              signupScreen { assertIsNotDisplayed() }
            }
          }
        }
      }
    }
  }

  fun signup() = run {
    ComposeScreen.onComposeScreen<SignupScreen>(composeTestRule) {
      // check if this email is already used

      step("Enter email") {
        emailField {
          assertIsDisplayed()
          performTextInput("marin.philippe@epfl.ch")
        }
      }
      step("Enter password") {
        passwordField {
          assertIsDisplayed()
          performTextInput("password")
        }
      }
      step("Enter confirm password") {
        confirmPasswordField {
          assertIsDisplayed()
          performTextInput("password")
        }
      }
      step("Click signup") {
        signUpButton {
          assertIsDisplayed()
          performClick()
        }
      }
    }
  }
}
