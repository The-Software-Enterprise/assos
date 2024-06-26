package com.swent.assos

import androidx.activity.compose.setContent
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.kaspersky.components.composesupport.config.withComposeSupport
import com.kaspersky.kaspresso.kaspresso.Kaspresso
import com.kaspersky.kaspresso.testcases.api.testcase.TestCase
import com.swent.assos.model.navigation.Destinations
import com.swent.assos.model.navigation.NavigationActions
import com.swent.assos.screens.ExplorerScreen
import com.swent.assos.ui.screens.Explorer
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
class ExplorerTest : TestCase(kaspressoBuilder = Kaspresso.Builder.withComposeSupport()) {

  @get:Rule(order = 1) var hiltRule = HiltAndroidRule(this)

  @get:Rule(order = 2) var composeTestRule = createAndroidComposeRule<MainActivity>()

  // This rule automatic initializes lateinit properties with @MockK, @RelaxedMockK, etc.
  @get:Rule(order = 3) val mockkRule = MockKRule(this)

  // Relaxed mocks methods have a default implementation returning values
  @RelaxedMockK lateinit var mockNavActions: NavigationActions

  @Before
  fun setup() {
    hiltRule.inject()
    composeTestRule.activity.setContent { Explorer(navigationActions = mockNavActions) }
  }

  @Test
  fun searchAssoFilterList() = run {
    ComposeScreen.onComposeScreen<ExplorerScreen>(composeTestRule) {
      step("Open Search Bar") {
        searchAsso {
          assertIsDisplayed()
          performClick()
        }
      }

      step("search a specific Association") {
        assoListSearch {
          performTextClearance()

          performTextInput("180°C")
        }
      }

      // Check if something is displayed when
      // inputting "180°C"
      assoListoneEighty { assertIsDisplayed() }
    }
  }

  @Test
  fun navigateToAssoDigestScreen() = run {
    ComposeScreen.onComposeScreen<ExplorerScreen>(composeTestRule) {
      step("Open Search Bar") {
        searchAsso {
          assertIsDisplayed()
          performClick()
        }
      }

      step("search 180°C Association") {
        assoListSearch {
          performTextClearance()
          performTextInput("180°C")
        }
      }

      assoListoneEighty {
        assertIsDisplayed()
        performClick()
      }
    }

    // get the id of the association

    verify { mockNavActions.navigateTo("${Destinations.ASSO_DETAILS.route}/jMWo6NgngIS2hCq054TF") }
    confirmVerified(mockNavActions)
  }
}
