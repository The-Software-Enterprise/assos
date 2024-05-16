package com.swent.assos.screens

import androidx.compose.ui.test.SemanticsNodeInteractionsProvider
import io.github.kakaocup.compose.node.element.ComposeScreen
import io.github.kakaocup.compose.node.element.KNode

class ScanTicketScreen(semanticsProvider: SemanticsNodeInteractionsProvider) :
    ComposeScreen<ScanTicketScreen>(
        semanticsProvider = semanticsProvider,
        viewBuilderAction = { hasTestTag("ScanTicketScreen") }) {

  val pageTitle: KNode = onNode { hasTestTag("PageTitle") }
  val goBackButton: KNode = onNode { hasTestTag("GoBackButton") }
}
