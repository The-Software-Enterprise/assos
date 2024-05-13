package com.swent.assos.ui.screens.assoDetails

import android.net.Uri
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.swent.assos.R
import com.swent.assos.model.data.Association
import com.swent.assos.model.data.Event
import com.swent.assos.model.navigation.Destinations
import com.swent.assos.model.navigation.NavigationActions
import com.swent.assos.model.view.AssoViewModel
import com.swent.assos.model.view.EventViewModel
import com.swent.assos.model.view.ProfileViewModel
import com.swent.assos.ui.components.ListItemAsso

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
      topBar = { TopNewsBar(asso, navigationActions, event) },
      floatingActionButton = {
        if (eventId != "") {
          if (isMember(myAssociations = myAssociations, cuurentAsso = asso.id)) {
            CreateTicketButton(navigationActions)
          } else {
            JoinUsButton(onClick = { confirming = true }, text = "Become Staff")
          }
        }
      },
      floatingActionButtonPosition = FabPosition.Center,
  ) { paddingValues ->
    LazyColumn(
        modifier = Modifier
            .testTag("EventDetailsList")
            .padding(paddingValues),
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
            Modifier
                .padding(10.dp)
                .height(200.dp)
                .clip(shape = RoundedCornerShape(20.dp))
                .background(MaterialTheme.colorScheme.background)
                .fillMaxWidth(),
            alignment = Alignment.Center,
            contentScale = ContentScale.Crop)
      }

      item { Text(text = event.description, modifier = Modifier.padding(10.dp)) }

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
            modifier = Modifier
                .testTag("GoBackButton")
                .clickable { navigationActions.goBack() })
      },
      actions = {})
}

@Composable
fun ConfirmButton(onConfirm: () -> Unit, text: String = "Yes") {
  Text(text = text, modifier = Modifier.clickable { onConfirm() })
}

@Composable
fun isMember(myAssociations : List<Association>, cuurentAsso : String) : Boolean {
    var isMember = false
    for (asso in myAssociations) {
        if (asso.id == cuurentAsso) {
        isMember = true
        }
    }
    return isMember
}

@Composable
fun CreateTicketButton(navigationActions: NavigationActions) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
    ) {
        FloatingActionButton(
            onClick = {navigationActions.navigateTo(Destinations.CREATE_TICKET.route)},
            modifier =
            Modifier.shadow(8.dp, shape = RoundedCornerShape(25), clip = false)
                .background(color = Color(0xFF5465FF), shape = RoundedCornerShape(size = 16.dp))
                .then(Modifier.widthIn(min = 92.dp))
                .height(42.dp)
                .testTag("CreateTicketButton"),
            containerColor = Color(0xFF5465FF),
        ) {
            Text(
                text = "Create ticket",
                fontSize = 16.sp,
                fontFamily = FontFamily(Font(R.font.sf_pro_display_regular)),
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onPrimary,
                style = MaterialTheme.typography.bodyMedium)
        }
    }

}