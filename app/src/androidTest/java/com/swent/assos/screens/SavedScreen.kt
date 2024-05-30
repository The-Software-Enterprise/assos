package com.swent.assos.screens

import androidx.compose.ui.test.SemanticsNodeInteractionsProvider
import io.github.kakaocup.compose.node.element.ComposeScreen
import io.github.kakaocup.compose.node.element.KNode

class SavedScreen(semanticsProvider: SemanticsNodeInteractionsProvider) :
    ComposeScreen<SavedScreen>(
        semanticsProvider = semanticsProvider, viewBuilderAction = { hasTestTag("SavedScreen") }) {

  val goBackButton: KNode = onNode { hasTestTag("GoBackButton") }
  val pageTitle: KNode = onNode { hasTestTag("PageTitle") }
  val segmentedControl: KNode = onNode { hasTestTag("SegmentedControl") }

  val contentSection: KNode = child { hasTestTag("ContentSection") }
  val savedEvents: KNode = contentSection.child { hasTestTag("SavedEvents") }
  val savedNews: KNode = contentSection.child { hasTestTag("SavedNews") }
}
