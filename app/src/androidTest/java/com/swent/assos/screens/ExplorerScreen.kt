package com.swent.assos.screens

import androidx.compose.ui.test.SemanticsNodeInteractionsProvider
import io.github.kakaocup.compose.node.element.ComposeScreen
import io.github.kakaocup.compose.node.element.KNode

class ExplorerScreen(semanticsProvider: SemanticsNodeInteractionsProvider) :
    ComposeScreen<ExplorerScreen>(
        semanticsProvider = semanticsProvider,
        viewBuilderAction = { hasTestTag("ExplorerScreen") }) {

  val assoList: KNode = child { hasTestTag("AssoList") }
  val assoListoneEighty: KNode = assoList.child { hasTestTag("AssoListItem180°C") }

  val searchAsso: KNode = child { hasTestTag("SearchAsso") }
  val assoListSearch: KNode = searchAsso.child { hasSetTextAction() }
}
