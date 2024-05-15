package com.swent.assos

import android.net.Uri
import androidx.activity.compose.setContent
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.swent.assos.model.data.Event
import com.swent.assos.model.generateUniqueID
import com.swent.assos.model.service.impl.AuthServiceImpl
import com.swent.assos.model.service.impl.DbServiceImpl
import com.swent.assos.model.service.impl.StorageServiceImpl
import com.swent.assos.model.service.impl.deserializeEvent
import com.swent.assos.model.service.impl.serialize
import com.swent.assos.model.view.EventViewModel
import com.swent.assos.screens.CreateEventScreen
import com.swent.assos.ui.screens.manageAsso.createEvent.CreateEvent
import dagger.hilt.android.testing.HiltAndroidTest
import io.github.kakaocup.compose.node.element.ComposeScreen
import io.mockk.confirmVerified
import io.mockk.verify
import java.time.LocalDateTime
import java.util.Calendar
import kotlin.random.Random
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
class CreateEventTest : SuperTest() {
  private val assoId = "jMWo6NgngIS2hCq054TF"
  private val randomInt = Random.nextInt()
  private val eventTitle = "Test event $randomInt"
  private val eventDescription = "Test description $randomInt"
  private val fieldTitle1 = "Test field title 1"
  private val fieldDescription1 = "Test field description 1"
  private val fieldTitle2 = "Test field title 2"
  private val fieldDescription2 = "Test field description 2"

  private lateinit var eventViewModel: EventViewModel

  override fun setup() {
    super.setup()
    eventViewModel =
        EventViewModel(
            DbServiceImpl(FirebaseFirestore.getInstance(), FirebaseAuth.getInstance()),
            StorageServiceImpl(FirebaseStorage.getInstance()),
            AuthServiceImpl(FirebaseAuth.getInstance()),
            Dispatchers.IO)
    composeTestRule.activity.setContent {
      CreateEvent(assoId = assoId, navigationActions = mockNavActions, eventViewModel)
    }
  }

  @Test
  fun createEventCancel() {
    eventViewModel.clear()
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
    eventViewModel.clear()
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
    eventViewModel.clear()
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
            performTextInput(fieldTitle1)
          }
          inputFieldDescription0 {
            assertIsDisplayed()
            performClick()
            performTextInput(fieldDescription1)
          }
        }

        step("add image field") { addImageFieldButton { performClick() } }

        step("add text field") {
          addTextFieldButton { performClick() }
          inputFieldTitle2 {
            assertIsDisplayed()
            performClick()
            performTextInput(fieldTitle2)
          }
          inputFieldDescription2 {
            assertIsDisplayed()
            performClick()
            performTextInput(fieldDescription2)
          }
        }

        step("Create the event") { createButton { performClick() } }
      }
    }
  }

  @Test
  fun testBadDates() {
    eventViewModel.clear()
    run {
      ComposeScreen.onComposeScreen<CreateEventScreen>(composeTestRule) {
        step("choose the start date before today") {
          eventViewModel.event.value.startTime = LocalDateTime.now().minusDays(1)
          startTimePicker { performClick() }
          composeTestRule.onNodeWithText("OK").assertExists()
          composeTestRule.onNodeWithText("OK").performClick()
          composeTestRule.onNodeWithText("OK").performClick()
        }

        composeTestRule.waitForIdle()

        step("choose the start time before now") {
          startTimePicker { performClick() }
          composeTestRule.onNodeWithText("OK").assertExists()
          composeTestRule.onNodeWithText("OK").performClick()
          eventViewModel.event.value.startTime = LocalDateTime.now().minusHours(1)
          composeTestRule.onNodeWithText("OK").performClick()
        }

        composeTestRule.waitForIdle()

        step("choose the end date before start date") {
          eventViewModel.event.value.startTime = LocalDateTime.now().plusDays(1)
          eventViewModel.event.value.endTime = LocalDateTime.now().minusDays(1)
          endTimePicker { performClick() }
          composeTestRule.onNodeWithText("OK").assertExists()
          composeTestRule.onNodeWithText("OK").performClick()
          composeTestRule.onNodeWithText("OK").performClick()
        }

        composeTestRule.waitForIdle()

        step("choose the end time before start time") {
          endTimePicker { performClick() }
          composeTestRule.onNodeWithText("OK").assertExists()
          composeTestRule.onNodeWithText("OK").performClick()
          eventViewModel.event.value.endTime = LocalDateTime.now().minusHours(1)
          composeTestRule.onNodeWithText("OK").performClick()
        }
      }
    }
  }

  @Test
  fun testSerializeDeserializeEvent() = runBlocking {
    val event =
        Event(
            id = generateUniqueID(),
            title = "title",
            description = "description",
            image = Uri.parse("https://www.google.com"),
            fields = listOf())
    val serialized = serialize(event)
    FirebaseFirestore.getInstance().collection("events").document(event.id).set(serialized).await()
    val documentSnapshot =
        FirebaseFirestore.getInstance().collection("events").document(event.id).get().await()
    val deserialized = deserializeEvent(documentSnapshot)
    deserialized.documentSnapshot = null
  }
}
