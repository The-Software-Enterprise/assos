package com.swent.assos

import androidx.activity.compose.setContent
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.kaspersky.components.composesupport.config.withComposeSupport
import com.kaspersky.kaspresso.kaspresso.Kaspresso
import com.kaspersky.kaspresso.testcases.api.testcase.TestCase
import com.swent.assos.model.navigation.NavigationActions
import com.swent.assos.screens.NewsScreen
import com.swent.assos.ui.screens.News
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import io.github.kakaocup.compose.node.element.ComposeScreen
import io.mockk.confirmVerified
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit4.MockKRule
import io.mockk.verify
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
class NewsTest : TestCase(kaspressoBuilder = Kaspresso.Builder.withComposeSupport()) {

    @get:Rule(order = 1) var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 2) var composeTestRule = createAndroidComposeRule<MainActivity>()

    // This rule automatic initializes lateinit properties with @MockK, @RelaxedMockK, etc.
    @get:Rule(order = 3) val mockkRule = MockKRule(this)

    // Relaxed mocks methods have a default implementation returning values
    @RelaxedMockK lateinit var mockNavActions: NavigationActions

    @Before
    fun setup() {
        hiltRule.inject()
        composeTestRule.activity.setContent { News() }
    }

    @Test
    fun areNewsDisplayed() = run {
        ComposeScreen.onComposeScreen<NewsScreen>(composeTestRule) {
            step("Check if the news are displayed") {
                newsList {
                    assertIsDisplayed()
                }
                newsListItems {
                    assertIsDisplayed()
                }
                itemsTitle {
                    assertTextContains("distribution de crêpes")
                }
                itemsDescription {
                    assertTextContains("distribution de crêpes par la JE")
                }
                itemsDate {
                    assertTextContains("Thu Apr 11 17:00:14 GMT+02:00 2024")
                }
                itemsAssociation {
                    assertTextContains("Junior Entreprise")
                }
            }
        }
    }

    @Test
    fun titleHasTheRightContent() = run {
        ComposeScreen.onComposeScreen<NewsScreen>(composeTestRule) {
            appTitle1 { assertTextContains("Student") }
            appTitle2 { assertTextContains("Sphere") }
        }
    }
}
