package com.swent.assos

import androidx.activity.compose.setContent
import androidx.compose.ui.test.hasText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.swent.assos.screens.CreateEventScreen
import com.swent.assos.ui.screens.manageAsso.CreateEvent
import dagger.hilt.android.testing.HiltAndroidTest
import io.github.kakaocup.compose.node.element.ComposeScreen
import io.mockk.confirmVerified
import io.mockk.verify
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
          pagetile {
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
}
