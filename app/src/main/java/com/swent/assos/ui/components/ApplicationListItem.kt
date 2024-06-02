package com.swent.assos.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.swent.assos.R
import com.swent.assos.model.data.Applicant
import com.swent.assos.model.view.ApplicantViewModel
import com.swent.assos.model.view.AssoViewModel
import com.swent.assos.model.view.UserViewModel
import java.time.LocalDateTime
import kotlinx.coroutines.launch

@Composable
fun ApplicationListItem(
    userId: String,
    eventId: String,
    assoId: String,
    isStaffing: Boolean,
) {

  val userViewModel: UserViewModel = hiltViewModel()
  val user by userViewModel.user.collectAsState()

  val applicantsViewModel: ApplicantViewModel = hiltViewModel()
  val applicants by applicantsViewModel.applicants.collectAsState()

  val assoViewModel: AssoViewModel = hiltViewModel()

  val applicant: Applicant =
      applicants.find { it.userId == userId } ?: Applicant("", "", LocalDateTime.now(), "")

  LaunchedEffect(key1 = Unit) { userViewModel.getUser(userId) }

  var status by remember { mutableStateOf(applicant.status) }
  val scope = rememberCoroutineScope()

  Box(
      modifier =
          Modifier.shadow(4.dp, RoundedCornerShape(6.dp))
              .background(MaterialTheme.colorScheme.background)
              .fillMaxWidth()
              .padding(vertical = 8.dp)
              .padding(horizontal = 5.dp)
              .height(30.dp)
              .testTag("ApplicationListItem")) {
        Row(
            modifier =
                Modifier.fillMaxSize().padding(horizontal = 8.dp).testTag("ApplicationItemRow"),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically) {
              Text(
                  text = user.firstName + " " + user.lastName,
                  style = MaterialTheme.typography.bodyMedium,
                  fontFamily = FontFamily(Font(R.font.sf_pro_display_regular)),
                  fontWeight = FontWeight.Medium,
                  modifier = Modifier.padding(all = 2.dp).testTag("ApplicationListItemFullName"))

              AssistChip(
                  colors =
                      if (status == "accepted")
                          AssistChipDefaults.assistChipColors(
                              containerColor = MaterialTheme.colorScheme.secondary)
                      else if (status == "rejected")
                          AssistChipDefaults.assistChipColors(
                              containerColor = MaterialTheme.colorScheme.error)
                      else
                          AssistChipDefaults.assistChipColors(
                              containerColor = MaterialTheme.colorScheme.surface),
                  border = null,
                  modifier =
                      Modifier.testTag("AcceptApplicationButton").padding(5.dp).fillMaxHeight(),
                  onClick = {
                    scope.launch {
                      if (isStaffing) {
                        when (status) {
                          "accepted" -> {
                            applicantsViewModel.rejectStaff(applicant.id, eventId)
                            status = "rejected"
                          }
                          "pending" -> {
                            applicantsViewModel.acceptStaff(applicant.id, eventId)
                            status = "accepted"
                          }
                          else -> {
                            applicantsViewModel.unAcceptStaff(applicant.id, eventId)
                            status = "pending"
                          }
                        }
                      } else {
                        when (status) {
                          "accepted" -> {
                            assoViewModel.quitAssociation(eventId, userId)
                            applicantsViewModel.rejectApplicant(
                                applicantId = applicant.id, assoId = assoId)
                            status = "rejected"
                          }
                          "pending" -> {
                            assoViewModel.joinAssociation(eventId, userId)
                            applicantsViewModel.acceptApplicant(
                                applicantId = applicant.id, assoId = assoId)
                            status = "accepted"
                          }
                          else -> {
                            assoViewModel.quitAssociation(eventId, userId)
                            applicantsViewModel.unAcceptApplicant(applicant.id, assoId)
                            status = "pending"
                          }
                        }
                      }
                    }
                  },
                  label = {
                    Text(
                        text = status,
                        fontFamily = FontFamily(Font(R.font.sf_pro_display_regular)),
                        fontWeight = FontWeight.Medium,
                    )
                  })

              Image(
                  colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onBackground),
                  imageVector = Icons.Default.Delete,
                  contentDescription = null,
                  modifier =
                      Modifier.testTag("GarbageIcon")
                          .padding(start = 16.dp)
                          .clip(RoundedCornerShape(100))
                          .clickable(enabled = status != "pending") {
                            if (isStaffing) {
                              applicantsViewModel.deleteStaffRequest(userId, eventId)
                            } else {
                              applicantsViewModel.deleteRequest(user.id, assoId)
                            }
                          }
                          .padding(start = 10.dp, end = 5.dp, top = 7.dp, bottom = 7.dp)
                          .padding()
                          .size(20.dp))
            }
      }
}
