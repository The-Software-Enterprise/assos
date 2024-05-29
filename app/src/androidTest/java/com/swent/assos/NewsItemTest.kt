package com.swent.assos

import android.net.Uri
import androidx.activity.compose.setContent
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.swent.assos.model.data.News
import com.swent.assos.model.navigation.Destinations
import com.swent.assos.screens.NewsItemScreen
import com.swent.assos.ui.components.NewsItem
import dagger.hilt.android.testing.HiltAndroidTest
import io.github.kakaocup.compose.node.element.ComposeScreen
import io.mockk.confirmVerified
import io.mockk.verify
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
class NewsItemTest : SuperTest() {

  private val news =
      News(title = "title", description = "description", associationId = "1UYICvqVKbImYMNK3Sz3")

  override fun setup() {
    super.setup()
    composeTestRule.activity.setContent {
      NewsItem(news = news, navigationActions = mockNavActions)
    }
  }

  @Test
  fun testWithEmptyimages() {
    run {
      ComposeScreen.onComposeScreen<NewsItemScreen>(composeTestRule) {
        composeTestRule.onNodeWithTag("NewsItemImage", true).assertIsDisplayed()
        composeTestRule.onNodeWithText("title").assertIsDisplayed()
        composeTestRule.onNodeWithText("description").assertIsDisplayed()
        newsItem {
          assertIsDisplayed()
          performClick()
        }
        verify {
          mockNavActions.navigateTo(
              Destinations.NEWS_DETAILS.route + "/${news.id}" + "/${news.associationId}")
        }
        confirmVerified(mockNavActions)
      }
    }
  }

  @Test
  fun testWithNonEmptyImages() {
    run {
      news.images = listOf(Uri.EMPTY)
      ComposeScreen.onComposeScreen<NewsItemScreen>(composeTestRule) {
        composeTestRule.onNodeWithTag("NewsItemImage", true).assertIsDisplayed()
        composeTestRule.onNodeWithText("title").assertIsDisplayed()
        composeTestRule.onNodeWithText("description").assertIsDisplayed()
        newsItem {
          assertIsDisplayed()
          performClick()
        }
        verify {
          mockNavActions.navigateTo(
              Destinations.NEWS_DETAILS.route + "/${news.id}" + "/${news.associationId}")
        }
        confirmVerified(mockNavActions)
      }
    }
  }
}
