package com.swent.assos

import androidx.activity.compose.setContent
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.isDisplayed
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.firebase.firestore.FirebaseFirestore
import com.swent.assos.model.data.DataCache
import com.swent.assos.model.data.News
import com.swent.assos.model.data.User
import com.swent.assos.model.serialize
import com.swent.assos.screens.NewsDetailsScreen
import com.swent.assos.ui.screens.Explorer
import com.swent.assos.ui.screens.News
import com.swent.assos.ui.screens.assoDetails.NewsDetails
import dagger.hilt.android.testing.HiltAndroidTest
import io.github.kakaocup.compose.node.element.ComposeScreen
import io.mockk.confirmVerified
import io.mockk.verify
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
class NewsDetailsTest : SuperTest() {

  val associationID = "GTi6W84Se3DGjfK6Z7MG"

  val fakeUser =
      User(
          id = "SomeUserId",
          email = "someone.important@epfl.ch",
          firstName = "Someone",
          lastName = "Important",
          associations = listOf(Triple(associationID, "someRank", 1)),
          following = listOf(associationID),
      )

  val OtherFakeUser = User(id = "SomeOtherUserId", email = "someone.notImportant@epflch")

  val news: News =
      News(
          id = "SomeNewsId",
          title = "Rocket team meeting",
          description = "Rocket team meeting",
          associationId = associationID,
      )
  val serNews: Map<String, Any> = serialize(news)

  @Before
  override fun setup() {
    super.setup()
    FirebaseFirestore.getInstance()
        .collection("users")
        .document(fakeUser.id)
        .set(serialize(fakeUser))
    FirebaseFirestore.getInstance()
        .collection("users")
        .document(OtherFakeUser.id)
        .set(serialize(OtherFakeUser))
    FirebaseFirestore.getInstance().collection("news").document(news.id).set(serNews)
    composeTestRule.activity.setContent {
      NewsDetails(newsId = news.id, navigationActions = mockNavActions)
    }
  }

  @Test
  fun goBackButtonNavigatesToHome() {
    run {
      ComposeScreen.onComposeScreen<NewsDetailsScreen>(composeTestRule) {
        step("Go back") {
          goBackButton {
            assertIsDisplayed()
            performClick()
          }
        }
        step("Check if we really navigate back to profile") {
          verify { mockNavActions.goBack() }
          confirmVerified(mockNavActions)
        }
      }
    }
  }

  @Test
  fun createNewsAndVerifyNewsDetails() {

    run {
      ComposeScreen.onComposeScreen<NewsDetailsScreen>(composeTestRule) {
        step("Check the News details is correctly displayed") {
          composeTestRule.waitUntil(10000) {
            composeTestRule.onNodeWithText(news.description).isDisplayed()
          }
          composeTestRule.onNodeWithTag("descriptionText").assertIsDisplayed()
        }
      }
    }
  }

  @Test
  fun deleteNews() {
    DataCache.currentUser.value = fakeUser
    run {
      ComposeScreen.onComposeScreen<NewsDetailsScreen>(composeTestRule) {
        step("Delete News") {
          composeTestRule.waitUntil(5000) {
            composeTestRule.onNodeWithTag("DeleteButton").isDisplayed()
          }
          composeTestRule.onNodeWithTag("DeleteButton").performClick()
        }
        step("cancel deletion") { composeTestRule.onNodeWithText("No").performClick() }
        step("Delete News") { composeTestRule.onNodeWithTag("DeleteButton").performClick() }
        step("confirm deletion") { composeTestRule.onNodeWithText("Yes").performClick() }
        step("check if we really delete the news") {
          verify { mockNavActions.goBack() }
          confirmVerified(mockNavActions)
        }
      }
    }

    composeTestRule.activity.setContent {
      Explorer(navigationActions = mockNavActions)
      run {
        step("Go check Home") { composeTestRule.onNodeWithText("Home").performClick() }
        step("Check if element is still there") {
          composeTestRule.onNodeWithText(news.title).assertDoesNotExist()
        }
      }
    }
  }
}
