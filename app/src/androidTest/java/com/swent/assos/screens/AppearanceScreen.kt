package com.swent.assos.screens


import androidx.compose.ui.test.SemanticsNodeInteractionsProvider
import io.github.kakaocup.compose.node.element.ComposeScreen
import io.github.kakaocup.compose.node.element.KNode

class AppearanceScreen(semanticsProvider: SemanticsNodeInteractionsProvider) :
    ComposeScreen<AppearanceScreen>(
        semanticsProvider = semanticsProvider,
        viewBuilderAction = { hasTestTag("AppearanceScreen") }) {

    val topBar: KNode = child { hasTestTag("TopBar") }
    val goBackButton: KNode = topBar.child { hasTestTag("GoBackButton") }
    val pageTitle: KNode = topBar.child { hasTestTag("PageTitle") }

    val contentSection: KNode = child { hasTestTag("ContentSection") }
}
