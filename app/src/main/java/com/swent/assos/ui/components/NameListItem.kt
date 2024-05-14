package com.swent.assos.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.swent.assos.R
import com.swent.assos.model.data.Applicant
import com.swent.assos.model.view.ApplicantViewModel
import com.swent.assos.model.view.UserViewModel
import java.time.LocalDateTime
import kotlinx.coroutines.launch

@Composable
fun NameListItem(
    userId: String,
    eventId: String,
    isStaffing: Boolean,
) {

  val userViewModel: UserViewModel = hiltViewModel()
  val user by userViewModel.user.collectAsState()

  val applicantsViewModel: ApplicantViewModel = hiltViewModel()

  val applicants by applicantsViewModel.applicants.collectAsState()

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
              .testTag("NameListItem")) {
        Row(
            modifier = Modifier.fillMaxSize().padding(horizontal = 8.dp).testTag("NameItemRow"),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically) {
              Text(
                  text = user.firstName + " " + user.lastName,
                  style = MaterialTheme.typography.bodyMedium,
                  fontFamily = FontFamily(Font(R.font.sf_pro_display_regular)),
                  fontWeight = FontWeight.Medium,
                  modifier = Modifier.padding(all = 2.dp).testTag("NameListItemFullName"))

              AssistChip(
                  colors =
                      if (status == "accepted")
                          AssistChipDefaults.assistChipColors(
                              containerColor = MaterialTheme.colorScheme.surface)
                      else
                          AssistChipDefaults.assistChipColors(
                              containerColor = MaterialTheme.colorScheme.primary),
                  border = null,
                  modifier = Modifier.testTag("AcceptStaffButton").padding(5.dp).fillMaxHeight(),
                  onClick = {
                    scope.launch {
                      if (isStaffing) {
                        if (status == "accepted") {
                          applicantsViewModel.unAcceptStaff(applicant.id, eventId)
                          status = "pending" // Assuming pending is the initial state
                        } else {
                          applicantsViewModel.acceptStaff(applicant.id, eventId)
                          status = "accepted"
                        }
                      } else {
                        // TODO IN CASE OF AN APPLICATION TO JOIN THE COMMITTEE OF AN ASSOCIATION
                      }
                    }
                  },
                  label = {
                    Text(
                        text = if (status == "accepted") "Un-Accept" else "Accept",
                        color =
                            if (status == "accepted") MaterialTheme.colorScheme.onSurface
                            else MaterialTheme.colorScheme.onPrimary,
                        fontFamily = FontFamily(Font(R.font.sf_pro_display_regular)),
                        fontWeight = FontWeight.Medium,
                    )
                  })
            }
      }
}
