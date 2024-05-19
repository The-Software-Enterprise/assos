package com.swent.assos.screens

import androidx.compose.ui.test.SemanticsNodeInteractionsProvider
import io.github.kakaocup.compose.node.element.ComposeScreen
import io.github.kakaocup.compose.node.element.KNode

class EventDetailsScreen(semanticsProvider: SemanticsNodeInteractionsProvider) :
    ComposeScreen<EventDetailsScreen>(
        semanticsProvider = semanticsProvider, viewBuilderAction = { hasTestTag("EventDetails") }) {

  val eventContent: KNode = child { hasTestTag("EventContent") }
  val setupNFCTag: KNode = eventContent.child { hasTestTag("SetupNFCTag") }
  val eventDetailsTitle: KNode = child { hasTestTag("EventDetailsTitle") }
  val eventDetailsDescription: KNode = child { hasTestTag("EventDetailsDescription") }
  val eventDetailsDate: KNode = child { hasTestTag("EventDetailsDate") }
  val eventDetailsLocation: KNode = child { hasTestTag("EventDetailsLocation") }
  val eventStaffListButton: KNode = child { hasTestTag("JoinButton") }
}
