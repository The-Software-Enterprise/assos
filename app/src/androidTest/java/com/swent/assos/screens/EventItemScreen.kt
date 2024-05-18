package com.swent.assos.screens

import androidx.compose.ui.test.SemanticsNodeInteractionsProvider
import io.github.kakaocup.compose.node.element.ComposeScreen
import io.github.kakaocup.compose.node.element.KNode

class EventItemScreen(semanticsProvider: SemanticsNodeInteractionsProvider) :
    ComposeScreen<EventItemScreen>(
        semanticsProvider = semanticsProvider, viewBuilderAction = { hasTestTag("EventItem") }) {

  val eventItem: KNode = onNode { hasTestTag("EventItem") }
  val eventItemImage: KNode = onNode { hasTestTag("EventItemImage") }
}
