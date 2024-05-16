package com.swent.assos.screens

import androidx.compose.ui.test.SemanticsNodeInteractionsProvider
import io.github.kakaocup.compose.node.element.ComposeScreen

class NFCWriterScreen(semanticsProvider: SemanticsNodeInteractionsProvider) :
    ComposeScreen<NFCWriterScreen>(
        semanticsProvider = semanticsProvider, viewBuilderAction = { hasTestTag("NFCWriter") }) {}
