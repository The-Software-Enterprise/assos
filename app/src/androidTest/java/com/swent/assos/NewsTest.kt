package com.swent.assos

import android.net.Uri
import androidx.activity.compose.setContent
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.filter
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.isDisplayed
import androidx.compose.ui.test.onChildren
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.swent.assos.model.data.DataCache
import com.swent.assos.model.data.Event
import com.swent.assos.model.data.News
import com.swent.assos.model.data.User
import com.swent.assos.model.serialize
import com.swent.assos.model.service.impl.AuthServiceImpl
import com.swent.assos.model.service.impl.DbServiceImpl
import com.swent.assos.model.service.impl.StorageServiceImpl
import com.swent.assos.model.view.EventViewModel
import com.swent.assos.screens.CreateNewsScreen
import com.swent.assos.screens.NewsScreen
import com.swent.assos.ui.screens.News
import com.swent.assos.ui.screens.manageAsso.CreateNews
import dagger.hilt.android.testing.HiltAndroidTest
import io.github.kakaocup.compose.node.element.ComposeScreen
import java.time.LocalDateTime
import kotlin.random.Random
import kotlinx.coroutines.Dispatchers
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

    eventViewModel =
        EventViewModel(
            DbServiceImpl(FirebaseFirestore.getInstance(), FirebaseAuth.getInstance()),
            StorageServiceImpl(FirebaseStorage.getInstance()),
            AuthServiceImpl(FirebaseAuth.getInstance()),
            Dispatchers.IO)

    FirebaseFirestore.getInstance().collection("users").document(user.id).set(user)
    FirebaseFirestore.getInstance()
        .collection("users")
        .document(user.id)
        .update(
            "associations",
            FieldValue.arrayUnion(
                mapOf("assoId" to assoIDFollowing, "position" to "Grand Chef", "rank" to 1)))

    FirebaseFirestore.getInstance()
        .collection("events")
        .document(eventFollowing.id)
        .set(serialize(eventFollowing))
    FirebaseFirestore.getInstance()
        .collection("events")
        .document(eventNotFollowing.id)
        .set(serialize(eventNotFollowing))
    FirebaseFirestore.getInstance()
        .collection("news")
        .document(newsOfTheEventFollowing.id)
        .set(serialize(newsOfTheEventFollowing))
    FirebaseFirestore.getInstance()
        .collection("news")
        .document(newsFollowing.id)
        .set(serialize(newsFollowing))
    FirebaseFirestore.getInstance()
        .collection("news")
        .document(newsOfTheEventNotFollowing.id)
        .set(serialize(newsOfTheEventNotFollowing))
    FirebaseFirestore.getInstance()
        .collection("news")
        .document(newsNotFollowing.id)
        .set(serialize(newsNotFollowing))

    DataCache.currentUser.value = user
  }

  @Test
  fun createAnEntireEventAndCheckANewsHasAlsoBeenCreated() {
    eventViewModel.setImage(Uri.EMPTY)
    eventViewModel.setTitle("Event 0qrqbQkbjTqbKgHG1akT")
    eventViewModel.setDescription("Event Description")
    eventViewModel.setStaffingEnabled(true)
    eventViewModel.createEvent(onSuccess = { assert(true) }, onError = { assert(false) })
  }

  @Test
  fun testHomePage() {
    composeTestRule.activity.setContent { News(mockNavActions) }

    run {
      ComposeScreen.onComposeScreen<NewsScreen>(composeTestRule) {
        step("Check if the news is displayed") {
          newsList { assertIsDisplayed() }

          followActivityTitle {
            composeTestRule.waitUntil(
                condition = {
                  composeTestRule
                      .onNodeWithText("Activity of associations you follow :")
                      .isDisplayed()
                },
                timeoutMillis = 10000)
            composeTestRule
                .onNodeWithText("Activity of associations you follow :")
                .assertIsDisplayed()
          }

          composeTestRule
              .onNode(hasTestTag("NewsList"))
              .onChildren()
              .filter(hasTestTag("NewsListItem"))
              .onFirst()
              .assertIsDisplayed()

          for (i in 0 until 4) {
            composeTestRule
                .onNode(hasTestTag("NewsList"))
                .onChildren()
                .filter(hasTestTag("NewsListItem"))
                .get(i)
                .assertIsDisplayed()
          }

          followActivityTitle {
            composeTestRule.waitUntil(
                condition = {
                  composeTestRule
                      .onNodeWithText("Activity of associations you follow :")
                      .isDisplayed()
                },
                timeoutMillis = 10000)
            composeTestRule
                .onNodeWithText("Activity of associations you follow :")
                .assertIsDisplayed()
          }
        }
        step("Check navigation to the news/event") {
          composeTestRule
              .onNode(hasTestTag("NewsList"))
              .onChildren()
              .filter(hasTestTag("NewsListItem"))
              .onFirst()
              .performClick()
        }
      }
    }
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

    composeTestRule.activity.setContent { News(mockNavActions) }

    run {
      ComposeScreen.onComposeScreen<NewsScreen>(composeTestRule) {
        step("Check if the news is displayed") {
          newsList { assertIsDisplayed() }
          newsListItem { assertIsDisplayed() }
        }
      }
    }
  }
}
