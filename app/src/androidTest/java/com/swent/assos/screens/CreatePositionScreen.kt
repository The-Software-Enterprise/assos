package com.swent.assos.screens

import androidx.compose.ui.test.SemanticsNodeInteractionsProvider
import io.github.kakaocup.compose.node.element.ComposeScreen
import io.github.kakaocup.compose.node.element.KNode

class CreatePositionScreen(semanticsProvider: SemanticsNodeInteractionsProvider) :
    ComposeScreen<CreatePositionScreen>(
        semanticsProvider = semanticsProvider,
        viewBuilderAction = { hasTestTag("CreatePosScreen") }) {

  val goBackButton: KNode = onNode { hasTestTag("GoBackButton") }
}
