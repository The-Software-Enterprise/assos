package com.swent.assos

import androidx.activity.compose.setContent
import androidx.compose.ui.test.isDisplayed
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.swent.assos.model.data.DataCache
import com.swent.assos.model.data.Event
import com.swent.assos.model.data.News
import com.swent.assos.model.data.User
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

  val user2 =
      User(
          id = "22222",
          firstName = "Anna",
          lastName = "Yildiran",
          email = "anna.yildiran@epfl.ch",
          appliedAssociation = List(1) { assoId },
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
  fun unfollowAsso() {
    DataCache.currentUser.value.following = List(1) { assoId }
    run {
      ComposeScreen.onComposeScreen<AssoDetailsScreen>(composeTestRule) {
        step("UnFollow association") {
          followButton {
            assertIsDisplayed()
            performClick()
            assert(!DataCache.currentUser.value.following.contains(assoId))
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
  fun removeApplicationIsDisplayed() {

    val id = "190719"

    FirebaseFirestore.getInstance().collection("users").document(user2.id).set(user2)

    FirebaseFirestore.getInstance()
        .collection("associations")
        .document(assoId)
        .collection("applicants")
        .document(id)
        .set(mapOf("userId" to user2.id, "status" to "pending", "createdAt" to Timestamp.now()))

    DataCache.currentUser.value = user2

    composeTestRule.activity.setContent {
      AssoDetails(assoId = assoId, navigationActions = mockNavActions)
    }

    run {
      ComposeScreen.onComposeScreen<AssoDetailsScreen>(composeTestRule) {
        step("Request to remove request to join association") {
          composeTestRule.onNodeWithTag("JoinUsButton").isDisplayed()
          composeTestRule.onNodeWithTag("JoinUsButton").performClick()
        }
      }
    }
  }

  @Test
  fun joinUsIsDisplayed() {

    val id = "190719"

    val user = DataCache.currentUser.value

    DataCache.currentUser.value.associations = emptyList()
    DataCache.currentUser.value.appliedAssociation = emptyList()

    FirebaseFirestore.getInstance()
        .collection("associations/$assoId/applicants")
        .document(id)
        .delete()

    FirebaseFirestore.getInstance()
        .collection("users")
        .document(user.id)
        .update("appliedAssociation", FieldValue.arrayRemove(assoId))

    run {
      ComposeScreen.onComposeScreen<AssoDetailsScreen>(composeTestRule) {
        step("Request to remove request to join association") {
          composeTestRule.onNodeWithText("Join Us").isDisplayed()
          composeTestRule.onNodeWithText("Join Us").performClick()
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
