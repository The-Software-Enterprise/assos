package com.swent.assos.screens

import androidx.compose.ui.test.SemanticsNodeInteractionsProvider
import io.github.kakaocup.compose.node.element.ComposeScreen

class NavigationScreen(semanticsProvider: SemanticsNodeInteractionsProvider) :
    ComposeScreen<NavigationScreen>(
        semanticsProvider = semanticsProvider, viewBuilderAction = { hasTestTag("NavHost") }) {}
