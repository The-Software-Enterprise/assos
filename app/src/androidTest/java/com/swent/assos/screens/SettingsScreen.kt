package com.swent.assos.screens

import androidx.compose.ui.test.SemanticsNodeInteractionsProvider
import io.github.kakaocup.compose.node.element.ComposeScreen
import io.github.kakaocup.compose.node.element.KNode

class SettingsScreen(semanticsProvider: SemanticsNodeInteractionsProvider) :
    ComposeScreen<SettingsScreen>(
        semanticsProvider = semanticsProvider,
        viewBuilderAction = { hasTestTag("SettingsScreen") }) {

    val topBar: KNode = child { hasTestTag("TopBar") }
    val goBackButton: KNode = topBar.child { hasTestTag("GoBackButton") }
    val pageTitle: KNode = topBar.child { hasTestTag("PageTitle") }

    val contentSection: KNode = child { hasTestTag("ContentSection") }

    val notificationSettingsButton: KNode = contentSection.child { hasTestTag("Notification settingsButton") }
    val appearanceButton: KNode = contentSection.child { hasTestTag("AppearanceButton") }
}