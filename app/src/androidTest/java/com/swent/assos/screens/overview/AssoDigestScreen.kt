package com.swent.assos.screens.overview

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
  val createButton: KNode = child { hasTestTag("CreateButton") }

  val bottomSheetCreation: KNode = child { hasTestTag("BottomSheetCreation") }
  val createNewsButton: KNode = bottomSheetCreation.child { hasTestTag("CreateNewsButton") }
}
