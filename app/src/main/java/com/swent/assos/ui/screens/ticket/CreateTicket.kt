package com.swent.assos.ui.screens.ticket

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import com.swent.assos.model.navigation.NavigationActions
import com.swent.assos.ui.components.PageTitleWithGoBack

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun CreateTicket(navigationActions: NavigationActions) {

  Scaffold(
      modifier = Modifier.semantics { testTagsAsResourceId = true }.testTag("CreateTicketScreen"),
      topBar = {
        PageTitleWithGoBack(title = "Create a Ticket", navigationActions = navigationActions)
      },
  ) { paddingValues ->
    Column(
        modifier = Modifier.fillMaxWidth().padding(paddingValues).testTag("Form"),
        horizontalAlignment = Alignment.CenterHorizontally) {
          Text("Not implemented yet!")
        }
  }
}