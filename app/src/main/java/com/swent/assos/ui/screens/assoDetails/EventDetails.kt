package com.swent.assos.ui.screens.assoDetails

import android.net.Uri
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.swent.assos.R
import com.swent.assos.model.data.Association
import com.swent.assos.model.data.Event
import com.swent.assos.model.navigation.NavigationActions
import com.swent.assos.model.view.AssoViewModel
import com.swent.assos.model.view.EventViewModel

@Composable
fun EventDetails(eventId: String, navigationActions: NavigationActions, assoId: String) {

  val eventViewModel: EventViewModel = hiltViewModel()

  val event by eventViewModel.event.collectAsState()
  val assoViewModel: AssoViewModel = hiltViewModel()
  val asso by assoViewModel.association.collectAsState()
  var confirming by remember { mutableStateOf(false) }

  LaunchedEffect(key1 = Unit) {
    assoViewModel.getAssociation(assoId)
    eventViewModel.getEvent(eventId)
    Log.d("EventDetails", "EventDetails: ${event.image}")
  }
  ConfirmDialog(
      onDismissRequest = {},
      confirmButton = { eventViewModel.applyStaffing(eventId, {}) },
      text = "Do you want to apply for staffing?",
      showing = confirming)

  Scaffold(
      modifier = Modifier.testTag("EventDetails").semantics { contentDescription = "EventDetails" },
      topBar = { TopNewsBar(asso, navigationActions, event) },
      floatingActionButton = {
        JoinUsButton {
          // call the function to apply for staffing

          eventViewModel.applyStaffing(assoId) {
            // show a snackbar

          }
        }
      },
      floatingActionButtonPosition = FabPosition.Center,
  ) { paddingValues ->
    LazyColumn(
        modifier = Modifier.testTag("EventDetailsList").padding(paddingValues),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
      item {
        Image(
            painter =
                if (event.image != Uri.EMPTY) {
                  rememberAsyncImagePainter(event.image)
                } else {
                  painterResource(id = R.drawable.ic_launcher_foreground)
                },
            contentDescription = null,
            modifier =
                Modifier.padding(10.dp)
                    .height(200.dp)
                    .clip(shape = RoundedCornerShape(20.dp))
                    .background(Color.Gray)
                    .fillMaxWidth(),
            alignment = Alignment.Center,
            contentScale = ContentScale.Crop)
      }

      item { Text(text = event.description, modifier = Modifier.padding(10.dp)) }
    }

    JoinUsButton { confirming = true }
  }
}

@Composable
fun ConfirmDialog(
    onDismissRequest: () -> Unit,
    confirmButton: () -> Unit,
    text: String,
    showing: Boolean
) {
  // create a dialog to confirm the action
  if (true) {
    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(dismissOnBackPress = true, dismissOnClickOutside = true)) {
          AlertDialog(
              onDismissRequest = onDismissRequest,
              confirmButton = { confirmButton() },
              text = { Text(text) })
        }
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopNewsBar(
    asso: Association,
    navigationActions: NavigationActions,
    event: Event,
) {

  MediumTopAppBar(
      colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent),
      modifier = Modifier.testTag("Header"),
      title = { Text(event.title, modifier = Modifier.testTag("Title")) },
      navigationIcon = {
        Image(
            imageVector = Icons.Default.ArrowBack,
            contentDescription = null,
            modifier = Modifier.testTag("GoBackButton").clickable { navigationActions.goBack() })
      },
      actions = {})
}
