@file:OptIn(ExperimentalMaterial3Api::class)

package com.swent.assos.ui.screens.ticket

import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.swent.assos.model.data.Ticket
import com.swent.assos.model.navigation.Destinations
import com.swent.assos.model.navigation.NavigationActions
import com.swent.assos.model.view.EventViewModel
import com.swent.assos.model.view.TicketViewModel
import com.swent.assos.ui.components.PageTitle
import java.time.LocalDateTime

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun MyTickets(navigationActions: NavigationActions) {

  val viewModel: TicketViewModel = hiltViewModel()
  val myTickets by viewModel.tickets.collectAsState()
    val context = LocalContext.current

  LaunchedEffect(key1 = Unit) { viewModel.getTickets() }

  Scaffold(
      modifier = Modifier
          .semantics { testTagsAsResourceId = true }
          .testTag("MyTicketsScreen"),
      topBar = { PageTitle(title = "My Tickets") },
      floatingActionButton = {
        FloatingActionButton(
            modifier = Modifier.testTag("AddImages"),
            containerColor = MaterialTheme.colorScheme.tertiary,
            contentColor = MaterialTheme.colorScheme.onTertiary,
            onClick = { navigationActions.navigateTo(Destinations.SCAN_TICKET.route) },
            shape = RoundedCornerShape(size = 16.dp)) {
              Image(
                  imageVector = Icons.Default.CameraAlt,
                  contentDescription = null,
                  colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onTertiary))
            }
      }) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(paddingValues)
                .testTag("TicketList"),
            horizontalAlignment = Alignment.CenterHorizontally,
            userScrollEnabled = true,
        ) {
            item {
                Button(
                    onClick = {
                        // Launch NFCActivity

                    }) {
                    Text("Launch NFC Activity")
                }
            }
          items(items = myTickets) {
            TicketItem(ticket = it, navigationActions = navigationActions)
          }
        }
      }
}

@Composable
fun TicketItem(ticket: Ticket, navigationActions: NavigationActions) {

  val viewModel: EventViewModel = hiltViewModel()
  val event by viewModel.event.collectAsState()

  LaunchedEffect(key1 = Unit) { viewModel.getEvent(ticket.eventId) }

  Card(
      colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
      shape = RoundedCornerShape(12.dp),
      modifier =
      Modifier
          .testTag("TicketItem")
          .padding(16.dp)
          .border(
              width = 0.5.dp,
              color = MaterialTheme.colorScheme.outline,
              shape = RoundedCornerShape(12.dp)
          )) {
        Column(
            modifier =
            Modifier
                .fillMaxWidth()
                .padding(vertical = 0.dp)
                .clickable {
                    navigationActions.navigateTo(
                        Destinations.TICKET_DETAILS.route + "/${ticket.eventId}"
                    )
                },
        ) {
          Image(
              painter = rememberAsyncImagePainter(event.image),
              contentDescription = null,
              contentScale = ContentScale.Crop,
              modifier =
              Modifier
                  .fillMaxWidth()
                  .height(84.dp)
                  .background(MaterialTheme.colorScheme.outline))

          Spacer(modifier = Modifier.height(10.dp))
          Text(
              text = event.title,
              style = MaterialTheme.typography.titleMedium,
              modifier = Modifier
                  .fillMaxWidth()
                  .padding(horizontal = 16.dp))
          Spacer(modifier = Modifier.height(6.dp))

          Text(
              text = event.startTime?.let { dateToReadableString(it) } ?: "",
              style = MaterialTheme.typography.bodyMedium,
              modifier = Modifier
                  .fillMaxWidth()
                  .padding(horizontal = 16.dp))
          Spacer(modifier = Modifier.height(10.dp))
        }
      }
}

fun dateToReadableString(date: LocalDateTime): String {
  return "${date.dayOfMonth} ${date.month} ${date.year}, ${date.hour}:${date.minute}"
}
