package com.swent.assos.screens

import androidx.compose.ui.test.SemanticsNodeInteractionsProvider
import io.github.kakaocup.compose.node.element.ComposeScreen
import io.github.kakaocup.compose.node.element.KNode

class NewsDetailsScreen(semanticsProvider: SemanticsNodeInteractionsProvider) :
    ComposeScreen<NewsDetailsScreen>(
        semanticsProvider = semanticsProvider,
        viewBuilderAction = { hasTestTag("NewsDetailsScreen") }) {

  val header: KNode = onNode { hasTestTag("Header") }
  val goBackButton: KNode = header.child { hasTestTag("GoBackButton") }
  val title: KNode = header.child { hasTestTag("Title") }
  val body: KNode = onNode { hasTestTag("NewsDetailsScreen") }
  val content: KNode = body.child { hasTestTag("Content") }
  val mainImage: KNode = content.child { hasTestTag("Main Image") }
  val description: KNode = content.child { hasTestTag("description") }
  val subImageList: KNode = content.child { hasTestTag("subImageList") }
  val subImage: KNode = subImageList.child { hasTestTag("subImage") }
}
