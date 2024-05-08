package com.swent.assos.screens

import androidx.compose.ui.test.SemanticsNodeInteractionsProvider
import io.github.kakaocup.compose.node.element.ComposeScreen
import io.github.kakaocup.compose.node.element.KNode

class NewsDetailsScreen(semanticsProvider: SemanticsNodeInteractionsProvider) :
    ComposeScreen<NewsDetailsScreen>(
        semanticsProvider = semanticsProvider,
        viewBuilderAction = { hasTestTag("NewsDetailsScreen") }) {

  val goBackButton: KNode = onNode { hasTestTag("GoBackButton") }
  val pageTitle: KNode = onNode { hasTestTag("PageTitle") }

  val content: KNode = child { hasTestTag("Content") }
  val mainImage: KNode = content.child { hasTestTag("Main Image") }
  val descriptionBox: KNode = content.child { hasTestTag("descriptionBox") }
  val descriptionText: KNode = descriptionBox.child { hasTestTag("descriptionText") }
  val subImageList: KNode = content.child { hasTestTag("subImageList") }
  val subImageBox: KNode = subImageList.child { hasTestTag("subImageBox") }
  val subImage: KNode = subImageBox.child { hasTestTag("subImage") }
}
