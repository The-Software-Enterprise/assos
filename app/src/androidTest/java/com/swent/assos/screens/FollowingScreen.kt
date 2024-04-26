package com.swent.assos.screens

import androidx.compose.ui.test.SemanticsNodeInteractionsProvider
import io.github.kakaocup.compose.node.element.ComposeScreen
import io.github.kakaocup.compose.node.element.KNode

class FollowingScreen(semanticsProvider: SemanticsNodeInteractionsProvider) :
    ComposeScreen<FollowingScreen>(
        semanticsProvider = semanticsProvider,
        viewBuilderAction = { hasTestTag("FollowingScreen") }) {

    val topBar: KNode = child { hasTestTag("TopBar") }
    val goBackButton: KNode = topBar.child { hasTestTag("GoBackButton") }
    val pageTitle: KNode = topBar.child { hasTestTag("PageTitle") }

    val contentSection: KNode = child { hasTestTag("ContentSection") }
    val associationCard: KNode = contentSection.child { hasTestTag("AssociationCard") }
}