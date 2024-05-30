package com.swent.assos

import androidx.activity.compose.setContent
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.swent.assos.screens.CreatePositionScreen
import com.swent.assos.ui.screens.manageAsso.CreatePosition
import dagger.hilt.android.testing.HiltAndroidTest
import io.github.kakaocup.compose.node.element.ComposeScreen
import io.mockk.confirmVerified
import io.mockk.verify
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
class CreatePosTest : SuperTest() {

  private val assoId: String = "B986lqXA5lKTuG7zgs5T"

  override fun setup() {
    super.setup()
    composeTestRule.activity.setContent {
      CreatePosition(assoId = assoId, navigationActions = mockNavActions)
    }
  }

  @Test
  fun goBack() {
    run {
      ComposeScreen.onComposeScreen<CreatePositionScreen>(composeTestRule) {
        step("Go back") {
          goBackButton { performClick() }
          verify { mockNavActions.goBack() }
          confirmVerified(mockNavActions)
        }
      }
    }
  }

  @Test
  fun createPosition() {
    run {
      ComposeScreen.onComposeScreen<CreatePositionScreen>(composeTestRule) {
        step("give Title and Description") {
          composeTestRule.onNodeWithTag("titleField").performTextInput("Supreme Leader")
          composeTestRule
              .onNodeWithTag("descriptionField")
              .performTextInput("Basically someone who knows and does everything")
        }

        step("requirement") {
          composeTestRule.onNodeWithTag("dynamicField|Requirement|add").performClick()
          composeTestRule.waitForIdle()
          composeTestRule
              .onNodeWithTag("dynamicField|Requirement|0")
              .performTextInput("Can code a website in VHDL")
        }

        step("responsibility") {
          composeTestRule.onNodeWithTag("dynamicField|Responsibility|add").performClick()
          composeTestRule.waitForIdle()
          composeTestRule
              .onNodeWithTag("dynamicField|Responsibility|0")
              .performTextInput("Find p == np, (trivial)")
        }

        step("submit") {
          composeTestRule.onNodeWithText("Submit").performClick()
          verify { mockNavActions.goBack() }
          confirmVerified(mockNavActions)
        }
      }
    }
  }
}
