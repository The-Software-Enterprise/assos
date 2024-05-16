package com.swent.assos.screens

import androidx.compose.ui.test.SemanticsNodeInteractionsProvider
import io.github.kakaocup.compose.node.element.ComposeScreen

class NFCWritingScreen(semanticsProvider: SemanticsNodeInteractionsProvider) :
    ComposeScreen<NFCWritingScreen>(
        semanticsProvider = semanticsProvider, viewBuilderAction = { hasTestTag("NFCWriter") }) {}
