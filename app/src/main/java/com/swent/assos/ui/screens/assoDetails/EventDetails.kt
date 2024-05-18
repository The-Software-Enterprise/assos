package com.swent.assos.ui.screens.assoDetails

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.swent.assos.model.data.Association
import com.swent.assos.model.data.Event
import com.swent.assos.model.navigation.Destinations
import com.swent.assos.model.navigation.NavigationActions
import com.swent.assos.model.view.AssoViewModel
import com.swent.assos.model.view.EventViewModel
import com.swent.assos.model.view.ProfileViewModel
import com.swent.assos.ui.components.PageTitleWithGoBack
import com.swent.assos.ui.screens.manageAsso.createEvent.components.EventContent

@Composable
fun EventDetails(eventId: String, navigationActions: NavigationActions, assoId: String) {

  val eventViewModel: EventViewModel = hiltViewModel()

  val event by eventViewModel.event.collectAsState()
  val assoViewModel: AssoViewModel = hiltViewModel()
  val asso by assoViewModel.association.collectAsState()
  var confirming by remember { mutableStateOf(false) }

  val userId by assoViewModel.currentUser.collectAsState()

  val viewModel: ProfileViewModel = hiltViewModel()
  val myAssociations by viewModel.memberAssociations.collectAsState()

  LaunchedEffect(key1 = Unit) {
    assoViewModel.getAssociation(assoId)
    eventViewModel.getEvent(eventId)
    Log.d("EventDetails", "EventDetails: ${event.image}")
  }
  ConfirmDialog(
      onDismissRequest = { confirming = false },
      confirmButton = {
        ConfirmButton(
            onConfirm = {
              confirming = false
              eventViewModel.applyStaffing(userId.id, {})
            })
      },
      text = "Do you want to apply for staffing? \n You will be contacted by the association.",
      showing = confirming)

  Scaffold(
      modifier = Modifier
          .testTag("EventDetails")
          .semantics { contentDescription = "EventDetails" },
      topBar = { PageTitleWithGoBack(title = asso.acronym, navigationActions = navigationActions) },
      floatingActionButton = {
        if (eventId != "") {
          when (isMember(myAssociations = myAssociations, currentAsso = asso.id)) {
            true ->
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                    if (event.isStaffingEnabled) {
                        JoinUsButton(
                            onClick = {
                                navigationActions.navigateTo(
                                    Destinations.STAFF_MANAGEMENT.route + "/${eventId}"
                                )
                            },
                            text = "Staff List"
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                    }
                  JoinUsButton(
                      onClick = {
                        navigationActions.navigateTo(
                            Destinations.CREATE_TICKET.route + "/${eventId}")
                      },
                      text = "Create ticket")
                }
            false ->
                if (event.isStaffingEnabled) {
                  JoinUsButton(onClick = { confirming = true }, text = "Become Staff")
                }
          }
        }
      },
      floatingActionButtonPosition = FabPosition.Center) { paddingValues ->
        EventContent(viewModel = eventViewModel, paddingValues = paddingValues)
      }
}

@Composable
fun ConfirmDialog(
    onDismissRequest: () -> Unit,
    confirmButton: @Composable () -> Unit,
    text: String,
    showing: Boolean
) {
  // create a dialog to confirm the action
  if (showing) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        confirmButton = confirmButton,
        text = { Text(text = text) },
        containerColor = MaterialTheme.colorScheme.primaryContainer,
        dismissButton = { ConfirmButton(onDismissRequest, "No") })
  }
}

@Composable
fun ConfirmButton(onConfirm: () -> Unit, text: String = "Yes") {
  Text(text = text, modifier = Modifier.clickable { onConfirm() })
}

@Composable
fun isMember(myAssociations: List<Association>, currentAsso: String): Boolean {
  for (asso in myAssociations) {
    if (asso.id == currentAsso) {
      return true
    }
  }
  return false
}
