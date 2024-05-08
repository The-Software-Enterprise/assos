package com.swent.assos.screens

import androidx.compose.ui.test.SemanticsNodeInteractionsProvider
import io.github.kakaocup.compose.node.element.ComposeScreen
import io.github.kakaocup.compose.node.element.KNode

class TicketDetailsScreen(semanticsProvider: SemanticsNodeInteractionsProvider) :
    ComposeScreen<TicketDetailsScreen>(
        semanticsProvider = semanticsProvider,
        viewBuilderAction = { hasTestTag("TicketDetailsScreen") }) {

    val pageTitle: KNode = onNode { hasTestTag("PageTitle") }
}