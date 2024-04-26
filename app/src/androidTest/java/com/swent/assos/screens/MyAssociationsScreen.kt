package com.swent.assos.screens

import androidx.compose.ui.test.SemanticsNodeInteractionsProvider
import io.github.kakaocup.compose.node.element.ComposeScreen
import io.github.kakaocup.compose.node.element.KNode

class MyAssociationsScreen(semanticsProvider: SemanticsNodeInteractionsProvider) :
    ComposeScreen<MyAssociationsScreen>(
        semanticsProvider = semanticsProvider,
        viewBuilderAction = { hasTestTag("MyAssociationsScreen") }) {

    val topBar: KNode = child { hasTestTag("TopBar") }
    val goBackButton: KNode = topBar.child { hasTestTag("GoBackButton") }
    val pageTitle: KNode = topBar.child { hasTestTag("PageTitle") }

    val contentSection: KNode = child { hasTestTag("ContentSection") }
    val associationCard: KNode = contentSection.child { hasTestTag("AssociationCard") }
}