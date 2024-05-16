package com.swent.assos.ui.screens.assoDetails

import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.swent.assos.NFCWriter
import com.swent.assos.R
import com.swent.assos.model.data.Association
import com.swent.assos.model.data.Event
import com.swent.assos.model.navigation.Destinations
import com.swent.assos.model.navigation.NavigationActions
import com.swent.assos.model.view.AssoViewModel
import com.swent.assos.model.view.EventViewModel
import com.swent.assos.model.view.ProfileViewModel

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

  val launcher =
      rememberLauncherForActivityResult(
          contract = ActivityResultContracts.StartActivityForResult()) { result ->
            // Handle the result of the activity here
            // For example, you can retrieve data from the activity result
            val data = result.data
            // Handle the data accordingly
          }
  val intent = Intent(LocalContext.current, NFCWriter::class.java).putExtra("eventID", eventId)

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
      modifier =
          Modifier.testTag("ExplorerScreen").semantics { contentDescription = "ExplorerScreen" },
      topBar = { TopNewsBar(asso, navigationActions, event) },
      floatingActionButton = {
        if (eventId != "") {
          when (isMember(myAssociations = myAssociations, currentAsso = asso.id)) {
            true ->
                JoinUsButton(
                    onClick = {
                      navigationActions.navigateTo(Destinations.CREATE_TICKET.route + "/${eventId}")
                    },
                    text = "Create ticket")
            false -> JoinUsButton(onClick = { confirming = true }, text = "Become Staff")
          }
        }
      },
      floatingActionButtonPosition = FabPosition.Center) { paddingValues ->
        LazyColumn(
            modifier = Modifier.testTag("EventDetails").padding(paddingValues),
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
                        .background(MaterialTheme.colorScheme.background)
                        .fillMaxWidth(),
                alignment = Alignment.Center,
                contentScale = ContentScale.Crop)
          }

          item {
            Text(
                text = event.description,
                style = MaterialTheme.typography.bodyMedium,
                fontFamily = FontFamily(Font(R.font.sf_pro_display_regular)),
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(all = 8.dp).testTag("eventDescriptionText"))
          }

          if (myAssociations.find { it.id == assoId } != null) {
            item {
              Spacer(modifier = Modifier.padding(top = 15.dp))
              JoinUsButton(
                  onClick = {
                    navigationActions.navigateTo(
                        Destinations.STAFF_MANAGEMENT.route + "/${eventId}")
                  },
                  text = "Staff List")
            }
          }

          item {
            if (isMember(myAssociations = myAssociations, currentAsso = asso.id)) {
              Button(
                  modifier = Modifier.testTag("SetupNFCTag"),
                  onClick = { launcher.launch(intent) }) {
                    Text("Setup NFC Tag")
                  }
            }
          }
        }
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

@Composable
fun ConfirmButton(onConfirm: () -> Unit, text: String = "Yes") {
  Text(text = text, modifier = Modifier.clickable { onConfirm() })
}

fun isMember(myAssociations: List<Association>, currentAsso: String): Boolean {
  return myAssociations.map { it.id }.contains(currentAsso)
}
