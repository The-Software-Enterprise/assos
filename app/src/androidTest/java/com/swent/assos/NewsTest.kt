package com.swent.assos

import android.net.Uri
import androidx.activity.compose.setContent
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.filter
import androidx.compose.ui.test.filterToOne
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.isDisplayed
import androidx.compose.ui.test.onChildren
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.swent.assos.model.data.DataCache
import com.swent.assos.model.data.Event
import com.swent.assos.model.data.News
import com.swent.assos.model.data.User
import com.swent.assos.model.serialize
import com.swent.assos.screens.CreateNewsScreen
import com.swent.assos.screens.NewsScreen
import com.swent.assos.ui.screens.News
import com.swent.assos.ui.screens.manageAsso.CreateNews
import dagger.hilt.android.testing.HiltAndroidTest
import io.github.kakaocup.compose.node.element.ComposeScreen
import kotlin.random.Random
import org.junit.Test
import org.junit.runner.RunWith
import java.time.LocalDateTime

@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
class NewsTest : SuperTest() {

  private val randomInt = Random.nextInt()
  private val newsTitle = "Test news $randomInt"
  private val newsDescription = "Test description $randomInt"

  private val assoIDFollowing = "02s16UZba2Bsx5opTcQb"
  private val assoIDNotFollowing = "05DUlszwHL5YZTb1Jwo8"

  private val eventFollowing =
    Event(
      id = "EVENT1111",
      title = "Event Title",
      associationId = assoIDFollowing,
      image = Uri.EMPTY,
      description = "Event Description",
      isStaffingEnabled = true,
    )
  private val eventNotFollowing =
    Event(
      id = "EVENT2222",
      title = "Event Title",
      associationId = assoIDNotFollowing,
      image = Uri.EMPTY,
      description = "Event Description",
      isStaffingEnabled = true,
    )

  private val newsFollowing =
    News(
      id = "NEWS1111",
      title = "News Title",
      description = "News Description",
      images = listOf(Uri.EMPTY),
      createdAt = LocalDateTime.now().minusHours(1),
      associationId = assoIDFollowing,
      eventIds = mutableListOf()
    )
  private val newsNotFollowing =
    News(
      id = "NEWS2222",
      title = "News Title",
      description = "News Description",
      images = listOf(Uri.EMPTY),
      createdAt = LocalDateTime.now().minusHours(1),
      associationId = assoIDNotFollowing,
      eventIds = mutableListOf()
    )

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

    FirebaseFirestore.getInstance().collection("users").document(user.id).set(user)
    FirebaseFirestore.getInstance()
      .collection("users")
      .document(user.id)
      .update(
        "associations",
        FieldValue.arrayUnion(
          mapOf("assoId" to assoIDFollowing, "position" to "Grand Chef", "rank" to 1)))

    FirebaseFirestore.getInstance().collection("events").document(eventFollowing.id).set(serialize(eventFollowing))
    FirebaseFirestore.getInstance().collection("events").document(eventNotFollowing.id).set(serialize(eventNotFollowing))
    FirebaseFirestore.getInstance().collection("news").document(newsFollowing.id).set(serialize(newsFollowing))
    FirebaseFirestore.getInstance().collection("news").document(newsNotFollowing.id).set(serialize(newsNotFollowing))

    DataCache.currentUser.value = user
  }

  @Test
  fun testHomePage() {
    composeTestRule.activity.setContent {
      News(mockNavActions)
    }


    run {
      ComposeScreen.onComposeScreen<NewsScreen>(composeTestRule) {
        step("Check if the news is displayed") {
          newsList {
            assertIsDisplayed()
          }

          followActivityTitle {
            composeTestRule.waitUntil(
              condition = { composeTestRule.onNodeWithText("Activity of associations you follow :").isDisplayed() },
              timeoutMillis = 10000)
            composeTestRule.onNodeWithText("Activity of associations you follow :").assertIsDisplayed()

          }

          composeTestRule.onNode(hasTestTag("NewsList"))
            .onChildren()
            .filter(hasTestTag("NewsListItem"))
            .onFirst()
            .assertIsDisplayed()


          for (i in 0 until 4) {
            val expectedTitle = "Title"
            val expectedDescription = "Description"

            composeTestRule.onNode(hasTestTag("NewsList"))
              .onChildren()
              .filter(hasTestTag("NewsListItem"))
              .get(i)
              .assertTextContains(expectedTitle)
              .assertTextContains(expectedDescription)

          }


          followActivityTitle {
            composeTestRule.waitUntil(
              condition = { composeTestRule.onNodeWithText("Activity of associations you follow :").isDisplayed() },
              timeoutMillis = 10000)
            composeTestRule.onNodeWithText("Activity of associations you follow :").assertIsDisplayed()

          }

          newsListItem { assertIsDisplayed() }
        }
      }
    }

  }



  /*@Test
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
          // newsItemTitle { assertTextContains(newsTitle) }
          // itemsDescription { assertTextContains(newsDescription) }
        }
      }
    }
  }*/

}
