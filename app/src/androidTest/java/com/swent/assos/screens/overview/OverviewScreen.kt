package com.swent.assos.screens.overview

import androidx.compose.ui.test.SemanticsNodeInteractionsProvider
import io.github.kakaocup.compose.node.element.ComposeScreen
import io.github.kakaocup.compose.node.element.KNode

class OverviewScreen(semanticsProvider: SemanticsNodeInteractionsProvider) :
    ComposeScreen<OverviewScreen>(
        semanticsProvider = semanticsProvider,
        viewBuilderAction = { hasTestTag("OverviewScreen") }) {

  val appTitle1: KNode = child { hasTestTag("AppTitle_1") }
  val appTitle2: KNode = child { hasTestTag("AppTitle_2") }

  val assoList: KNode = child { hasTestTag("AssoList") }
  val assoListItems: KNode = assoList.child { hasTestTag("AssoListItem") }

  val searchAsso: KNode = child { hasTestTag("SearchAsso") }
  val assoListSearch: KNode = searchAsso.child { hasSetTextAction() }
}
