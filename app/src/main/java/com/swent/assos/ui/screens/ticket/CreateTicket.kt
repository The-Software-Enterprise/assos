package com.swent.assos.ui.screens.ticket

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.swent.assos.model.navigation.NavigationActions
import com.swent.assos.model.view.EventViewModel
import com.swent.assos.ui.components.PageTitleWithGoBack

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun CreateTicket(navigationActions: NavigationActions, eventId: String) {
  val eventViewModel: EventViewModel = hiltViewModel()

  var email by remember { mutableStateOf("") }
  var showingSuccess by remember { mutableStateOf(false) }
  var showingError by remember { mutableStateOf(false) }

  @Composable
  fun resultDialog(email: String) {
    // if showingSuccess is true, then text = "Ticket created for $email"

    when {
      showingSuccess -> {
        AlertDialog(
            onDismissRequest = { showingSuccess = false },
            title = { Text("Success") },
            text = { Text("Ticket created for $email") },
            confirmButton = { Button(onClick = { showingSuccess = false }) { Text("Ok") } })
      }
      showingError -> {
        AlertDialog(
            onDismissRequest = { showingError = false },
            title = { Text("Error") },
            text = { Text("$email has no account") },
            confirmButton = { Button(onClick = { showingError = false }) { Text("Ok") } })
      }
    }
  }

  LaunchedEffect(key1 = Unit) { eventViewModel.getEvent(eventId) }

  Scaffold(
      modifier = Modifier.semantics { testTagsAsResourceId = true }.testTag("CreateTicketScreen"),
      topBar = {
        PageTitleWithGoBack(title = "Create a Ticket", navigationActions = navigationActions)
      },
  ) { paddingValues ->
    resultDialog(email)
    Column(
        modifier = Modifier.fillMaxWidth().padding(paddingValues).testTag("Form"),
        horizontalAlignment = Alignment.CenterHorizontally) {
          OutlinedTextField(value = email, onValueChange = { email = it })
          EmailSubmit {
            eventViewModel.createTicket(email, { showingSuccess = true }, { showingError = true })
          }
        }
  }
}

@Composable
fun EmailSubmit(onClick: () -> Unit) {
  Row(
      modifier =
          Modifier.padding(16.dp).width(92.dp).height(48.dp).clickable(true, onClick = onClick),
      horizontalArrangement = Arrangement.Center,
      verticalAlignment = Alignment.Bottom,
  ) {
    Text(text = "Submit")
  }
}
