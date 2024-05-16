package com.swent.assos.screens

import androidx.compose.ui.test.SemanticsNodeInteractionsProvider
import io.github.kakaocup.compose.node.element.ComposeScreen
import io.github.kakaocup.compose.node.element.KNode

class EventDetailsScreen(semanticsProvider: SemanticsNodeInteractionsProvider) :
    ComposeScreen<EventDetailsScreen>(
        semanticsProvider = semanticsProvider,
        viewBuilderAction = { hasTestTag("ExplorerScreen") }) {

  val eventDetails: KNode = child { hasTestTag("EventDetails") }
    val setupNFCTag: KNode = eventDetails.child { hasTestTag("SetupNFCTag") }
}
