package com.swent.assos.screens

import androidx.compose.ui.test.SemanticsNodeInteractionsProvider
import io.github.kakaocup.compose.node.element.ComposeScreen
import io.github.kakaocup.compose.node.element.KNode

class AssoDigestScreen(semanticsProvider: SemanticsNodeInteractionsProvider) :
    ComposeScreen<AssoDigestScreen>(
        semanticsProvider = semanticsProvider,
        viewBuilderAction = { hasTestTag("AssoDigestScreen") }) {

  val goBackButton: KNode = onNode { hasTestTag("GoBackButton") }

  val assoTitle: KNode = onNode { hasTestTag("AssoTitle") }
  val assoWebsite: KNode = onNode { hasTestTag("AssoWebsite") }
  val assoName: KNode = onNode { hasTestTag("AssoName") }
}
