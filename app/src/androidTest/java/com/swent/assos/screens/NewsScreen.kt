package com.swent.assos.screens

import androidx.compose.ui.test.SemanticsNodeInteractionsProvider
import io.github.kakaocup.compose.node.element.ComposeScreen
import io.github.kakaocup.compose.node.element.KNode

class NewsScreen(semanticsProvider: SemanticsNodeInteractionsProvider) :
    ComposeScreen<NewsScreen>(
        semanticsProvider = semanticsProvider, viewBuilderAction = { hasTestTag("NewsScreen") }) {

  val appTitle1: KNode = child { hasTestTag("AppTitle_1") }
  val appTitle2: KNode = child { hasTestTag("AppTitle_2") }

  val newsList: KNode = child { hasTestTag("NewsList") }
  val newsListItems1: KNode = newsList.child { hasTestTag("NewsListItem1") }
  val newsListItems2: KNode = newsListItems1.child { hasTestTag("NewsListItem2") }
  val itemsTitle: KNode = newsListItems2.child { hasTestTag("ItemsTitle") }
  val itemsDescription: KNode = newsListItems2.child { hasTestTag("ItemsDescription") }
}
