package com.swent.assos

import android.net.Uri
import androidx.activity.compose.setContent
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.firebase.firestore.FirebaseFirestore
import com.swent.assos.model.AssociationPosition
import com.swent.assos.model.data.Association
import com.swent.assos.model.data.DataCache
import com.swent.assos.model.data.Event
import com.swent.assos.model.data.User
import com.swent.assos.model.navigation.Destinations
import com.swent.assos.model.view.AssoViewModel
import com.swent.assos.screens.ApplicationManagementScreen
import com.swent.assos.screens.AssoDetailsScreen
import com.swent.assos.screens.CreateNewsScreen
import com.swent.assos.screens.EventDetailsScreen
import com.swent.assos.screens.LoginScreen
import com.swent.assos.screens.ManageAssoScreen
import com.swent.assos.screens.MyAssociationsScreen
import com.swent.assos.screens.NewsScreen
import com.swent.assos.screens.SignupScreen
import com.swent.assos.screens.StaffManagementScreen
import com.swent.assos.ui.login.SignUpScreen
import com.swent.assos.ui.screens.News
import com.swent.assos.ui.screens.assoDetails.AssoDetails
import com.swent.assos.ui.screens.assoDetails.EventDetails
import com.swent.assos.ui.screens.manageAsso.ApplicationManagement
import com.swent.assos.ui.screens.manageAsso.CreateNews
import com.swent.assos.ui.screens.manageAsso.ManageAssociation
import com.swent.assos.ui.screens.manageAsso.StaffManagement
import com.swent.assos.ui.screens.profile.Following
import com.swent.assos.ui.screens.profile.MyAssociations
import dagger.hilt.android.testing.HiltAndroidTest
import io.github.kakaocup.compose.node.element.ComposeScreen
import io.mockk.confirmVerified
import io.mockk.verify
import org.junit.Test
import org.junit.runner.RunWith
import java.time.LocalDateTime
import kotlin.random.Random

@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
class StaffManagementTest : SuperTest() {
    private val assoID = "02s16UZba2Bsx5opTcQb"

    private val event1 = Event("ABCDEF", "description", assoID, Uri.EMPTY, "assoId")

    private val profileId = "dxpZJlPsqzWAmBI47qtx3jvGMHX2"
    private val firstName = "Antoine"
    private val lastName = "Marchand"

    private val memberAssociation = Association("02s16UZba2Bsx5opTcQb")

    val user1 =
        User(
            id = profileId,
            firstName = firstName,
            lastName = lastName,
            email = "antoine.marchand@epfl.ch",
            associations = emptyList(),
            sciper = "330249",
            semester = "GM-BA6")

    val user2 =
        User(
            id = profileId,
            firstName = firstName,
            lastName = lastName,
            email = "anna.yildiran@epfl.ch",
            associations = listOf(Triple(memberAssociation.id, "Chef de projet", 1)),
            sciper = "330248",
            semester = "SV-MA2")


    override fun setup() {

        DataCache.currentUser.value = user1

        FirebaseFirestore.getInstance().collection("events").add(event1)
    }

    @Test
    fun testStaffList() {

        composeTestRule.activity.setContent {
            EventDetails(eventId = event1.id, assoId = assoID, navigationActions = mockNavActions)
        }

        run {
            ComposeScreen.onComposeScreen<EventDetailsScreen>(composeTestRule) {
                step("I want to join") { composeTestRule.onNodeWithText("Become Staff").performClick() }
            }
            step("I changed my mind") { composeTestRule.onNodeWithText("No").performClick() }

            step("I want to join again") { composeTestRule.onNodeWithText("Become Staff").performClick() }
            step("Confirm") { composeTestRule.onNodeWithText("Yes").performClick() }
        }

        DataCache.currentUser.value = user2

        composeTestRule.activity.setContent {
            EventDetails(eventId = event1.id, assoId = assoID, navigationActions = mockNavActions)
        }

        run {
            step("Check staff list"){
                composeTestRule.onNodeWithText("Staff List").performClick()
            }
        }


        }

        /*composeTestRule.activity.setContent {
            StaffManagement(navigationActions = mockNavActions, eventId = event1.id)
        }

        run {
            ComposeScreen.onComposeScreen<StaffManagementScreen>(composeTestRule){

                step("Check if the staff list is displayed") {

                    staffList { assertIsDisplayed()}

                    staffItemName {
                        assertIsDisplayed()
                        assertTextContains(value = "Marchand", substring = true, ignoreCase = true)
                    }

                    staffItemAcceptButton {
                        assertIsDisplayed()
                        performClick()
                    }

                    FirebaseFirestore.getInstance().collection("events").document(event1.id)
                        .collection("applicants").get().result.toList()
                        .find { it["userId"] == user.id}?.let { it1 -> assert(it1.exists()) }
                }
            }
        }*/






    /*@Test
    fun ApplyAndVerifyApplicationDisplay() {
        run {
            ComposeScreen.onComposeScreen<LoginScreen>(composeTestRule) {
                step("Click on signup") {
                    signUpButton {
                        assertIsDisplayed()
                        performClick()
                    }
                }
                verify { mockNavActions.navigateTo(Destinations.SIGN_UP) }
                confirmVerified(mockNavActions)
            }
        }
        composeTestRule.activity.setContent { SignUpScreen(navigationActions = mockNavActions) }
        run {
            ComposeScreen.onComposeScreen<SignupScreen>(composeTestRule) {
                step("Fill the Sign Up form") {
                    emailField {
                        assertIsDisplayed()
                        performTextInput("anna.yildiran@epfl.ch")
                    }
                    passwordField {
                        assertIsDisplayed()
                        performTextInput("123456")
                    }
                    confirmPasswordField {
                        assertIsDisplayed()
                        performTextInput("123456")
                    }
                }
                step("Click on signup") {
                    signUpButton {
                        assertIsDisplayed()
                        performClick()
                    }

                    Thread.sleep(2000)
                    // check if we are on the Home screen
                    verify { mockNavActions.navigateTo(Destinations.HOME) }
                    confirmVerified(mockNavActions)
                }
            }
        }

        FirebaseFirestore.getInstance().collection("events").add(event1)


        composeTestRule.activity.setContent {
            EventDetails(eventId = "123456789", assoId = assoId, navigationActions = mockNavActions)
        }

        run {
            ComposeScreen.onComposeScreen<EventDetailsScreen>(composeTestRule) {
                step("Apply to staff for an event") {
                    eventStaffListButton {
                        assertIsDisplayed()
                        performClick()
                    }
                }
            }
        }



        composeTestRule.activity.setContent {
            ManageAssociation(navigationActions = mockNavActions, assoId = "9hciFZwTKU9rWg4r0B2A")}

        run {
            ComposeScreen.onComposeScreen<ManageAssoScreen>(composeTestRule) {
                step("Go to the list of applications") {
                    applicationsButton {
                        assertIsDisplayed()
                        performClick()
                    }

                    verify { mockNavActions.navigateTo(Destinations.APPLICATION_MANAGEMENT) }
                    confirmVerified(mockNavActions)
                }
            }
        }

        composeTestRule.activity.setContent {
            ApplicationManagement(assoId = "9hciFZwTKU9rWg4r0B2A", navigationActions = mockNavActions)
        }

        run{
            ComposeScreen.onComposeScreen<ApplicationManagementScreen>(composeTestRule) {
                step("Check if the application is displayed") {
                    applicationList {
                        assertIsDisplayed()
                    }
                    applicationListItem {
                        assertIsDisplayed()
                        assertTextContains(value ="Yildiran", substring = true, ignoreCase = true)
                    }

                    acceptButton {
                        assertIsDisplayed()
                        performClick()
                    }
                }
            }
        }

        composeTestRule.activity.setContent {
            MyAssociations( navigationActions = mockNavActions)
        }

        run {
            ComposeScreen.onComposeScreen<MyAssociationsScreen>(composeTestRule) {
                step("Check if the association is displayed") {
                    associationCard {
                        assertIsDisplayed()
                        performClick()
                    }
                }
            }
        }
    }*/


}
