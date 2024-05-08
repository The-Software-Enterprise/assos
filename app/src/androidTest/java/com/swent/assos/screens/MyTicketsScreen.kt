package com.swent.assos.screens

import androidx.compose.ui.test.SemanticsNodeInteractionsProvider
import io.github.kakaocup.compose.node.element.ComposeScreen
import io.github.kakaocup.compose.node.element.KNode

class MyTicketsScreen(semanticsProvider: SemanticsNodeInteractionsProvider) :
    ComposeScreen<MyTicketsScreen>(
        semanticsProvider = semanticsProvider,
        viewBuilderAction = { hasTestTag("MyTicketsScreen") }) {

    val pageTitle: KNode = onNode { hasTestTag("PageTitle") }

    val ticketList: KNode = child { hasTestTag("TicketList") }
    val ticketItem: KNode = ticketList.child { hasTestTag("TicketItem") }


}