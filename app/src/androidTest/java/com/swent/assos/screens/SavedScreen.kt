package com.swent.assos.screens

import androidx.compose.ui.test.SemanticsNodeInteractionsProvider
import io.github.kakaocup.compose.node.element.ComposeScreen
import io.github.kakaocup.compose.node.element.KNode

class SavedScreen(semanticsProvider: SemanticsNodeInteractionsProvider) :
    ComposeScreen<SavedScreen>(
        semanticsProvider = semanticsProvider, viewBuilderAction = { hasTestTag("SavedScreen") }) {

  val goBackButton: KNode = onNode { hasTestTag("GoBackButton") }
  val pageTitle: KNode = onNode { hasTestTag("PageTitle") }

  val contentSection: KNode = child { hasTestTag("ContentSection") }
}
