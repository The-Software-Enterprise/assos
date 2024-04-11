package com.swent.assos.screens

import androidx.compose.ui.test.SemanticsNodeInteractionsProvider
import io.github.kakaocup.compose.node.element.ComposeScreen
import io.github.kakaocup.compose.node.element.KNode

class OverviewScreen(semanticsProvider: SemanticsNodeInteractionsProvider) :
    ComposeScreen<OverviewScreen>(
        semanticsProvider = semanticsProvider,
        viewBuilderAction = { hasTestTag("OverviewScreen") }) {

  val appTitle: KNode = child { hasTestTag("AppTitle") }

  val assoList: KNode = child { hasTestTag("AssoList") }
  val assoListItems: KNode = assoList.child { hasTestTag("AssoListItem") }

  val searchAsso: KNode = child { hasTestTag("SearchAsso") }
  val assoListSearch: KNode = searchAsso.child { hasSetTextAction() }
}
