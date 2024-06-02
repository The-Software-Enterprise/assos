package com.swent.assos.ui.screens.ticket

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.FabPosition
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
import androidx.hilt.navigation.compose.hiltViewModel
import com.swent.assos.model.data.ParticipationStatus
import com.swent.assos.model.navigation.NavigationActions
import com.swent.assos.model.view.EventViewModel
import com.swent.assos.ui.components.JoinUsButton
import com.swent.assos.ui.components.PageTitleWithGoBack

@Composable
fun EmailSubmit(onClick: () -> Unit) {
  JoinUsButton(onClick = onClick, text = "Submit")
}

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
      floatingActionButton = {
        EmailSubmit(
            onClick = {
              eventViewModel.createTicket(
                  email = email,
                  onSuccess = { showingSuccess = true },
                  onFailure = { showingError = true },
                  status = ParticipationStatus.Participant)
            })
      },
      floatingActionButtonPosition = FabPosition.Center,
  ) { paddingValues ->
    resultDialog(email)
    Column(
        modifier = Modifier.fillMaxWidth().padding(paddingValues).testTag("Form"),
        horizontalAlignment = Alignment.CenterHorizontally) {
          OutlinedTextField(
              value = email,
              onValueChange = { email = it },
              modifier = Modifier.testTag("EmailField").padding(paddingValues),
              label = { Text("Email") })
        }
  }
}
