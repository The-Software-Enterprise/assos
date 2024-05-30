package com.swent.assos.screens

import androidx.compose.ui.test.SemanticsNodeInteractionsProvider
import io.github.kakaocup.compose.node.element.ComposeScreen

class PosDetailsScreen(semanticsProvider: SemanticsNodeInteractionsProvider) :
    ComposeScreen<PosDetailsScreen>(
        semanticsProvider = semanticsProvider, viewBuilderAction = { hasTestTag("PosDetails") }) {}
