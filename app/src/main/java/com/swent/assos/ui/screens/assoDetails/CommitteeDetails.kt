package com.swent.assos.ui.screens.assoDetails

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.swent.assos.model.navigation.NavigationActions
import com.swent.assos.model.view.AssoViewModel
import com.swent.assos.ui.components.CommitteeItem
import com.swent.assos.ui.components.PageTitleWithGoBack

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun CommitteeDetails(assoId: String, assoName: String, navigationActions: NavigationActions) {

  val viewModel: AssoViewModel = hiltViewModel()
  val committeeList by viewModel.committee.collectAsState()

  val listState = rememberLazyListState()

  LaunchedEffect(key1 = Unit) { viewModel.getCommittee(assoId) }

  Scaffold(
      modifier = Modifier.testTag("CommitteeScreen"),
      topBar = {
        PageTitleWithGoBack(title = "Members of the committee of " + assoName, navigationActions)
      }) { paddingValues ->
        LazyColumn(
            modifier =
                Modifier.padding(paddingValues)
                    .padding(horizontal = 15.dp)
                    .padding(vertical = 5.dp)
                    .testTag("CommitteeList"),
            verticalArrangement = Arrangement.spacedBy(15.dp),
            userScrollEnabled = true,
            state = listState) {
              if (committeeList.isNotEmpty()) {
                items(committeeList) { member -> CommitteeItem(member.memberId, member.position) }
              } else {
                item { Text(text = "No member in the committee") }
              }
            }
      }
}
