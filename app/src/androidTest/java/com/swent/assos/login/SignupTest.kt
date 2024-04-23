package com.swent.assos.login

import androidx.activity.compose.setContent
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.firebase.auth.FirebaseAuth
import com.kaspersky.components.composesupport.config.withComposeSupport
import com.kaspersky.kaspresso.kaspresso.Kaspresso
import com.kaspersky.kaspresso.testcases.api.testcase.TestCase
import com.swent.assos.MainActivity
import com.swent.assos.model.navigation.Destinations
import com.swent.assos.model.navigation.NavigationActions
import com.swent.assos.screens.login.LoginScreen
import com.swent.assos.screens.login.SignupScreen
import com.swent.assos.ui.screens.login.LoginScreen
import com.swent.assos.ui.screens.login.SignUpScreen
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import io.github.kakaocup.compose.node.element.ComposeScreen
import io.mockk.confirmVerified
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit4.MockKRule
import io.mockk.verify
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
    val firebaseAuth = FirebaseAuth.getInstance()
    firebaseAuth.signOut()
  }

  @Test
  fun navigateSignup() {
    composeTestRule.activity.setContent { LoginScreen(navigationActions = mockNavActions) }

    run {
      ComposeScreen.onComposeScreen<LoginScreen>(composeTestRule) {
        step("Open Signup Screen") {
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
        step("Open Signup Screen") {
          loginNavButton {
            assertIsDisplayed()
            performClick()
          }
        }
        // wait for the navigation to be done
        Thread.sleep(1000)

        verify { mockNavActions.navigateTo(Destinations.LOGIN) }
        confirmVerified(mockNavActions)
      }
    }
  }

  @Test
  fun signup() {
    composeTestRule.activity.setContent { SignUpScreen(navigationActions = mockNavActions) }

    run {
      ComposeScreen.onComposeScreen<SignupScreen>(composeTestRule) {
        step("Signup") {
          emailField { performTextInput("antoine.marchand@epfl.ch") }
          step("password") {
            passwordField {
              assertIsDisplayed()
              performTextInput("123456")
            }
          }
          step("confirm password") {
            confirmPasswordField {
              assertIsDisplayed()
              performTextInput("123456")
            }
          }
          step("nav") {
            signUpButton {
              assertIsDisplayed()
              performClick()
            }
          }
          Thread.sleep(1000)

          verify { mockNavActions.navigateTo(Destinations.HOME) }
          confirmVerified(mockNavActions)
        }
      }
    }
  }

  @Test
  fun signupNonemail() {
    composeTestRule.activity.setContent { SignUpScreen(navigationActions = mockNavActions) }
    run {
      ComposeScreen.onComposeScreen<SignupScreen>(composeTestRule) {
        step("Signup") {
          emailField { performTextInput("Iamnotanemail") }
          step("password") {
            passwordField {
              assertIsDisplayed()
              performTextInput("123456")
            }
          }
          step("confirm password") {
            confirmPasswordField {
              assertIsDisplayed()
              performTextInput("123456")
            }
          }
          step("nav") {
            signUpButton {
              assertIsDisplayed()
              performClick()
            }
          }
          step("verify navigation to home") { signUpButton { assertIsDisplayed() } }
        }
      }
    }
  }
}
