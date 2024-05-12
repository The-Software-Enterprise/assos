package com.swent.assos.screens

import androidx.compose.ui.test.SemanticsNodeInteractionsProvider
import io.github.kakaocup.compose.node.element.ComposeScreen
import io.github.kakaocup.compose.node.element.KNode

class TicketDetailsScreen(semanticsProvider: SemanticsNodeInteractionsProvider) :
    ComposeScreen<TicketDetailsScreen>(
        semanticsProvider = semanticsProvider,
        viewBuilderAction = { hasTestTag("TicketDetailsScreen") }) {

  val pageTitle: KNode = onNode { hasTestTag("PageTitle") }
  val goBackButton: KNode = onNode { hasTestTag("GoBackButton") }

  val ticketDetails: KNode = onNode { hasTestTag("TicketDetails") }
  val eventImage: KNode = ticketDetails.child { hasTestTag("EventImage") }
  val startTime: KNode = ticketDetails.child { hasTestTag("StartTime") }
  val endTime: KNode = ticketDetails.child { hasTestTag("EndTime") }
  val description: KNode = ticketDetails.child { hasTestTag("Description") }
}
