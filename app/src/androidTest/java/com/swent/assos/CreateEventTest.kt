package com.swent.assos

import androidx.activity.compose.setContent
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.swent.assos.screens.CreateEventScreen
import com.swent.assos.ui.screens.manageAsso.createEvent.CreateEvent
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
  fun createEventCancel() {
    run {
      ComposeScreen.onComposeScreen<CreateEventScreen>(composeTestRule) {
        step("Fill the form") {
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

        step("choose the start time") {
          startTimePicker { performClick() }
          // select the time
          composeTestRule.onNodeWithText("Cancel").assertExists()
          composeTestRule.onNodeWithText("Cancel").performClick()
        }
        composeTestRule.waitForIdle()

        step("choose the end time") {
          endTimePicker { performClick() }
          composeTestRule.onNodeWithText("Cancel").assertExists()
          composeTestRule.onNodeWithText("Cancel").performClick()
        }
        composeTestRule.waitForIdle()

        step("Go back") {
          goBackButton { performClick() }
          verify { mockNavActions.goBack() }
          confirmVerified(mockNavActions)
        }
      }
    }
  }

  @Test
  fun testImageBanner() {
    run {
      ComposeScreen.onComposeScreen<CreateEventScreen>(composeTestRule) {
        step("test the image") { image { performClick() } }
      }
    }
  }

  @Test
  fun testImageField() {
    run {
      ComposeScreen.onComposeScreen<CreateEventScreen>(composeTestRule) {
        step("add text field") { addTextFieldButton { performClick() } }
        step("add image field") {
          addImageFieldButton { performClick() }
          inputFieldImage1 { performClick() }
        }
      }
    }
  }

  @Test
  fun testDatePicker() {
    run {
      ComposeScreen.onComposeScreen<CreateEventScreen>(composeTestRule) {
        step("choose the start time") {
          startTimePicker { performClick() }
          composeTestRule.onNodeWithText("OK").assertExists()
          composeTestRule.onNodeWithText("OK").performClick()
          composeTestRule.onNodeWithText("OK").performClick()
        }
        composeTestRule.waitForIdle()

        step("choose the end time") {
          endTimePicker { performClick() }
          composeTestRule.onNodeWithText("OK").assertExists()
          composeTestRule.onNodeWithText("OK").performClick()
          composeTestRule.onNodeWithText("OK").performClick()
        }
        composeTestRule.waitForIdle()
      }
    }
  }

  @Test
  fun testCreateAnEntireEvent() {
    run {
      ComposeScreen.onComposeScreen<CreateEventScreen>(composeTestRule) {
        step("Fill the form") {
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

        step("add text field") {
          addTextFieldButton { performClick() }
          inputFieldTitle0 {
            assertIsDisplayed()
            performClick()
            performTextInput("Test field title 1")
          }
          inputFieldDescription0 {
            assertIsDisplayed()
            performClick()
            performTextInput("Test field description 1")
          }
        }

        step("add image field") { addImageFieldButton { performClick() } }

        step("add text field") {
          addTextFieldButton { performClick() }
          inputFieldTitle2 {
            assertIsDisplayed()
            performClick()
            performTextInput("Test field title 2")
          }
          inputFieldDescription2 {
            assertIsDisplayed()
            performClick()
            performTextInput("Test field description 2")
          }
        }

        step("Create the event") { createButton { performClick() } }
      }
    }
  }
}
