package com.swent.assos

import android.net.Uri
import androidx.activity.compose.setContent
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.firebase.firestore.FirebaseFirestore
import com.swent.assos.model.data.DataCache
import com.swent.assos.model.data.Event
import com.swent.assos.model.data.News
import com.swent.assos.model.data.User
import com.swent.assos.model.navigation.Destinations
import com.swent.assos.model.serialize
import com.swent.assos.screens.SavedScreen
import com.swent.assos.ui.screens.profile.Saved
import dagger.hilt.android.testing.HiltAndroidTest
import io.github.kakaocup.compose.node.element.ComposeScreen
import io.mockk.confirmVerified
import io.mockk.verify
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
class SavedTest : SuperTest() {

  private val profileId = "dxpZJlPsqzWAmBI47qtx3jvGMHX2"
  private val firstName = "Antoine"
  private val lastName = "Marchand"
  private val assoId = "QjAOBhVVcL0P2G1etPgk"

  private val event = Event("123456", "description", assoId, Uri.EMPTY, "description")

  private val news: News =
      News(
          id = "SomeNewsId",
          title = "Rocket team meeting",
          description = "Rocket team meeting",
          associationId = assoId,
      )

  private val myUser =
      User(
          id = profileId,
          firstName = firstName,
          lastName = lastName,
          email = "antoine.marchand@epfl.ch",
          associations = listOf(Triple(assoId, "Chef de projet", 1)),
          following = mutableListOf(assoId),
          sciper = "330249",
          semester = "GM-BA6",
          savedNews = listOf(news.id),
          savedEvents = listOf(event.id))

  val otherUser =
      User(
          id = "11111",
          firstName = "Paul",
          lastName = "Levebre",
          email = "paul.levebre@epfl.ch",
          associations = emptyList(),
          sciper = "330245",
          semester = "GM-BA2")

  override fun setup() {
    FirebaseFirestore.getInstance().collection("news").document(news.id).set(serialize(news))
    FirebaseFirestore.getInstance().collection("events").document(event.id).set(serialize(event))
    FirebaseFirestore.getInstance().collection("users").document(myUser.id).set(serialize(myUser))
    DataCache.currentUser.value = myUser
    super.setup()
    composeTestRule.activity.setContent { Saved(navigationActions = mockNavActions) }
  }

  @Test
  fun savedDisplaysTheCorrectPageTitle() {
    run {
      ComposeScreen.onComposeScreen<SavedScreen>(composeTestRule) {
        step("Check if page title is displayed") {
          pageTitle {
            assertIsDisplayed()
            assert(hasText("Saved", substring = true, ignoreCase = true))
          }
        }
      }
    }
  }

  @Test
  fun goBackButtonNavigatesToProfile() {
    run {
      ComposeScreen.onComposeScreen<SavedScreen>(composeTestRule) {
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
  fun savedEventIsDisplayedAndClickNavigatesToEventPage() {
    run {
      ComposeScreen.onComposeScreen<SavedScreen>(composeTestRule) {
        step("Check if saved events are displayed") {
          savedEvents {
            assertIsDisplayed()
            performClick()
          }
        }
        step("Check if we navigate to the correct event") {
          verify {
            mockNavActions.navigateTo(
                Destinations.EVENT_DETAILS.route + "/${event.id}" + "/${assoId}")
          }
          confirmVerified(mockNavActions)
        }
      }
    }
  }

  @Test
  fun testThatTheSegmentedControlButtonIsDisplayed() {
    run {
      ComposeScreen.onComposeScreen<SavedScreen>(composeTestRule) {
        step("Check if the segmented control button is displayed") {
          segmentedControl { assertIsDisplayed() }
        }
      }
    }
  }

  @Test
  fun savedNewsIsDisplayedAndClickNavigatesToNewsPage() {
    run {
      ComposeScreen.onComposeScreen<SavedScreen>(composeTestRule) {
        step("Check if the segmented control button navigates to saved news") {
          composeTestRule.onNodeWithText("News").assertIsDisplayed()
          composeTestRule.onNodeWithText("News").performClick()
        }
        step("Check if saved news are displayed") {
          savedNews {
            assertIsDisplayed()
            performClick()
          }
        }
        step("Check if we navigate to the correct news") {
          verify {
            mockNavActions.navigateTo(
                Destinations.NEWS_DETAILS.route + "/${news.id}" + "/${news.associationId}")
          }
          confirmVerified(mockNavActions)
        }
      }
    }
  }

  @Test
  fun testTheEmptyList() {
    DataCache.currentUser.value = otherUser
    FirebaseFirestore.getInstance()
        .collection("users")
        .document(otherUser.id)
        .set(serialize(otherUser))
    run {
      ComposeScreen.onComposeScreen<SavedScreen>(composeTestRule) {
        step("Check if the empty list is displayed") {
          composeTestRule.onNodeWithText("No results were found").assertIsDisplayed()
        }
        step("Navigates to saved news") {
          composeTestRule.onNodeWithText("News").assertIsDisplayed()
          composeTestRule.onNodeWithText("News").performClick()
        }
        step("Check if the empty list is displayed") {
          composeTestRule.onNodeWithText("No results were found").assertIsDisplayed()
        }
      }
    }
  }
}
