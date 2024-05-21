package com.swent.assos.screens

import androidx.compose.ui.test.SemanticsNodeInteractionsProvider
import androidx.compose.ui.test.hasTestTag
import io.github.kakaocup.compose.node.element.ComposeScreen
import io.github.kakaocup.compose.node.element.KNode

class NewsScreen(semanticsProvider: SemanticsNodeInteractionsProvider) :
    ComposeScreen<NewsScreen>(
        semanticsProvider = semanticsProvider, viewBuilderAction = { hasTestTag("NewsScreen") }) {

  val newsList: KNode = onNode { hasTestTag("NewsList") }
  val followActivityTitle: KNode = newsList.child { hasTestTag("FollowActivityTitle") }
  val allActivityTitle: KNode = newsList.child { hasTestTag("AllActivityTitle") }
  val newsListItem: KNode = newsList.child { hasTestTag("NewsListItem") }
  val newsItemRow: KNode = onNode { hasTestTag("NewsItemRow") }
  val newsItemColumn: KNode = newsItemRow.child { hasTestTag("NewsItemColumn") }
  val newsItemTitle: KNode = newsItemColumn.child { hasTestTag("NewsItemsTitle") }
  val newsItemDescription: KNode = newsItemColumn.child { hasTestTag("NewsItemsDescription") }
}
