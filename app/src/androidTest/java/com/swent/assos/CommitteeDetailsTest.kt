package com.swent.assos

import androidx.activity.compose.setContent
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.isDisplayed
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.swent.assos.model.data.CommitteeMember
import com.swent.assos.model.data.User
import com.swent.assos.model.serialize
import com.swent.assos.screens.CreateNewsScreen
import com.swent.assos.ui.screens.assoDetails.CommitteeDetails
import dagger.hilt.android.testing.HiltAndroidTest
import io.github.kakaocup.compose.node.element.ComposeScreen
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
class CommitteeDetailsTest : SuperTest() {

  val associationName = "JE EPFL"
  val assoId = "9hciFZwTKU9rWg4r0B2A"

  val member =
      CommitteeMember(
          id = "111111Member",
          memberId = "111111User",
          position = "President",
          rank = 1,
      )

  val user = User(id = "111111User", firstName = "Jules", lastName = "Schneuwly", email = "")

  override fun setup() {
    super.setup()
    Firebase.firestore.collection("users").document(user.id).set(serialize(user))
    Firebase.firestore
        .collection("associations")
        .document(assoId)
        .collection("committee")
        .document(member.id)
        .set(member)
  }

  @Test
  fun testCommitteeDetails() {
    composeTestRule.activity.setContent {
      CommitteeDetails(
          navigationActions = mockNavActions, assoId = assoId, assoName = associationName)
    }

    run {
      ComposeScreen.onComposeScreen<CreateNewsScreen>(composeTestRule) {
        step("Check if the title is displayed") {
          composeTestRule
              .onNodeWithText("Members of the committee of " + associationName)
              .assertIsDisplayed()
        }
        step("Check if the members are displayed") {
          composeTestRule.waitUntil(
              condition = { composeTestRule.onNodeWithText("Jules Schneuwly").isDisplayed() },
              timeoutMillis = 10000)

          composeTestRule.onNodeWithText("Jules Schneuwly").assertIsDisplayed()
          composeTestRule.waitUntil(
              condition = { composeTestRule.onNodeWithText("President").isDisplayed() },
              timeoutMillis = 10000)

          composeTestRule.onNodeWithText("President").assertIsDisplayed()
        }
      }
    }
  }
}
