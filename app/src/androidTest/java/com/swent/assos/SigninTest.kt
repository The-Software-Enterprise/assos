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
import com.swent.assos.ui.login.SignUpScreen
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
class SigninTest : TestCase(kaspressoBuilder = Kaspresso.Builder.withComposeSupport()) {

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
    fun signinWithEmptyMailOrEmptyPassword() {
        composeTestRule.activity.setContent { LoginScreen(navigationActions = mockNavActions) }
        run {
            ComposeScreen.onComposeScreen<LoginScreen>(composeTestRule) {
                step("Signin with empty mail and empty password") {
                    loginButton {
                        assertIsDisplayed()
                        performClick()
                    }
                    errorMessage {
                        assertTextEquals("Please fill in all fields")
                    }
                }
                step("Sign in with empty password") {
                    emailField {
                        assertIsDisplayed()
                        performTextInput("jules.herrscher@icloud.com")
                    }
                    loginButton {
                        assertIsDisplayed()
                        performClick()
                    }
                    errorMessage {
                        assertTextEquals("Please fill in all fields")
                    }
                }
                step("Sign in with empty email") {
                    passwordField {
                        assertIsDisplayed()
                        performTextInput("password")
                    }
                    loginButton {
                        assertIsDisplayed()
                        performClick()
                    }
                    errorMessage {
                        assertTextEquals("Please fill in all fields")
                    }
                }
            }
        }
    }

    @Test
    fun signInWithBadlyFormattedEmail() {
        composeTestRule.activity.setContent { LoginScreen(navigationActions = mockNavActions) }
        run {
            ComposeScreen.onComposeScreen<LoginScreen>(composeTestRule) {
                step("Signin with badly formatted email") {
                    emailField {
                        assertIsDisplayed()
                        performTextInput("jules.herrscher")
                    }
                    passwordField {
                        assertIsDisplayed()
                        performTextInput("password")
                    }
                    loginButton {
                        assertIsDisplayed()
                        performClick()
                    }
                    errorMessage {
                        assertTextContains("badly formatted email")
                    }
                }
            }
        }
    }

    @Test
    fun sinInWithWrongCredentials() {
        composeTestRule.activity.setContent { LoginScreen(navigationActions = mockNavActions) }
        run {
            ComposeScreen.onComposeScreen<LoginScreen>(composeTestRule) {
                step("Signin with wrong credentials") {
                    emailField {
                        assertIsDisplayed()
                        performTextInput("jules.herrscher@icloud.com")
                    }
                    passwordField {
                        assertIsDisplayed()
                        performTextInput("password")
                    }
                }
            }
        }
    }

    fun signInWithGoodCredentials() {
        composeTestRule.activity.setContent { LoginScreen(navigationActions = mockNavActions) }
        run {
            ComposeScreen.onComposeScreen<LoginScreen>(composeTestRule) {
                step("Signin with good credentials") {
                    emailField {
                        assertIsDisplayed()
                        performTextInput("jules.herrscher@epfl.ch")
                    }
                    passwordField {
                        assertIsDisplayed()
                        performTextInput("test1234")
                    }
                }
            }
        }
    }
}
