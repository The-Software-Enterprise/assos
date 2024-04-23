package com.swent.assos.screens.profile

import androidx.compose.ui.test.SemanticsNodeInteractionsProvider
import com.swent.assos.screens.calendar.CalendarScreen
import io.github.kakaocup.compose.node.element.ComposeScreen

class ProfileScreen(semanticsProvider: SemanticsNodeInteractionsProvider) :
    ComposeScreen<CalendarScreen>(
        semanticsProvider = semanticsProvider, viewBuilderAction = { hasTestTag("ProfileScreen") })