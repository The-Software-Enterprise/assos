package com.swent.assos.screens

import androidx.compose.ui.test.SemanticsNodeInteractionsProvider
import io.github.kakaocup.compose.node.element.ComposeScreen
import io.github.kakaocup.compose.node.element.KNode

class NotificationSettingsScreen(semanticsProvider: SemanticsNodeInteractionsProvider) :
    ComposeScreen<NotificationSettingsScreen>(
        semanticsProvider = semanticsProvider,
        viewBuilderAction = { hasTestTag("NotificationSettingsScreen") }) {

    val topBar: KNode = child { hasTestTag("TopBar") }
    val goBackButton: KNode = topBar.child { hasTestTag("GoBackButton") }
    val pageTitle: KNode = topBar.child { hasTestTag("PageTitle") }

    val contentSection: KNode = child { hasTestTag("ContentSection") }

}