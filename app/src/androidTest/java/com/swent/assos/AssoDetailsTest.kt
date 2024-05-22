package com.swent.assos

import androidx.activity.compose.setContent
import androidx.compose.ui.test.isDisplayed
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.firebase.Timestamp
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
import java.time.LocalDateTime
import org.junit.Test
import org.junit.runner.RunWith

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

    val user = DataCache.currentUser.value
    super.setup()
    FirebaseFirestore.getInstance()
        .collection("events")
        .document(testEvent.id)
        .set(serialize(testEvent))
    FirebaseFirestore.getInstance()
        .collection("news")
        .document(testNews.id)
        .set(serialize(testNews))

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
  fun requestApplication() {
    DataCache.currentUser.value.associations = emptyList()
    DataCache.currentUser.value.appliedAssociation = emptyList()

    run {
      step("Request to join association is displayed") {
        composeTestRule.waitUntil(
            condition = { composeTestRule.onNodeWithText("Join Us").isDisplayed() },
            timeoutMillis = 5000)
      }
    }
  }

  @Test
  fun toggleRequestButtonWorksAsExpected() {
    DataCache.currentUser.value.associations = emptyList()
    DataCache.currentUser.value.appliedAssociation = List(1) { assoId }
    FirebaseFirestore.getInstance()
        .collection("associations/$assoId/applicants")
        .add(
            mapOf(
                "userId" to DataCache.currentUser.value.id,
                "status" to "pending",
                "createdAt" to Timestamp.now()))

    run {
      ComposeScreen.onComposeScreen<AssoDetailsScreen>(composeTestRule) {
        step("Request to remove request to join association") {
          composeTestRule.onNodeWithText("Remove application").isDisplayed()
        }
        step("Request to join association is displayed") {
          composeTestRule.waitUntil(
              condition = { composeTestRule.onNodeWithText("Join Us").isDisplayed() },
              timeoutMillis = 5000)
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
}
