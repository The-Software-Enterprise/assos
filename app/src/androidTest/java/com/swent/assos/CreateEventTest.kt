package com.swent.assos

import androidx.activity.compose.setContent
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.swent.assos.screens.CreateEventScreen
import com.swent.assos.ui.screens.manageAsso.CreateEvent
import dagger.hilt.android.testing.HiltAndroidTest
import io.github.kakaocup.compose.node.element.ComposeScreen
import io.mockk.confirmVerified
import io.mockk.verify
import java.util.Calendar
import kotlin.random.Random
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
class CreateEventTest : SuperTest() {
  private val assoId = "jMWo6NgngIS2hCq054TF"
  private val randomInt = Random.nextInt()
  private val eventTitle = "Test event $randomInt"
  private val eventDescription = "Test description $randomInt"

  override fun setup() {
    super.setup()
    composeTestRule.activity.setContent {
      CreateEvent(assoId = assoId, navigationActions = mockNavActions)
    }
  }

  @Test
  fun goBack() {
    run {
      ComposeScreen.onComposeScreen<CreateEventScreen>(composeTestRule) {
        step("Go back") {
          goBackButton { performClick() }
          verify { mockNavActions.goBack() }
          confirmVerified(mockNavActions)
        }
      }
    }
  }

  @Test
  fun createEventDisplaysTheCorrectPageTitle() {
    run {
      ComposeScreen.onComposeScreen<CreateEventScreen>(composeTestRule) {
        step("Check if page title is displayed") {
          pageTitle {
            assertIsDisplayed()
            assert(hasText("Create an event", substring = true, ignoreCase = true))
          }
        }
      }
    }
  }

  @Test
  fun createSimpleEvent() {
    run {
      ComposeScreen.onComposeScreen<CreateEventScreen>(composeTestRule) {
        step("Fill the form") {
          // form { assertIsDisplayed() }
          inputTitle {
            assertIsDisplayed()
            performClick()
            performTextInput(eventTitle)
          }
          inputDescription {
            assertIsDisplayed()
            performClick()
            performTextInput(eventDescription)
          }
        }
      }
    }
  }

  @Test
  fun createEventCancel() {
    run {
      ComposeScreen.onComposeScreen<CreateEventScreen>(composeTestRule) {
        step("Fill the form") {
          // form { assertIsDisplayed() }
          inputTitle {
            assertIsDisplayed()
            performClick()
            performTextInput(eventTitle)
          }
          inputDescription {
            assertIsDisplayed()
            performClick()
            performTextInput(eventDescription)
          }
        }

        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_MONTH, 1)
        val tomorrowDate = calendar.get(Calendar.DAY_OF_MONTH)
        // next month
        val nextMonth = calendar.add(Calendar.MONTH, 1)

        step("choose the start time") {
          startTimePicker { performClick() }
          // select the time
          composeTestRule.onNodeWithText("OK").assertExists()
          composeTestRule.onNodeWithText("Cancel").performClick()
        }
        composeTestRule.waitForIdle()
        step("choose the end time") { endTimePicker { performClick() } }
      }
    }
  }

  @Test
  fun testCreateAnEntireEvent() {
    run {
      ComposeScreen.onComposeScreen<CreateEventScreen>(composeTestRule) {
        step("Fill the form") {
          // form { assertIsDisplayed() }
          inputTitle {
            assertIsDisplayed()
            performClick()
            performTextInput(eventTitle)
          }
          inputDescription {
            assertIsDisplayed()
            performClick()
            performTextInput(eventDescription)
          }
        }

        step("choose the start time") {
          startTimePicker { performClick() }
          // select the time
          composeTestRule.onNodeWithText("OK").assertExists()
          composeTestRule.onNodeWithText("OK").performClick()
          composeTestRule.onNodeWithText("OK").performClick()
        }

        step("choose the end time") {
          startTimePicker { performClick() }
          // select the time
          composeTestRule.onNodeWithText("OK").assertExists()
          composeTestRule.onNodeWithText("OK").performClick()
          composeTestRule.onNodeWithText("OK").performClick()
        }

        step("test the image") { image { performClick() } }
        composeTestRule.waitForIdle()
      }
    }
  }
}
