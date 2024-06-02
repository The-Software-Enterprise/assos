package com.swent.assos

import androidx.activity.compose.setContent
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.isDisplayed
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.swent.assos.model.data.DataCache
import com.swent.assos.model.data.Event
import com.swent.assos.model.data.News
import com.swent.assos.model.data.User
import com.swent.assos.model.serialize
import com.swent.assos.screens.AssoDetailsScreen
import com.swent.assos.screens.EventDetailsScreen
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
  val polyLanId = "02s16UZba2Bsx5opTcQb"
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

  private val profileId = "dxpZJlPsqzWAmBI47qtx3jvGMHX2"
  private val firstName = "Antoine"
  private val lastName = "Marchand"
  private val memberAssociationId = "1GysfTi14xSiW4Te9fUH"

  val user =
      User(
          id = profileId,
          firstName = firstName,
          lastName = lastName,
          email = "antoine.marchand@epfl.ch",
          associations = listOf(Triple(memberAssociationId, "Chef de projet", 1)),
          sciper = "330249",
          semester = "GM-BA6",
          appliedAssociation = listOf(assoId))

  override fun setup() {

    super.setup()
    FirebaseFirestore.getInstance()
        .collection("events")
        .document(testEvent.id)
        .set(serialize(testEvent))
    FirebaseFirestore.getInstance()
        .collection("news")
        .document(testNews.id)
        .set(serialize(testNews))

    DataCache.currentUser.value = user
    FirebaseFirestore.getInstance().collection("users").document(user.id).set(serialize(user))

    FirebaseFirestore.getInstance()
        .collection("associations")
        .document(assoId)
        .collection("applicants")
        .document("323239")
        .set(mapOf("userId" to user.id, "status" to "pending", "createdAt" to Timestamp.now()))

    DataCache.currentUser.value.appliedAssociation = listOf(assoId)

    composeTestRule.activity.setContent {
      AssoDetails(assoId = assoId, navigationActions = mockNavActions)
    }
  }

  @Test
  fun testNavigationToCommitteeDetails() {
    run {
      ComposeScreen.onComposeScreen<AssoDetailsScreen>(composeTestRule) {
        step("Check if the committee button is displayed") {
          composeTestRule.onNodeWithText("The Committee").assertIsDisplayed()
        }
        step("Click on the committee button") {
          composeTestRule.onNodeWithText("The Committee").performClick()
        }
      }
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

    composeTestRule.activity.setContent {
      AssoDetails(assoId = assoId, navigationActions = mockNavActions)
    }

    run {
      ComposeScreen.onComposeScreen<AssoDetailsScreen>(composeTestRule) {
        composeTestRule.waitUntil(timeoutMillis = 5000) {
          composeTestRule.onNodeWithText("Remove application").isDisplayed()
        }
        step("I want to remove my application") {
          composeTestRule.onNodeWithText("Remove application").performClick()
        }
      }
    }
  }

  @Test
  fun joinUsIsDisplayed() {

    composeTestRule.activity.setContent {
      AssoDetails(assoId = polyLanId, navigationActions = mockNavActions)
    }

    run {
      ComposeScreen.onComposeScreen<EventDetailsScreen>(composeTestRule) {
        composeTestRule.waitUntil(timeoutMillis = 5000) {
          composeTestRule.onNodeWithText("Join Us").isDisplayed()
        }
        step("I want to join") { composeTestRule.onNodeWithText("Join Us").performClick() }
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
