package com.swent.assos

import android.app.Application
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.kaspersky.components.composesupport.config.withComposeSupport
import com.kaspersky.kaspresso.kaspresso.Kaspresso
import com.kaspersky.kaspresso.testcases.api.testcase.TestCase
import com.swent.assos.model.navigation.Destinations
import com.swent.assos.model.navigation.NavigationActions
import com.swent.assos.model.view.OverviewViewModel
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

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class OverviewTest: TestCase(kaspressoBuilder = Kaspresso.Builder.withComposeSupport()) {

  @get:Rule(order = 1)
  var hiltRule = HiltAndroidRule(this)

  @get:Rule(order = 2)
  val composeTestRule = createComposeRule()

  // This rule automatic initializes lateinit properties with @MockK, @RelaxedMockK, etc.
  @get:Rule(order = 3) val mockkRule = MockKRule(this)

  // Relaxed mocks methods have a default implementation returning values
  @RelaxedMockK
  lateinit var mockNavActions: NavigationActions

  @Before
  fun testSetup() {
    hiltRule.inject()
    composeTestRule.setContent {
      Overview(navigationActions = mockNavActions)
    }
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

          performTextInput("Challenge")
        }
      }

      // Check if something is displayed when
      // inputting "Challenge"
      assoListItems { assertIsDisplayed() }
    }
  }

  @Test
  fun navigateToAssoDigestScreen() = run {
    ComposeScreen.onComposeScreen<OverviewScreen>(composeTestRule) {
      assoListItems {
        assertIsDisplayed()

        performClick()
      }
    }

    verify { mockNavActions.navigateTo(Destinations.ASSOCIATION_PAGE.route) }
    confirmVerified(mockNavActions)
  }

  @Test
  fun titleHasTheRightContent() = run {
    ComposeScreen.onComposeScreen<OverviewScreen>(composeTestRule) {
      appTitle {
        assertTextContains("Student")
        assertTextContains("Sphere")
      }
    }
  }
}