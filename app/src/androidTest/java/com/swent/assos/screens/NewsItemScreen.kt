package com.swent.assos.screens

import androidx.compose.ui.test.SemanticsNodeInteractionsProvider
import io.github.kakaocup.compose.node.element.ComposeScreen
import io.github.kakaocup.compose.node.element.KNode

class NewsItemScreen(semanticsProvider: SemanticsNodeInteractionsProvider) :
    ComposeScreen<NewsItemScreen>(
        semanticsProvider = semanticsProvider, viewBuilderAction = { hasTestTag("NewsItem") }) {

  val newsItem: KNode = onNode { hasTestTag("NewsItem") }
  val newsItemImage: KNode = onNode { hasTestTag("NewsItemImage") }
}
