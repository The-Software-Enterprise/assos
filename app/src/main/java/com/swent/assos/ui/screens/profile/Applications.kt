package com.swent.assos.ui.screens.profile

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.swent.assos.R
import com.swent.assos.model.navigation.NavigationActions
import com.swent.assos.model.view.ApplicationViewModel
import com.swent.assos.ui.components.ApplicationItem
import com.swent.assos.ui.components.PageTitleWithGoBack

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun Applications(navigationActions: NavigationActions) {

  val applicationViewModel: ApplicationViewModel = hiltViewModel()
  val joiningApplications = applicationViewModel.joiningApplications.collectAsState()
  val staffingApplications = applicationViewModel.staffingApplications.collectAsState()

  LaunchedEffect(key1 = Unit) {
    applicationViewModel.updateJoiningApplications()
    applicationViewModel.updateStaffingApplications()
  }

  Scaffold(
      modifier =
          Modifier.semantics { testTagsAsResourceId = true }
              .padding(16.dp)
              .testTag("ApplicationsScreen"),
      topBar = { PageTitleWithGoBack(title = "My Associations", navigationActions) },
  ) { paddingValues ->
    LazyColumn(modifier = Modifier.padding(paddingValues)) {
      item {
        Text(
            modifier = Modifier.testTag("AssociationsApplicationsTitle"),
            text = "Requested Associations",
            style =
                TextStyle(
                    fontSize = 30.sp,
                    lineHeight = 32.sp,
                    fontFamily = FontFamily(Font(R.font.sf_pro_display_regular)),
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onBackground))
        Spacer(modifier = Modifier.padding(8.dp))
      }
      if (joiningApplications.value.isEmpty()) {
        item { Text("No applications found.") }
      }
      for (application in joiningApplications.value) {
        item { ApplicationItem(application) }
      }
      item {
        Spacer(modifier = Modifier.padding(8.dp))
        Text(
            modifier = Modifier.testTag("StaffingApplicationsTitle"),
            text = "Requested Staffing",
            style =
                TextStyle(
                    fontSize = 30.sp,
                    lineHeight = 32.sp,
                    fontFamily = FontFamily(Font(R.font.sf_pro_display_regular)),
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onBackground))
        Spacer(modifier = Modifier.padding(8.dp))
      }
      if (staffingApplications.value.isEmpty()) {
        item { Text("No applications found.") }
      }
      for (application in staffingApplications.value) {
        item { ApplicationItem(application) }
      }
    }
  }
}
