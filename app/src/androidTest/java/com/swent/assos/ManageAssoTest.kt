package com.swent.assos

import androidx.activity.compose.setContent
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.firebase.firestore.FirebaseFirestore
import com.swent.assos.model.data.Event
import com.swent.assos.model.data.News
import com.swent.assos.model.navigation.Destinations
import com.swent.assos.model.serialize
import com.swent.assos.screens.ManageAssoScreen
import com.swent.assos.ui.screens.manageAsso.ManageAssociation
import dagger.hilt.android.testing.HiltAndroidTest
import io.github.kakaocup.compose.node.element.ComposeScreen
import io.mockk.confirmVerified
import io.mockk.verify
import java.time.LocalDateTime
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
class ManageAssoTest : SuperTest() {
  val assoId = "B986lqXA5lKTuG7zgs5T"
  val acronym = "Swiss Solar Boat"
  val fullname = "Association participating in the Monaco Solar & Energy Boat Challenge – MAKE"

  val testEvent =
      Event(
          id = "1",
          title = "Test event 1",
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
      ManageAssociation(assoId = assoId, navigationActions = mockNavActions)
    }
  }

  @Test
  fun addEventNavigatesToEventScreen() {
    run {
      ComposeScreen.onComposeScreen<ManageAssoScreen>(composeTestRule) {
        step("Add event") {
          addEventButton {
            assertIsDisplayed()
            performClick()
          }
        }
        step("Check if we actually navigate to event screen") {
          verify { mockNavActions.navigateTo(Destinations.CREATE_EVENT.route + "/${assoId}") }
          confirmVerified(mockNavActions)
        }
      }
    }
  }

  @Test
  fun goBackButtonNavigatesToMyAssociations() {
    run {
      ComposeScreen.onComposeScreen<ManageAssoScreen>(composeTestRule) {
        step("Go back") {
          goBackButton {
            assertIsDisplayed()
            performClick()
          }
        }
        step("Check if we really navigate back to my associations") {
          verify { mockNavActions.goBack() }
          confirmVerified(mockNavActions)
        }
      }
    }
  }

  @Test
  fun testThatTheEventIsDisplayed() {
    run {
      ComposeScreen.onComposeScreen<ManageAssoScreen>(composeTestRule) {
        step("Check that the event is displayed") { eventItem { assertIsDisplayed() } }
      }
    }
  }

  @Test
  fun testThatTheNewsIsDisplayed() {
    run {
      ComposeScreen.onComposeScreen<ManageAssoScreen>(composeTestRule) {
        step("Check that the news is displayed") { newsItem { assertIsDisplayed() } }
      }
    }
  }

  @Test
  fun testCreatePosNav() {
    run {
      ComposeScreen.onComposeScreen<ManageAssoScreen>(composeTestRule) {
        step("Create position") {
          composeTestRule.onNodeWithTag("AddPositionButton").performClick()
        }
        step("Check if we actually navigate to create position screen") {
          verify { mockNavActions.navigateTo(Destinations.CREATE_POSITION.route + "/${assoId}") }
          confirmVerified(mockNavActions)
        }
      }
    }
  }
}
