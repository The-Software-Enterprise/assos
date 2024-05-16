package com.swent.assos

import androidx.activity.compose.setContent
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.swent.assos.model.data.DataCache
import com.swent.assos.model.data.User
import com.swent.assos.model.localDateTimeToTimestamp
import com.swent.assos.screens.EventDetailsScreen
import com.swent.assos.screens.MyTicketsScreen
import com.swent.assos.ui.screens.assoDetails.EventDetails
import com.swent.assos.ui.screens.ticket.MyTickets
import dagger.hilt.android.testing.HiltAndroidTest
import io.github.kakaocup.compose.node.element.ComposeScreen
import org.junit.Test
import org.junit.runner.RunWith
import java.time.LocalDateTime

@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
class NFCTest: SuperTest() {

    private val rocketTeamId = "QjAOBhVVcL0P2G1etPgk"
    private val ticketId = "a2yHSEnKrvdWEClidKEa"
    private val userId = "userIdTest"
    private val eventId = "4sS18EaaF6qknAFqxHX2"
    override fun setup() {
        super.setup()
        DataCache.currentUser.value = User(
            userId,
            "Jules",
            "Herrscher",
            "jules.herrscher@test.ch",
            listOf(Triple(rocketTeamId, "TL", 2)),
            listOf(rocketTeamId),
            "123456",
            "IN-BA6",
            listOf(ticketId)
        )
        Firebase.firestore.collection("users").document(userId).set(
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
            )
        )
        /*Firebase.firestore.collection("users").document("userIdTest").collection("tickets").document("a2yHSEnKrvdWEClidKEa").set(
            mapOf(
                "eventId" to "4sS18EaaF6qknAFqxHX2",
                "id" to "a2yHSEnKrvdWEClidKEa",
                "userId" to "userIdTest"
            )
        )*/


        Firebase.firestore.collection("events").document(eventId).set(
            mapOf(
                "associationId" to rocketTeamId,
                "description" to "Rocket Team Meeting",
                "startTime" to localDateTimeToTimestamp(
                    LocalDateTime.now()),
                "endTime" to localDateTimeToTimestamp(
                    LocalDateTime.now()),
                "location" to "INM202",
                "image" to "https://firebasestorage.googleapis.com/v0/b/the-software-enterprise-c5ea2.appspot.com/o/banners%2FERT.jpg?alt=media&token=243e3f53-c26a-4d8f-ae5a-5881f1b95313",
                "title" to "Rocket Team meeting",
            )
        )

        Firebase.firestore.collection("tickets").document(ticketId).set(
            mapOf(
                "eventId" to eventId,
                "id" to ticketId,
                "userId" to userId
            )
        )

    }

    @Test
    fun testRead() {
        composeTestRule.activity.setContent { MyTickets(navigationActions = mockNavActions) }
        run {
            step("Check if the ticket is displayed") {
                ComposeScreen.onComposeScreen<MyTicketsScreen>(composeTestRule) {
                    ticketList { assertIsDisplayed() }
                    ticketItem { performClick() }
                }
            }
        }
    }

    @Test
    fun testWrite() {
        composeTestRule.activity.setContent { EventDetails(eventId, mockNavActions, rocketTeamId) }
        run {
            step("assert ny associations display correctly") {
                ComposeScreen.onComposeScreen<EventDetailsScreen>(composeTestRule) {
                    eventDetails {
                        assertIsDisplayed()
                    }
                    setupNFCTag {
                        assertIsDisplayed()
                    }
                }

            }
        }
    }
}
