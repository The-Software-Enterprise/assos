package com.swent.assos.screens

import androidx.compose.ui.test.SemanticsNodeInteractionsProvider
import io.github.kakaocup.compose.node.element.ComposeScreen

class NFCReadingScreen(semanticsProvider: SemanticsNodeInteractionsProvider) :
    ComposeScreen<NFCReadingScreen>(
        semanticsProvider = semanticsProvider, viewBuilderAction = { hasTestTag("NFCReader") }) {


}

