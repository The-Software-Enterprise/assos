package com.swent.assos

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.swent.assos.model.navigation.NavigationGraph
import com.swent.assos.ui.theme.AssosTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LoginViewTest {

  @get:Rule val composeTestRule = createComposeRule()
  // use createAndroidComposeRule<YourActivity>() if you need access to
  // an activity

  @Test
  fun myTest() {
    // Start the app
    composeTestRule.setContent {
      AssosTheme {
        // start from the navigation graph
        NavigationGraph()

        // then from the login screen we go to the sign up screen
      }
    }
    // Click on the sign up button
    composeTestRule.onNodeWithText("Don't have an account? Sign up").performClick()
    // Check if the sign up screen is displayed SignupScreen
    composeTestRule.onNodeWithTag("email").assertIsDisplayed()
    composeTestRule.onNodeWithTag("password").assertIsDisplayed()
    composeTestRule.onNodeWithTag("confirmPassword").assertIsDisplayed()

    // type in the email field
    composeTestRule.onNodeWithTag("email").performTextInput("marin.philippe@epfl.ch")
    // type in the password field
    composeTestRule.onNodeWithTag("password").performTextInput("password")
    // type in the confirm password field
    composeTestRule.onNodeWithTag("confirmPassword").performTextInput("password")
    // check if the password and confirm password match
    composeTestRule.onNodeWithText("Passwords do not match").assertIsNotDisplayed()

    // Click on the sign up button
    composeTestRule.onNodeWithText("Sign up").performClick()
    // wait for the sign up to be successful

    // Check if the home screen is displayed

  }
}
