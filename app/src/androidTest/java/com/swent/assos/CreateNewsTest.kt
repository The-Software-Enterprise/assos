package com.swent.assos

import androidx.activity.compose.setContent
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.base.Verify.verify
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.swent.assos.screens.CreateNewsScreen
import com.swent.assos.ui.screens.manageAsso.CreateNews
import dagger.hilt.android.testing.HiltAndroidTest
import io.github.kakaocup.compose.node.element.ComposeScreen
import io.mockk.confirmVerified
import io.mockk.verify
import kotlin.random.Random
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
class CreateNewsTest : SuperTest() {
  private val assoId = "jMWo6NgngIS2hCq054TF"
  private val randomInt = Random.nextInt()
  private val newsTitle = "Test news $randomInt"
  private val newsDescription = "Test description $randomInt"

  override fun setup() {
    super.setup()
    composeTestRule.activity.setContent {
      CreateNews(assoId = assoId, navigationActions = mockNavActions)
    }
  }

  @Test
  fun createNews() {
    run {
      ComposeScreen.onComposeScreen<CreateNewsScreen>(composeTestRule) {
        step("Create News") {
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
    runBlocking {
      delay(2000)
      val newsId =
          Firebase.firestore
              .collection("news")
              .whereEqualTo("title", newsTitle)
              .whereEqualTo("description", newsDescription)
              .get()
              .await()
              .documents[0]
              .id
      assert(newsId.isNotEmpty())
      Firebase.firestore.collection("news").document(newsId).delete().await()
      Firebase.auth.signOut()
    }
  }

  @Test
  fun goBack() {
    run {
      ComposeScreen.onComposeScreen<CreateNewsScreen>(composeTestRule) {
        step("Go back") {
          goBackButton { performClick() }
          verify { mockNavActions.goBack() }
          confirmVerified(mockNavActions)
        }
      }
    }
  }
}
