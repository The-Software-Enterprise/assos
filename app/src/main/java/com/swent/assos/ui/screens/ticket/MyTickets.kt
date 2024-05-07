@file:OptIn(ExperimentalMaterial3Api::class)

package com.swent.assos.ui.screens.ticket

import android.net.Uri
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import com.swent.assos.model.data.Ticket
import com.swent.assos.model.navigation.NavigationActions
import com.swent.assos.ui.components.PageTitle
import com.swent.assos.ui.components.TicketItem
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
      modifier = Modifier.semantics { testTagsAsResourceId = true }.testTag("TicketScreen"),
      topBar = { PageTitle(title = "My Tickets") }) { paddingValues ->
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
