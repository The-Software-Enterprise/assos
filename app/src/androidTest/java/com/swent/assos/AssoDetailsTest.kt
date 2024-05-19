package com.swent.assos

import androidx.activity.compose.setContent
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.firebase.firestore.FirebaseFirestore
import com.swent.assos.model.data.DataCache
import com.swent.assos.model.data.Event
import com.swent.assos.model.data.News
import com.swent.assos.model.serialize
import com.swent.assos.screens.AssoDetailsScreen
import com.swent.assos.ui.screens.assoDetails.AssoDetails
import dagger.hilt.android.testing.HiltAndroidTest
import io.github.kakaocup.compose.node.element.ComposeScreen
import io.mockk.confirmVerified
import io.mockk.verify
import org.junit.Test
import org.junit.runner.RunWith
import java.time.LocalDateTime

@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
class AssoDetailsTest : SuperTest() {
  val assoId = "jMWo6NgngIS2hCq054TF"
  val acronym = "180°C"

  val testEvent =
      Event(
          id = "1",
          title = "Test event",
          description = "Test event description",
          associationId = assoId,
          startTime = LocalDateTime.of(2025, 1, 1, 0, 0),
          endTime = LocalDateTime.of(2025, 1, 1, 0, 1))

  val testNews =
      News(
          id = "2",
          title = "Test news",
          description = "Test news description",
          associationId = assoId,
      )

  override fun setup() {
    super.setup()
    FirebaseFirestore.getInstance().collection("events").add(serialize(testEvent))
    FirebaseFirestore.getInstance().collection("news").add(serialize(testNews))
    composeTestRule.activity.setContent {
      AssoDetails(assoId = assoId, navigationActions = mockNavActions)
    }
  }

  @Test
  fun followAsso() {
    DataCache.currentUser.value.following = emptyList()
    run {
      ComposeScreen.onComposeScreen<AssoDetailsScreen>(composeTestRule) {
        step("Follow association") {
          followButton {
            assertIsDisplayed()
            performClick()
            assert(DataCache.currentUser.value.following.contains(assoId))
          }
        }
      }
    }
  }

  @Test
  fun goBack() {
    run {
      ComposeScreen.onComposeScreen<AssoDetailsScreen>(composeTestRule) {
        step("Go back") {
          goBackButton { performClick() }
          verify { mockNavActions.goBack() }
          confirmVerified(mockNavActions)
        }
      }
    }
  }

  @Test
  fun joinAssociation() {
    DataCache.currentUser.value.associations = emptyList()
    run {
      ComposeScreen.onComposeScreen<AssoDetailsScreen>(composeTestRule) {
        step("Join association") {
          joinButton {
            assertIsDisplayed()
            performClick()
          }
        }
      }
    }
  }

  @Test
  fun testThatTheEventIsDisplayed() {
    run {
      ComposeScreen.onComposeScreen<AssoDetailsScreen>(composeTestRule) {
        step("Check that the event is displayed") { eventItem { assertIsDisplayed() } }
      }
    }
  }

  @Test
  fun testThatTheNewsIsDisplayed() {
    run {
      ComposeScreen.onComposeScreen<AssoDetailsScreen>(composeTestRule) {
        step("Check that the news is displayed") { newsItem { assertIsDisplayed() } }
      }
    }
  }
}
