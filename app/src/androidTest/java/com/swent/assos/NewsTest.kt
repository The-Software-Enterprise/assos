package com.swent.assos

import androidx.activity.compose.setContent
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.kaspersky.components.composesupport.config.withComposeSupport
import com.kaspersky.kaspresso.kaspresso.Kaspresso
import com.kaspersky.kaspresso.testcases.api.testcase.TestCase
import com.swent.assos.model.navigation.Destinations
import com.swent.assos.model.navigation.NavigationActions
import com.swent.assos.screens.CreateNewsScreen
import com.swent.assos.screens.LoginScreen
import com.swent.assos.screens.NewsScreen
import com.swent.assos.screens.SignupScreen
import com.swent.assos.ui.login.LoginScreen
import com.swent.assos.ui.login.SignUpScreen
import com.swent.assos.ui.screens.News
import com.swent.assos.ui.screens.manageAsso.CreateNews
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import io.github.kakaocup.compose.node.element.ComposeScreen
import io.mockk.confirmVerified
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit4.MockKRule
import io.mockk.verify
import kotlin.random.Random
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
class NewsTest : TestCase(kaspressoBuilder = Kaspresso.Builder.withComposeSupport()) {

  @get:Rule(order = 1) var hiltRule = HiltAndroidRule(this)

  @get:Rule(order = 2) var composeTestRule = createAndroidComposeRule<MainActivity>()

  // This rule automatic initializes lateinit properties with @MockK, @RelaxedMockK, etc.
  @get:Rule(order = 3) val mockkRule = MockKRule(this)

  // Relaxed mocks methods have a default implementation returning values
  @RelaxedMockK lateinit var mockNavActions: NavigationActions

  var associationAcronym = "180°C"

  val newsHeader = "Test description -1934310868"

  @Before
  fun setup() {
    hiltRule.inject()
  }

  private val randomInt = Random.nextInt()
  private val newsTitle = "Test news $randomInt"
  private val newsDescription = "Test description $randomInt"

  @Test
  fun createNewsAndVerifyCreation() {

    run {
      composeTestRule.activity.setContent { SignUpScreen(navigationActions = mockNavActions) }
      ComposeScreen.onComposeScreen<SignupScreen>(composeTestRule) {
        step("Signup") {
          emailField {
            assertIsDisplayed()
            performTextInput("test@epfl.ch")
          }
          passwordField {
            assertIsDisplayed()
            performTextInput("test1234")
          }
          confirmPasswordField {
            assertIsDisplayed()
            performTextInput("test1234")
          }
          signUpButton {
            assertIsDisplayed()
            performClick()
          }
        }
        Thread.sleep(1000)
        verify { mockNavActions.navigateTo(Destinations.HOME) }
        confirmVerified(mockNavActions)
      }
      composeTestRule.activity.setContent { LoginScreen(navigationActions = mockNavActions) }
      ComposeScreen.onComposeScreen<LoginScreen>(composeTestRule) {
        step("Signin with good credentials") {
          emailField {
            assertIsDisplayed()
            performTextInput("test@epfl.ch")
          }
          passwordField {
            assertIsDisplayed()
            performTextInput("test1234")
          }
          loginButton {
            assertIsDisplayed()
            performClick()
          }
        }
        Thread.sleep(1000)
        verify { mockNavActions.navigateTo(Destinations.HOME.route) }
        confirmVerified(mockNavActions)
        composeTestRule.activity.setContent {
          CreateNews(navigationActions = mockNavActions, assoId = "jMWo6NgngIS2hCq054TF")
        }
        ComposeScreen.onComposeScreen<CreateNewsScreen>(composeTestRule) {
          step("Create a news") {
            form { assertIsDisplayed() }
            inputTitle {
              assertIsDisplayed()
              performClick()
              performTextInput(newsTitle)
            }
            inputDescription {
              assertIsDisplayed()
              performClick()
              performTextInput(newsDescription)
            }
            buttonSave { performClick() }
          }
        }
      }

      composeTestRule.activity.setContent { News() }

      run {
        ComposeScreen.onComposeScreen<NewsScreen>(composeTestRule) {
          step("Check if the news is displayed") {
            newsList { assertIsDisplayed() }
            newsListItems { assertIsDisplayed() }
            itemsTitle { assertTextContains(newsTitle) }
            itemsDescription { assertTextContains(newsDescription) }
            itemsAssociation { assertTextContains("Association to promote cooking amongst students") }
          }
        }
      }
    }
  }
}
