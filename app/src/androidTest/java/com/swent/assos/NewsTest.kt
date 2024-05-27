package com.swent.assos

import android.net.Uri
import androidx.activity.compose.setContent
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.isDisplayed
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.swent.assos.model.data.DataCache
import com.swent.assos.model.data.Event
import com.swent.assos.model.data.News
import com.swent.assos.model.data.User
import com.swent.assos.model.view.EventViewModel
import com.swent.assos.screens.CreateNewsScreen
import com.swent.assos.screens.NewsScreen
import com.swent.assos.ui.screens.News
import com.swent.assos.ui.screens.manageAsso.CreateNews
import dagger.hilt.android.testing.HiltAndroidTest
import io.github.kakaocup.compose.node.element.ComposeScreen
import java.time.LocalDateTime
import kotlin.random.Random
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
class NewsTest : SuperTest() {

  private val randomInt = Random.nextInt()

  private lateinit var eventViewModel: EventViewModel

  private val newsTitle = "Test news $randomInt"
  private val newsDescription = "Test description $randomInt"

  private val assoIDFollowing = "0qrqbQkbjTqbKgHG1akT"
  private val assoIDNotFollowing = "05DUlszwHL5YZTb1Jwo8"

  private val oneMoreEvent =
      Event(
          id = "3333EVENT",
          title = "Event 0qrqbQkbjTqbKgHG1akT",
          associationId = assoIDFollowing,
          image = Uri.EMPTY,
          description = "Event Description",
          startTime = LocalDateTime.now().plusHours(4),
          isStaffingEnabled = true,
      )

  private val eventFollowing =
      Event(
          id = "1111EVENT",
          title = "Event 0qrqbQkbjTqbKgHG1akT",
          associationId = assoIDFollowing,
          image = Uri.EMPTY,
          description = "Event Description",
          startTime = LocalDateTime.now().plusHours(1),
          isStaffingEnabled = true,
      )
  private val eventNotFollowing =
      Event(
          id = "2222EVENT",
          title = "Event 05DUlszwHL5YZTb1Jwo8",
          associationId = assoIDNotFollowing,
          image = Uri.EMPTY,
          description = "Event Description",
          startTime = LocalDateTime.now().plusHours(1),
          isStaffingEnabled = true,
      )

  private val newsOfTheEventFollowing =
      News(
          id = "0000NEWS",
          title = "Event 0qrqbQkbjTqbKgHG1akT",
          description = "News Description",
          images = listOf(Uri.EMPTY),
          createdAt = LocalDateTime.now().minusHours(2),
          associationId = assoIDFollowing,
          eventId = "1111EVENT")

  private val newsFollowing =
      News(
          id = "1111NEWS",
          title = "News 0qrqbQkbjTqbKgHG1akT",
          description = "News Description",
          images = listOf(Uri.EMPTY),
          createdAt = LocalDateTime.now().minusHours(2),
          associationId = assoIDFollowing)

  private val newsOfTheEventNotFollowing =
      News(
          id = "2222NEWS",
          title = "Event 05DUlszwHL5YZTb1Jwo8",
          description = "News Description",
          images = listOf(Uri.EMPTY),
          createdAt = LocalDateTime.now().minusHours(2),
          associationId = assoIDNotFollowing,
          eventId = "2222EVENT")
  private val newsNotFollowing =
      News(
          id = "2222NEWS",
          title = "News 05DUlszwHL5YZTb1Jwo8",
          description = "News Description",
          images = listOf(Uri.EMPTY),
          createdAt = LocalDateTime.now().minusHours(2),
          associationId = assoIDNotFollowing)

  val user =
      User(
          id = "11111",
          firstName = "Paul",
          lastName = "Levebre",
          email = "paul.levebre@epfl.ch",
          associations = emptyList(),
          sciper = "330245",
          semester = "GM-BA2")

  override fun setup() {
    super.setup()
    DataCache.currentUser.value = User("", "", "", "", listOf(), listOf(), "", "")
  }

  @Test
  fun createNewsAndVerifyCreation() {
    composeTestRule.activity.setContent {
      CreateNews(navigationActions = mockNavActions, assoId = "jMWo6NgngIS2hCq054TF")
    }
    run {
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
          createButton { performClick() }
        }
      }
    }

    composeTestRule.activity.setContent { News(navigationActions = mockNavActions) }

    run {
      ComposeScreen.onComposeScreen<NewsScreen>(composeTestRule) {
        step("Check if the news is displayed") {
          composeTestRule.waitUntil(5000) {
            composeTestRule.onNodeWithText(newsTitle).isDisplayed()
          }
          newsList { assertIsDisplayed() }
          newsListItem { assertIsDisplayed() }
        }
      }
    }
  }
}
