package com.swent.assos

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.swent.assos.model.data.DataCache
import com.swent.assos.ui.screens.profile.Profile
import com.swent.assos.ui.screens.ticket.MyTickets
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Test
import org.junit.runner.RunWith
import java.time.LocalDateTime

@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
class NFCTest: SuperTest() {

    override fun setup() {
        super.setup()
        DataCache.currentUser.value.associations = listOf(Triple("QjAOBhVVcL0P2G1etPgk", "TL", 2))
        Firebase.firestore.collection("events").document("4sS18EaaF6qknAFqxHX2").set(
            mapOf(
                "associationId" to "QjAOBhVVcL0P2G1etPgk",
                "description" to "Rocket Team Meeting",
                "startTime" to LocalDateTime.now(),
                "endTime" to LocalDateTime.now(),
                "location" to "INM202",
                "image" to "https://firebasestorage.googleapis.com/v0/b/the-software-enterprise-c5ea2.appspot.com/o/banners%2FERT.jpg?alt=media&token=243e3f53-c26a-4d8f-ae5a-5881f1b95313",
                "title" to "Rocket Team meeting",
            )
        )
        Firebase.firestore.collection("tickets").document("a2yHSEnKrvdWEClidKEa").set(
            mapOf(
                "eventId" to "4sS18EaaF6qknAFqxHX2",
                "id" to "a2yHSEnKrvdWEClidKEa",
                "userId" to DataCache.currentUser.value.id
            )
        )
    }

    @Test
    fun testRead() {
        composeTestRule.setContent { MyTickets(navigationActions = mockNavActions) }
    }

    @Test
    fun testWrite() {
        composeTestRule.setContent { Profile(navigationActions = mockNavActions) }

    }
}
