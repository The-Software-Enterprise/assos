package com.swent.assos.screens

import androidx.compose.ui.test.SemanticsNodeInteractionsProvider
import io.github.kakaocup.compose.node.element.ComposeScreen
import io.github.kakaocup.compose.node.element.KNode

class FollowingScreen(semanticsProvider: SemanticsNodeInteractionsProvider) :
    ComposeScreen<FollowingScreen>(
        semanticsProvider = semanticsProvider,
        viewBuilderAction = { hasTestTag("FollowingScreen") }) {

  val goBackButton: KNode = onNode { hasTestTag("GoBackButton") }
  val pageTitle: KNode = onNode { hasTestTag("PageTitle") }

  val contentSection: KNode = child { hasTestTag("ContentSection") }
  val associationCard: KNode = contentSection.child { hasTestTag("AssociationCard") }
}
