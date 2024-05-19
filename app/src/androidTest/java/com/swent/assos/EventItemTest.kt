package com.swent.assos

import androidx.activity.compose.setContent
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.swent.assos.model.data.Association
import com.swent.assos.model.data.Event
import com.swent.assos.model.navigation.Destinations
import com.swent.assos.screens.EventItemScreen
import com.swent.assos.ui.components.EventItem
import dagger.hilt.android.testing.HiltAndroidTest
import io.github.kakaocup.compose.node.element.ComposeScreen
import io.mockk.confirmVerified
import io.mockk.verify
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
class EventItemTest : SuperTest() {

  private val asso = Association()

  private val event = Event(id = "eventId", title = "title", description = "description")

  override fun setup() {
    super.setup()
    composeTestRule.activity.setContent { EventItem(event, mockNavActions, asso) }
  }

  @Test
  fun testEventItem() {
    run {
      ComposeScreen.onComposeScreen<EventItemScreen>(composeTestRule) {
        step("Check if the event item is displayed") {
          composeTestRule.onNodeWithTag("EventItemImage", true).assertIsDisplayed()
          composeTestRule.onNodeWithText("title").assertIsDisplayed()
          composeTestRule.onNodeWithText("description").assertIsDisplayed()
          eventItem {
            assertIsDisplayed()
            performClick()
          }
          verify {
            mockNavActions.navigateTo(
                Destinations.EVENT_DETAILS.route + "/${event.id}" + "/${asso.id}")
          }
          confirmVerified(mockNavActions)
        }
      }
    }
  }
}
