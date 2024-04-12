package com.swent.assos

import androidx.activity.compose.setContent
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.kaspersky.components.composesupport.config.withComposeSupport
import com.kaspersky.kaspresso.kaspresso.Kaspresso
import com.kaspersky.kaspresso.testcases.api.testcase.TestCase
import com.swent.assos.model.navigation.NavigationActions
import com.swent.assos.screens.OverviewScreen
import com.swent.assos.ui.screens.Overview
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
class OverviewTest : TestCase(kaspressoBuilder = Kaspresso.Builder.withComposeSupport()) {

  @get:Rule(order = 1) var hiltRule = HiltAndroidRule(this)

  @get:Rule(order = 2) var composeTestRule = createAndroidComposeRule<MainActivity>()

  // This rule automatic initializes lateinit properties with @MockK, @RelaxedMockK, etc.
  @get:Rule(order = 3) val mockkRule = MockKRule(this)

  // Relaxed mocks methods have a default implementation returning values
  @RelaxedMockK lateinit var mockNavActions: NavigationActions

  @Before
  fun setup() {
    hiltRule.inject()
    composeTestRule.activity.setContent { Overview(navigationActions = mockNavActions) }
  }

  @Test
  fun searchAssoFilterList() = run {
    ComposeScreen.onComposeScreen<OverviewScreen>(composeTestRule) {
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
      assoListItems { assertIsDisplayed() }
    }
  }

  @Test
  fun navigateToAssoDigestScreen() = run {
    ComposeScreen.onComposeScreen<OverviewScreen>(composeTestRule) {
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

      assoListItems {
        assertIsDisplayed()

        performClick()
      }
    }

    verify {
      mockNavActions.navigateTo(
          "AssociationPage/jMWo6NgngIS2hCq054TF/180°C/Association to promote cooking amongst students/https%3A%2F%2Fwww.180c.ch%2Fassociation%2F")
    }
    confirmVerified(mockNavActions)
  }

  @Test
  fun titleHasTheRightContent() = run {
    ComposeScreen.onComposeScreen<OverviewScreen>(composeTestRule) {
      appTitle1 { assertTextContains("Student") }
      appTitle2 { assertTextContains("Sphere") }
    }
  }
}
