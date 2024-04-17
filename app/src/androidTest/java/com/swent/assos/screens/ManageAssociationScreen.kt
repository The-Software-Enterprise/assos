package com.swent.assos.screens

import androidx.compose.ui.test.SemanticsNodeInteractionsProvider
import io.github.kakaocup.compose.node.element.ComposeScreen

class ManageAssociationScreen(semanticsProvider: SemanticsNodeInteractionsProvider) :
    ComposeScreen<CalendarScreen>(
        semanticsProvider = semanticsProvider, viewBuilderAction = { hasTestTag("ManageAssociationScreen") })