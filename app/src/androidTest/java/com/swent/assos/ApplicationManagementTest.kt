package com.swent.assos

import android.net.Uri
import androidx.activity.compose.setContent
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.isDisplayed
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.swent.assos.model.data.DataCache
import com.swent.assos.model.data.Event
import com.swent.assos.model.data.User
import com.swent.assos.model.service.impl.serialize
import com.swent.assos.screens.ApplicationManagementScreen
import com.swent.assos.screens.AssoDetailsScreen
import com.swent.assos.screens.EventDetailsScreen
import com.swent.assos.screens.ManageAssoScreen
import com.swent.assos.screens.StaffManagementScreen
import com.swent.assos.ui.screens.assoDetails.AssoDetails
import com.swent.assos.ui.screens.assoDetails.EventDetails
import com.swent.assos.ui.screens.manageAsso.ApplicationManagement
import com.swent.assos.ui.screens.manageAsso.ManageAssociation
import com.swent.assos.ui.screens.manageAsso.StaffManagement
import dagger.hilt.android.testing.HiltAndroidTest
import io.github.kakaocup.compose.node.element.ComposeScreen
import org.junit.Test
import org.junit.runner.RunWith
@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
class ApplicationManagementTest : SuperTest() {

    private val assoID = "02s16UZba2Bsx5opTcQb"

    val user1 =
        User(
            id = "11111",
            firstName = "Paul",
            lastName = "Levebre",
            email = "paul.levebre@epfl.ch",
            associations = emptyList(),
            sciper = "330245",
            semester = "GM-BA2")

    val user2 =
        User(
            id = "33333",
            firstName = "Henri",
            lastName = "Duflot",
            email = "henri.duflot@epfl.ch",
            associations = listOf(Triple(assoID, "Director", 1)),
        )

    override fun setup() {

        super.setup()

        FirebaseFirestore.getInstance().collection("users").document(user2.id).set(user2)
        FirebaseFirestore.getInstance()
            .collection("users")
            .document(user2.id)
            .update(
                "associations",
                FieldValue.arrayUnion(
                    mapOf("assoId" to assoID, "position" to "Director", "rank" to 1)))

        FirebaseFirestore.getInstance()
            .collection("associations")
            .document(assoID)
            .collection("applicants")
            .add(mapOf("userId" to user1.id, "status" to "pending", "createdAt" to Timestamp.now()))

        DataCache.currentUser.value = user2
    }

    @Test
    fun testApplicationList() {

        composeTestRule.activity.setContent {
            ManageAssociation(assoId = assoID, navigationActions = mockNavActions)
        }

        run {
            ComposeScreen.onComposeScreen<ManageAssoScreen>(composeTestRule) {
                step("Check applicant list") {
                    composeTestRule.waitUntil(
                        condition = { composeTestRule.onNodeWithText("Applications").isDisplayed() },
                        timeoutMillis = 10000)
                    composeTestRule.onNodeWithText("Applications").performClick()
                }
            }
        }

        composeTestRule.activity.setContent() {
            ApplicationManagement(navigationActions = mockNavActions, assoId = assoID)
        }

        run {
            ComposeScreen.onComposeScreen<ApplicationManagementScreen>(composeTestRule) {
                step("Check if the applications list is displayed") {
                    applicationList { assertIsDisplayed() }

                    applicationListItem { assertIsDisplayed() }
                }
                step("Check the accept button") { composeTestRule.onNodeWithText("Accept").performClick() }

                step("Check if the staff is accepted") {
                    composeTestRule.onNodeWithText("Un-Accept").assertIsDisplayed()
                    composeTestRule.onNodeWithText("Un-Accept").performClick()
                }
            }
        }
    }
}
