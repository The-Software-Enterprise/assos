package com.swent.assos

import android.net.Uri
import androidx.activity.compose.setContent
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.firebase.firestore.FirebaseFirestore
import com.swent.assos.model.data.Event
import com.swent.assos.screens.EventDetailsScreen
import com.swent.assos.ui.screens.assoDetails.EventDetails
import dagger.hilt.android.testing.HiltAndroidTest
import io.github.kakaocup.compose.node.element.ComposeScreen
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
class EventDetailsTest : SuperTest() {
  val assoID = "02s16UZba2Bsx5opTcQb"
  val event = Event("123456", "description", assoID, Uri.EMPTY, "assoId")

  override fun setup() {

    super.setup()
    FirebaseFirestore.getInstance().collection("events").document(event.id).set(event)

    composeTestRule.activity.setContent {
      EventDetails(eventId = event.id, assoId = assoID, navigationActions = mockNavActions)
    }
  }

  @Test
  fun testEventDetails() {
    run {
      ComposeScreen.onComposeScreen<EventDetailsScreen>(composeTestRule) {
        step("I want to join") { composeTestRule.onNodeWithText("Become Staff").performClick() }
      }
      step("I changed my mind") { composeTestRule.onNodeWithText("No").performClick() }

      step("I want to join again") { composeTestRule.onNodeWithText("Become Staff").performClick() }
      step("Confirm") { composeTestRule.onNodeWithText("Yes").performClick() }
    }
  }
}
