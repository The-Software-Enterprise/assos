@file:OptIn(ExperimentalMaterial3Api::class)

package com.swent.assos.ui.screens.ticket

import android.net.Uri
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.swent.assos.model.data.Ticket
import com.swent.assos.model.navigation.Destinations
import com.swent.assos.model.navigation.NavigationActions
import com.swent.assos.ui.components.PageTitle
import java.time.LocalDateTime

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun MyTickets(navigationActions: NavigationActions) {

  val balelecTicket =
      Ticket(
          name = "Balélec Ticket",
          startTime = LocalDateTime.now(),
          banner =
              Uri.parse(
                  "https://scontent-zrh1-1.xx.fbcdn.net/v/t39.30808-6/417379790_892614632321109_331978589442030329_n.jpg?stp=cp6_dst-jpg&_nc_cat=104&ccb=1-7&_nc_sid=5f2048&_nc_ohc=BCPdUFeZuqoQ7kNvgFT7nt-&_nc_ht=scontent-zrh1-1.xx&oh=00_AfA0SUayO8Bt2y2LQJLNHaL8CP7NSV5ChMfmQuuP5fxrHA&oe=663AC822"))

  val rocketTeamTicket =
      Ticket(
          name = "Rocket Launch Ticket",
          startTime = LocalDateTime.now(),
      )

  val challengeTicket = Ticket(name = "Challenge Week Ticket", startTime = LocalDateTime.now())

  val amacPartyTicket = Ticket(name = "AMAC Party Ticket", startTime = LocalDateTime.now())

  val myTickets = listOf(balelecTicket, rocketTeamTicket, challengeTicket, amacPartyTicket)

  Scaffold(
      modifier = Modifier.semantics { testTagsAsResourceId = true }.testTag("MyTicketsScreen"),
      topBar = { PageTitle(title = "My Tickets") },
              floatingActionButton = {
          FloatingActionButton(
              modifier = Modifier.testTag("AddImages"),
              containerColor = MaterialTheme.colorScheme.tertiary,
              contentColor = MaterialTheme.colorScheme.onTertiary,
              onClick = {  },
              shape = RoundedCornerShape(size = 16.dp)) {
              Image(imageVector = Icons.Default.CameraAlt, contentDescription = null)
          }
      }) { paddingValues ->
        LazyColumn(
            modifier = Modifier.fillMaxWidth().padding(paddingValues).testTag("TicketList"),
            horizontalAlignment = Alignment.CenterHorizontally,
            userScrollEnabled = true,
        ) {
          items(items = myTickets) {
            TicketItem(ticket = it, navigationActions = navigationActions)
          }
        }
      }
}

@Composable
fun TicketItem(ticket: Ticket, navigationActions: NavigationActions) {
  Card(
      colors = CardDefaults.cardColors(MaterialTheme.colorScheme.onPrimary),
      shape = RoundedCornerShape(12.dp),
      modifier =
          Modifier.testTag("TicketItem")
              .padding(16.dp)
              .border(
                  width = 0.5.dp,
                  color = MaterialTheme.colorScheme.outline,
                  shape = RoundedCornerShape(12.dp))) {
        Column(
            modifier =
                Modifier.fillMaxWidth().padding(vertical = 0.dp).clickable {
                  navigationActions.navigateTo(Destinations.TICKET_DETAILS.route)
                },
        ) {
          Image(
              painter = rememberAsyncImagePainter(ticket.banner),
              contentDescription = null,
              contentScale = ContentScale.Crop,
              modifier =
                  Modifier.fillMaxWidth()
                      .height(84.dp)
                      .background(MaterialTheme.colorScheme.outline))

          Spacer(modifier = Modifier.height(10.dp))
          Text(
              text = ticket.name,
              style = MaterialTheme.typography.titleMedium,
              modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp))
          Spacer(modifier = Modifier.height(6.dp))

          Text(
              text = ticket.startTime?.let { dateToReadableString(it) } ?: "",
              style = MaterialTheme.typography.bodyMedium,
              modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp))
          Spacer(modifier = Modifier.height(10.dp))
        }
      }
}

fun dateToReadableString(date: LocalDateTime): String {
  return "${date.dayOfMonth} ${date.month} ${date.year}, ${date.hour}:${date.minute}"
}
