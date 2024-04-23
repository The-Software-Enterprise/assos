package com.swent.assos.screens

import androidx.compose.ui.test.SemanticsNodeInteractionsProvider
import io.github.kakaocup.compose.node.element.ComposeScreen
import io.github.kakaocup.compose.node.element.KNode

class ManageAssoScreen(semanticsProvider: SemanticsNodeInteractionsProvider):
  ComposeScreen<ManageAssoScreen>(
    semanticsProvider = semanticsProvider,
    viewBuilderAction = { hasTestTag("ManageAssoScreen") }) {

    val description: KNode = child { hasTestTag("DescriptionField") }
    val editDescriptionButton: KNode = child { hasTestTag("EditDescriptionButton") }
    val addEventButton: KNode = child { hasTestTag("AddEventButton") }
    val addPostButton: KNode = child { hasTestTag("AddPostButton") }
    val newsItem: KNode = child { hasTestTag("NewsItem") }
    val topBar: KNode = child { hasTestTag("Header") }
    val goBackButton: KNode = topBar.child { hasTestTag("GoBackButton") }


}