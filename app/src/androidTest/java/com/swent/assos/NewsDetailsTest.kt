package com.swent.assos

import androidx.activity.compose.setContent
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.kaspersky.components.composesupport.config.withComposeSupport
import com.kaspersky.kaspresso.kaspresso.Kaspresso
import com.kaspersky.kaspresso.testcases.api.testcase.TestCase
import com.swent.assos.model.navigation.NavigationActions
import com.swent.assos.screens.NewsDetailsScreen
import com.swent.assos.ui.screens.assoDetails.NewsDetails
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
class NewsDetailsTest : TestCase(kaspressoBuilder = Kaspresso.Builder.withComposeSupport()) {

  @get:Rule(order = 1) var hiltRule = HiltAndroidRule(this)

  @get:Rule(order = 2) var composeTestRule = createAndroidComposeRule<MainActivity>()

  // This rule automatic initializes lateinit properties with @MockK, @RelaxedMockK, etc.
  @get:Rule(order = 3) val mockkRule = MockKRule(this)

  @RelaxedMockK lateinit var mockNavActions: NavigationActions

  @Before
  fun setup() {
    hiltRule.inject()
    composeTestRule.activity.setContent {
      NewsDetails(newsId = "GTi6W84Se3DGjfK6Z7MG", navigationActions = mockNavActions)
    }
  }

  @Test
  fun createNewsAndVerifyNewsDetails() {

    run {
      ComposeScreen.onComposeScreen<NewsDetailsScreen>(composeTestRule) {
        step("Check the News details is correctly displayed") {
          title { assertIsDisplayed() }
          description { assertIsDisplayed() }
          mainImage { assertIsDisplayed() }
          subImageList { assertIsDisplayed() }
          subImageList { assertIsDisplayed() }
        }
      }
    }
  }
}
