package com.swent.assos

import androidx.activity.compose.setContent
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.firebase.firestore.FirebaseFirestore
import com.swent.assos.model.data.OpenPositions
import com.swent.assos.model.serialize
import com.swent.assos.screens.PosDetailsScreen
import com.swent.assos.ui.screens.manageAsso.createEvent.PosDetails
import dagger.hilt.android.testing.HiltAndroidTest
import io.github.kakaocup.compose.node.element.ComposeScreen
import io.mockk.confirmVerified
import io.mockk.verify
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
class PosDetailsTest : SuperTest() {

  val assoId = "jMWo6NgngIS2hCq054TF"
  val posId = "IamAPos"

  val pos =
      OpenPositions(
          id = posId,
          title = "Gordon Ramsay",
          description = "Help me cook some haggis",
          requirements = listOf("Yelling", "Haggis"),
          responsibilities = listOf("something about cooking", "represent scotland"),
      )

  override fun setup() {
    super.setup()
    FirebaseFirestore.getInstance().document("associations/$assoId/$posId").set(serialize(pos))
    composeTestRule.activity.setContent {
      PosDetails(assoId = assoId, posId = "posId", navigationActions = mockNavActions)
    }
  }

  @Test
  fun goBackButtonNavigatesToManageAsso() {
    run {
      ComposeScreen.onComposeScreen<PosDetailsScreen>(composeTestRule) {
        step("Go back") {
          composeTestRule.onNodeWithTag("GoBackButton").performClick()
          verify { mockNavActions.goBack() }
          confirmVerified(mockNavActions)
        }
      }
    }
  }

  @Test
  fun testPosDetails() {
    run {
      ComposeScreen.onComposeScreen<PosDetailsScreen>(composeTestRule) {
        step("Check if the position details are displayed") {
          composeTestRule.onNodeWithText("Gordon Ramsay").assertIsDisplayed()
          composeTestRule.onNodeWithText("Help me cook some haggis").assertIsDisplayed()
          composeTestRule.onNodeWithText("Yelling").assertIsDisplayed()
          composeTestRule.onNodeWithText("Haggis").assertIsDisplayed()
          composeTestRule.onNodeWithText("something about cooking").assertIsDisplayed()
          composeTestRule.onNodeWithText("represent scotland").assertIsDisplayed()
        }
      }
    }
  }
}
