package com.swent.assos

import androidx.activity.compose.setContent
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.firebase.firestore.FirebaseFirestore
import com.swent.assos.model.data.DataCache
import com.swent.assos.model.data.User
import com.swent.assos.model.localDateTimeToTimestamp
import com.swent.assos.model.view.CalendarViewModel
import com.swent.assos.screens.ReminderScreen
import com.swent.assos.ui.screens.calendar.Reminder
import dagger.hilt.android.testing.HiltAndroidTest
import io.github.kakaocup.compose.node.element.ComposeScreen
import java.time.LocalDateTime
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
class ReminderTest : SuperTest() {
  private val rocketTeamId = "QjAOBhVVcL0P2G1etPgk"

  override fun setup() {
    super.setup()
  }

  @Test
  fun reminderTest() {
    val firestore = FirebaseFirestore.getInstance()
    runBlocking {
      DataCache.currentUser.value = User(following = mutableListOf(rocketTeamId))
      firestore
          .collection("events")
          .add(
              hashMapOf(
                  "associationId" to rocketTeamId,
                  "description" to "Rocket Team meeting",
                  "endTime" to localDateTimeToTimestamp(LocalDateTime.now().plusDays(1)),
                  "location" to "INM202",
                  "startTime" to localDateTimeToTimestamp(LocalDateTime.now().plusDays(1)),
                  "title" to "Rocket Team meeting"))
          .await()
    }

    composeTestRule.activity.setContent {
      val calendarViewModel: CalendarViewModel = hiltViewModel()
      Reminder(calendarViewModel)
    }

    run {
      ComposeScreen.onComposeScreen<ReminderScreen>(composeTestRule) {
        step("Check if the reminder is displayed") { reminder { assertIsDisplayed() } }
        step("Check if the description is displayed") { description { assertIsDisplayed() } }
        step("Check if the reminder item is displayed") {
          reminderList { assertIsDisplayed() }
          item { assertIsDisplayed() }
        }
      }
    }
  }
}
