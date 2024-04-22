package com.swent.assos

import androidx.activity.compose.setContent
import com.swent.assos.model.data.DataCache
import com.swent.assos.screens.AssoDetailsScreen
import com.swent.assos.ui.screens.assoDetails.AssoDetails
import dagger.hilt.android.testing.HiltAndroidTest
import io.github.kakaocup.compose.node.element.ComposeScreen
import io.mockk.confirmVerified
import io.mockk.verify
import org.junit.Test

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
  fun followUnfollowAsso() {
    run {
      ComposeScreen.onComposeScreen<AssoDetailsScreen>(composeTestRule) {
        step("Follow association") {
          title {
            assertIsDisplayed()
            assertTextEquals(acronym)
          }
          followButton {
            assertIsDisplayed()
            performClick()
            assert(DataCache.currentUser.value.following.contains(assoId))
          }
        }
        step("Unfollow association") {
          followButton {
            performClick()
            assert(!DataCache.currentUser.value.following.contains(assoId))
          }
        }
        step("Go back") {
          goBackButton { performClick() }
          verify { mockNavActions.goBack() }
          confirmVerified(mockNavActions)
        }
      }
    }
  }
}
