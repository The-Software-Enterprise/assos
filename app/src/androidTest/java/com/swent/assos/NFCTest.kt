package com.swent.assos

import android.content.Intent
import android.nfc.NdefMessage
import android.nfc.NdefRecord
import android.nfc.NfcAdapter
import android.nfc.Tag
import androidx.activity.compose.setContent
import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ActivityScenario
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.swent.assos.model.data.DataCache
import com.swent.assos.model.data.ParticipationStatus
import com.swent.assos.model.data.User
import com.swent.assos.model.localDateTimeToTimestamp
import com.swent.assos.screens.EventDetailsScreen
import com.swent.assos.screens.MyTicketsScreen
import com.swent.assos.ui.screens.assoDetails.EventDetails
import com.swent.assos.ui.screens.ticket.MyTickets
import dagger.hilt.android.testing.HiltAndroidTest
import io.github.kakaocup.compose.node.element.ComposeScreen
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import java.time.LocalDateTime
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
class NFCTest : SuperTest() {

  private val rocketTeamId = "QjAOBhVVcL0P2G1etPgk"
  private val ticketId = "a2yHSEnKrvdWEClidKEa"
  private val userId = "userIdTest"
  private val eventId = "4sS18EaaF6qknAFqxHX2"

  private val mockTag = mockk<Tag>()
  private val mockIntent = mockk<Intent>()
  private val mockMessage = mockk<NdefMessage>()
  private val readPayload: String = "Hello !"

  override fun setup() {
    super.setup()
    DataCache.currentUser.value =
        User(
            userId,
            "Jules",
            "Herrscher",
            "jules.herrscher@test.ch",
            listOf(Triple(rocketTeamId, "TL", 2)),
            listOf(rocketTeamId),
            "123456",
            "IN-BA6",
            listOf(ticketId))
    Firebase.firestore
        .collection("users")
        .document(userId)
        .set(
            mapOf(
                "associations" to listOf(Triple(rocketTeamId, "TL", 2)),
                "id" to userId,
                "email" to "jules.herrscher@test.ch",
                "firstName" to "Jules",
                "lastName" to "Herrscher",
                "following" to listOf(rocketTeamId),
                "sciper" to "123456",
                "semester" to "IN-BA6",
                "tickets" to listOf(ticketId),
            ))
    /*Firebase.firestore.collection("users").document("userIdTest").collection("tickets").document("a2yHSEnKrvdWEClidKEa").set(
        mapOf(
            "eventId" to "4sS18EaaF6qknAFqxHX2",
            "id" to "a2yHSEnKrvdWEClidKEa",
            "userId" to "userIdTest"
        )
    )*/

    Firebase.firestore
        .collection("events")
        .document(eventId)
        .set(
            mapOf(
                "associationId" to rocketTeamId,
                "description" to "Rocket Team Meeting",
                "startTime" to localDateTimeToTimestamp(LocalDateTime.now()),
                "endTime" to localDateTimeToTimestamp(LocalDateTime.now()),
                "location" to "INM202",
                "image" to
                    "https://firebasestorage.googleapis.com/v0/b/the-software-enterprise-c5ea2.appspot.com/o/banners%2FERT.jpg?alt=media&token=243e3f53-c26a-4d8f-ae5a-5881f1b95313",
                "title" to "Rocket Team meeting",
            ))

    Firebase.firestore
        .collection("tickets")
        .document(ticketId)
        .set(mapOf("eventId" to eventId, "id" to ticketId, "userId" to userId))
    val ticket =
        Firebase.firestore
            .collection("tickets")
            .document(ticketId)
            .set(
                mapOf(
                    "userId" to userId,
                    "eventId" to eventId,
                    "participantStatus" to ParticipationStatus.Participant.name))
    // add ticket id to user collection tickets
    Firebase.firestore
        .collection("users/$userId/tickets")
        .document(ticketId)
        .set(mapOf("ticketId" to ticketId))

    mockkStatic(NfcAdapter::class)
    val mockNfcAdapter = mockk<NfcAdapter>()
    every { NfcAdapter.getDefaultAdapter(any()) } returns mockNfcAdapter
    every { mockNfcAdapter.isEnabled } returns true
    every { mockNfcAdapter.enableForegroundDispatch(any(), any(), any(), any()) } returns Unit
    every { mockNfcAdapter.disableForegroundDispatch(any()) } returns Unit

    every { mockTag.id } returns byteArrayOf(0x01, 0x02, 0x03, 0x04)
    every { mockIntent.action } returns NfcAdapter.ACTION_TAG_DISCOVERED
    every { mockIntent.hasExtra(any()) } returns false
    every { mockIntent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES) } returns
        arrayOf(mockMessage)

    val pathPrefix = "swent.com:nfcapp"
    val nfcRecord =
        NdefRecord(
            NdefRecord.TNF_EXTERNAL_TYPE,
            pathPrefix.toByteArray(),
            ByteArray(0),
            readPayload.toByteArray())
    every { mockMessage.records } returns arrayOf(nfcRecord)
  }

  @Test
  fun testRead() {
    composeTestRule.activity.setContent { MyTickets(navigationActions = mockNavActions) }
    run {
      step("Check if the ticket is displayed") {
        ComposeScreen.onComposeScreen<MyTicketsScreen>(composeTestRule) {
          ticketItem { assertIsDisplayed() }
        }
      }
    }
  }

  @Test
  fun testReaderActivity() {
    val intent =
        Intent(InstrumentationRegistry.getInstrumentation().targetContext, NFCReader::class.java)
            .apply { putExtra("ticketId", ticketId) }
    ActivityScenario.launch<NFCReader>(intent).use { scenario ->
      scenario.onActivity { activity ->
        // Check if the activity is displayed
        assert(activity.lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED))
      }
    }
  }

  @Test
  fun testWriterActivity() {
    val intent =
        Intent(InstrumentationRegistry.getInstrumentation().targetContext, NFCWriter::class.java)
            .apply { putExtra("eventID", eventId) }
    ActivityScenario.launch<NFCWriter>(intent).use { scenario ->
      scenario.onActivity { activity ->
        // Check if the activity is displayed
        assert(activity.lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED))
      }
    }
  }

  @Test
  fun testWrite() {
    composeTestRule.activity.setContent { EventDetails(eventId, mockNavActions, rocketTeamId) }
    run {
      step("assert my associations display correctly") {
        ComposeScreen.onComposeScreen<EventDetailsScreen>(composeTestRule) {
          setupNFCTag { assertIsDisplayed() }
        }
      }
    }
  }
}
