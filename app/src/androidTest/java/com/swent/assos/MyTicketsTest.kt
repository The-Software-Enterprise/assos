package com.swent.assos

import androidx.activity.compose.setContent
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.onNodeWithTag
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.swent.assos.model.data.DataCache
import com.swent.assos.model.data.User
import com.swent.assos.model.navigation.Destinations
import com.swent.assos.screens.MyTicketsScreen
import com.swent.assos.screens.ProfileScreen
import com.swent.assos.ui.screens.profile.Profile
import com.swent.assos.ui.screens.ticket.MyTickets
import dagger.hilt.android.testing.HiltAndroidTest
import io.github.kakaocup.compose.node.element.ComposeScreen
import io.mockk.confirmVerified
import io.mockk.verify
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
class MyTicketsTest : SuperTest() {

    private val profileId = "dxpZJlPsqzWAmBI47qtx3jvGMHX2"
    private val firstName = "Antoine"
    private val lastName = "Marchand"

    private val eventId = "4sS18EaaF6qknAFqxHX2"

    override fun setup() {
        DataCache.currentUser.value =
            User(
                id = profileId,
                firstName = firstName,
                lastName = lastName,
                email = "antoine.marchand@epfl.ch",
                associations = listOf(Triple("QjAOBhVVcL0P2G1etPgk", "Chef de projet", 1)),
                sciper = "330249",
                semester = "GM-BA6",
            tickets = listOf("aY826AKyHh6DOjbsI1Vi"))
        super.setup()
        composeTestRule.activity.setContent { MyTickets(navigationActions = mockNavActions) }
    }

    @Test
    fun myTicketsDisplaysTheCorrectPageTitle() {
        run {
            ComposeScreen.onComposeScreen<MyTicketsScreen>(composeTestRule) {
                step("Check if page title is displayed") {
                    pageTitle {
                        assertIsDisplayed()
                        assert(hasText("My tickets", substring = true, ignoreCase = true))
                    }
                }
            }
        }
    }

    @Test
    fun clickOnMyTicketRedirectsToTicketDetails() {
        run {
            ComposeScreen.onComposeScreen<MyTicketsScreen>(composeTestRule) {
                step("Check if my ticket is displayed and perform click") {
                    ticketItem {
                        assertIsDisplayed()
                        //assert(hasText("Rocket Team", substring = true, ignoreCase = true))
                        performClick()
                    }
                }
            }
            verify { mockNavActions.navigateTo(Destinations.TICKET_DETAILS.route + "/${eventId}") }
            confirmVerified(mockNavActions)
        }
    }

    @Test
    fun clickOnFloatingActionButtonRedirectsToScanTicket() {
        run {
            ComposeScreen.onComposeScreen<MyTicketsScreen>(composeTestRule) {
                step("Check if floating action button is displayed and perform click") {
                    addImages {
                        assertIsDisplayed()
                        performClick()
                    }
                }
            }
            verify { mockNavActions.navigateTo(Destinations.SCAN_TICKET.route) }
            confirmVerified(mockNavActions)
        }
    }

}
