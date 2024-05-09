package com.swent.assos.screens

import androidx.compose.ui.test.SemanticsNodeInteractionsProvider
import io.github.kakaocup.compose.node.element.ComposeScreen
import io.github.kakaocup.compose.node.element.KNode

class MyAssociationsScreen(semanticsProvider: SemanticsNodeInteractionsProvider) :
    ComposeScreen<MyAssociationsScreen>(
        semanticsProvider = semanticsProvider,
        viewBuilderAction = { hasTestTag("MyAssociationsScreen") }) {

  val goBackButton: KNode = onNode { hasTestTag("GoBackButton") }
  val pageTitle: KNode = onNode { hasTestTag("PageTitle") }

  val contentSection: KNode = child { hasTestTag("ContentSection") }
  val associationCard: KNode = contentSection.child { hasTestTag("AssoListItem") }
  val acronym: KNode = associationCard.child { hasTestTag("acronym") }
}
