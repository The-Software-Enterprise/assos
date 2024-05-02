package com.swent.assos

import androidx.activity.compose.setContent
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.swent.assos.model.data.DataCache
import com.swent.assos.screens.AssoDetailsScreen
import com.swent.assos.ui.screens.assoDetails.AssoDetails
import dagger.hilt.android.testing.HiltAndroidTest
import io.github.kakaocup.compose.node.element.ComposeScreen
import io.mockk.confirmVerified
import io.mockk.verify
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
class AssoDetailsTest : SuperTest() {
  val assoId = "jMWo6NgngIS2hCq054TF"
  val acronym = "180°C"

  override fun setup() {
    super.setup()
    composeTestRule.activity.setContent {
      AssoDetails(assoId = assoId, navigationActions = mockNavActions)
    }
  }

  @Test
  fun followAsso() {
    DataCache.currentUser.value.following = emptyList()
    run {
      ComposeScreen.onComposeScreen<AssoDetailsScreen>(composeTestRule) {
        step("Follow association") {
          followButton {
            assertIsDisplayed()
            performClick()
            assert(DataCache.currentUser.value.following.contains(assoId))
          }
        }
      }
    }
  }

  @Test
  fun goBack() {
    run {
      ComposeScreen.onComposeScreen<AssoDetailsScreen>(composeTestRule) {
        step("Go back") {
          goBackButton { performClick() }
          verify { mockNavActions.goBack() }
          confirmVerified(mockNavActions)
        }
      }
    }
  }
}
