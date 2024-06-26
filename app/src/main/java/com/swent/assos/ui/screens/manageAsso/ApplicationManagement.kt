package com.swent.assos.ui.screens.manageAsso

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.swent.assos.R
import com.swent.assos.model.navigation.NavigationActions
import com.swent.assos.model.view.ApplicantViewModel
import com.swent.assos.ui.components.ApplicationListItem
import com.swent.assos.ui.components.PageTitleWithGoBack

@Composable
fun ApplicationManagement(assoId: String, navigationActions: NavigationActions) {

  val applicantsViewModel: ApplicantViewModel = hiltViewModel()

  val applicants by applicantsViewModel.applicants.collectAsState()
  val listState = rememberLazyListState()

  val update = applicantsViewModel.update.collectAsState()

  LaunchedEffect(key1 = Unit) { applicantsViewModel.getApplicantsForJoining(assoId) }

  LaunchedEffect(key1 = update.value) { applicantsViewModel.updateApplicants(assoId) }

  Scaffold(
      modifier = Modifier.testTag("ApplicationManagementScreen"),
      topBar = {
        PageTitleWithGoBack(title = "Application Management", navigationActions = navigationActions)
      }) { paddingValues ->
        LazyColumn(
            contentPadding = paddingValues,
            modifier =
                Modifier.fillMaxHeight()
                    .padding(horizontal = 10.dp)
                    .padding(vertical = 7.dp)
                    .padding(start = 16.dp, end = 16.dp)
                    .background(MaterialTheme.colorScheme.surface)
                    .testTag("ApplicationList"),
            verticalArrangement = Arrangement.spacedBy(15.dp),
            userScrollEnabled = true,
            state = listState) {
              if (applicants.isNotEmpty()) {
                items(applicants) { applicant ->
                  ApplicationListItem(
                      userId = applicant.userId,
                      eventId = assoId,
                      assoId = assoId,
                      isStaffing = false)
                }
              } else {
                // Show a message if there are no applicants
                item {
                  Text(
                      modifier =
                          Modifier.fillMaxWidth()
                              .fillMaxHeight()
                              .padding(horizontal = 10.dp)
                              .padding(vertical = 7.dp)
                              .padding(start = 16.dp, end = 16.dp),
                      text = "No applicants",
                      textAlign = TextAlign.Center,
                      fontSize = 14.sp,
                      fontFamily = FontFamily(Font(R.font.sf_pro_display_regular)),
                      color = MaterialTheme.colorScheme.onBackground)
                }
              }
            }
      }
}
