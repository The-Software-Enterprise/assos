package com.swent.assos.screens

import androidx.compose.ui.test.SemanticsNodeInteractionsProvider
import androidx.compose.ui.test.hasTestTag
import io.github.kakaocup.compose.node.element.ComposeScreen
import io.github.kakaocup.compose.node.element.KNode

class NewsScreen(semanticsProvider: SemanticsNodeInteractionsProvider) :
    ComposeScreen<NewsScreen>(
        semanticsProvider = semanticsProvider, viewBuilderAction = { hasTestTag("NewsScreen") }) {

  val newsList: KNode = onNode { hasTestTag("NewsList") }
  val newsListItem: KNode = newsList.child { hasTestTag("NewsListItem") }
  val itemsTitle: KNode = newsListItem.child { hasTestTag("ItemsTitle") }
  val itemsDescription: KNode = newsListItem.child { hasTestTag("ItemsDescription") }
}
