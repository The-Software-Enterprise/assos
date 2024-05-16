package com.swent.assos.screens

import androidx.compose.ui.test.SemanticsNodeInteractionsProvider
import io.github.kakaocup.compose.node.element.ComposeScreen

class NFCReaderScreen(semanticsProvider: SemanticsNodeInteractionsProvider) :
    ComposeScreen<NFCReaderScreen>(
        semanticsProvider = semanticsProvider, viewBuilderAction = { hasTestTag("NFCReader") }) {


}

