package com.swent.assos.screens

import androidx.compose.ui.test.SemanticsNodeInteractionsProvider
import io.github.kakaocup.compose.node.element.ComposeScreen
import io.github.kakaocup.compose.node.element.KNode

class EventDetailsScreen(semanticsProvider: SemanticsNodeInteractionsProvider) :
    ComposeScreen<EventDetailsScreen>(
        semanticsProvider = semanticsProvider,
        viewBuilderAction = { hasTestTag("ExplorerScreen") }) {

  val eventDetails: KNode = child { hasTestTag("EventDetails") }
  val eventDetailsTitle: KNode = eventDetails.child { hasTestTag("EventDetailsTitle") }
  val eventDetailsDescription: KNode = eventDetails.child { hasTestTag("EventDetailsDescription") }
  val eventDetailsDate: KNode = eventDetails.child { hasTestTag("EventDetailsDate") }
  val eventDetailsLocation: KNode = eventDetails.child { hasTestTag("EventDetailsLocation") }
}