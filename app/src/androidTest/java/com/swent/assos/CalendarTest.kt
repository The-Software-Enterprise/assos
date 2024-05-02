package com.swent.assos

import androidx.activity.compose.setContent
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.swent.assos.screens.CalendarScreen
import com.swent.assos.ui.screens.calendar.Calendar
import dagger.hilt.android.testing.HiltAndroidTest
import io.github.kakaocup.compose.node.element.ComposeScreen
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import org.junit.Test
import org.junit.runner.RunWith

private val titleDateFormatter = DateTimeFormatter.ofPattern("dd LLL uuuu")

@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
class CalendarTest : SuperTest() {

  override fun setup() {
    super.setup()
    composeTestRule.activity.setContent { Calendar() }
  }

  @Test
  fun assertEventIsDisplayed() {
    run {
      ComposeScreen.onComposeScreen<CalendarScreen>(composeTestRule) {
        step("Title displays today's date") {
          topBarTitle {
            assertIsDisplayed()
            assertTextContains(
                LocalDate.now().format(titleDateFormatter), ignoreCase = true, substring = true)
          }
          titleItemSelected {
            assertIsDisplayed()
            assertTextContains(
                LocalDate.now().dayOfMonth.toString(), ignoreCase = true, substring = true)
          }
        }
      }
    }
  }
}
