package com.swent.assos

import androidx.activity.compose.setContent
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.kaspersky.components.composesupport.config.withComposeSupport
import com.kaspersky.kaspresso.kaspresso.Kaspresso
import com.kaspersky.kaspresso.testcases.api.testcase.TestCase
import com.swent.assos.model.data.Association
import com.swent.assos.model.navigation.NavigationActions
import com.swent.assos.screens.AssoDigestScreen
import com.swent.assos.screens.CreateNewsScreen
import com.swent.assos.screens.NewsScreen
import com.swent.assos.ui.screens.News
import com.swent.assos.ui.screens.manageAssos.AssoDigest
import com.swent.assos.ui.screens.manageAssos.CreateNews
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import io.github.kakaocup.compose.node.element.ComposeScreen
import io.mockk.confirmVerified
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit4.MockKRule
import io.mockk.verify
import kotlin.random.Random
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

  var associationAcronym = "180°C"

  val newsHeader = "Test description -1934310868"

  @Before
  fun setup() {
    hiltRule.inject()
  }

  private val random = Random(0)
  private val randomInt = random.nextInt()
  private val newsTitle = "Test news $randomInt"
  private val newsDescription = "Test description $randomInt"

  @Test
  fun redirectToCreateNews() {
    val firestore = Firebase.firestore

    firestore
        .collection("associations")
        .whereEqualTo("acronym", associationAcronym)
        .get()
        .addOnSuccessListener {
          val association =
              Association(
                  id = it.documents[0].id,
                  acronym = it.documents[0].getString("acronym") ?: "",
                  fullname = it.documents[0].getString("fullname") ?: "",
                  url = it.documents[0].getString("url") ?: "",
                  description = it.documents[0].getString("description") ?: "")

          composeTestRule.activity.setContent {
            AssoDigest(asso = association, navigationActions = mockNavActions)
          }
        }

    run {
      ComposeScreen.onComposeScreen<AssoDigestScreen>(composeTestRule) {
        step("Open the association ${associationAcronym}") {
          createButton { performClick() }
          /*bottomSheetCreation {
            assertIsDisplayed()
          }
          createNewsButton {
            performClick()
          }*/
        }
      }
      step("Verify navigation to create news") {
        var newsID = ""
        firestore
            .collection("associations")
            .whereEqualTo("acronym", associationAcronym)
            .get()
            .addOnSuccessListener {
              newsID = it.documents[0].id

              verify { mockNavActions.navigateTo("CreateNews/${newsID}") }
              confirmVerified(mockNavActions)
            }
      }
    }
  }

  @Test
  fun createNewsAndVerifyCreation() {
    val firestore = Firebase.firestore
    firestore
        .collection("associations")
        .whereEqualTo("acronym", associationAcronym)
        .get()
        .addOnSuccessListener {
          composeTestRule.activity.setContent {
            CreateNews(navigationActions = mockNavActions, associationId = it.documents[0].id)
          }
        }

    run {
      ComposeScreen.onComposeScreen<CreateNewsScreen>(composeTestRule) {
        step("Create a news") {
          form { assertIsDisplayed() }
          inputTitle {
            assertIsDisplayed()
            performClick()
            performTextInput(newsTitle)
          }
          inputDescription {
            assertIsDisplayed()
            performClick()
            performTextInput(newsDescription)
          }
          buttonSave { performClick() }
        }
      }
    }

    composeTestRule.activity.setContent { News() }

    run {
      ComposeScreen.onComposeScreen<NewsScreen>(composeTestRule) {
        step("Check if the news is displayed") {
          newsList { assertIsDisplayed() }
          newsListItems { assertIsDisplayed() }
          itemsTitle { assertTextContains(newsTitle) }
          itemsDescription { assertTextContains(newsDescription) }
          itemsAssociation { assertTextContains("Association to promote cooking amongst students") }
        }
      }
    }
  }
}
