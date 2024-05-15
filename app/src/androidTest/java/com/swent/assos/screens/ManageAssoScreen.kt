package com.swent.assos.screens

import androidx.compose.ui.test.SemanticsNodeInteractionsProvider
import io.github.kakaocup.compose.node.element.ComposeScreen
import io.github.kakaocup.compose.node.element.KNode

class ManageAssoScreen(semanticsProvider: SemanticsNodeInteractionsProvider) :
    ComposeScreen<ManageAssoScreen>(
        semanticsProvider = semanticsProvider,
        viewBuilderAction = { hasTestTag("ManageAssoScreen") }) {

  val content: KNode = child { hasTestTag("Content") }
  val description: KNode = content.child { hasTestTag("DescriptionField") }
  val editDescriptionButton: KNode = content.child { hasTestTag("EditDescriptionButton") }
  val addEventButton: KNode = content.child { hasTestTag("AddEventButton") }
  val addPostButton: KNode = content.child { hasTestTag("AddPostButton") }
  val newsItem: KNode = content.child { hasTestTag("NewsItem") }
    val applicationsButton: KNode = content.child { hasTestTag("ApplicationsButton") }

  val topBar: KNode = child { hasTestTag("Header") }
  val goBackButton: KNode = topBar.child { hasTestTag("GoBackButton") }
}
