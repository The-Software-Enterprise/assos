package com.swent.assos.screens

import androidx.compose.ui.test.SemanticsNodeInteractionsProvider
import androidx.compose.ui.test.hasTestTag
import io.github.kakaocup.compose.node.element.ComposeScreen
import io.github.kakaocup.compose.node.element.KNode

class NewsScreen(semanticsProvider: SemanticsNodeInteractionsProvider) :
    ComposeScreen<NewsScreen>(
        semanticsProvider = semanticsProvider, viewBuilderAction = { hasTestTag("NewsScreen") }) {

  val appTitle1: KNode = child { hasTestTag("AppTitle_1") }
  val appTitle2: KNode = child { hasTestTag("AppTitle_2") }

  val newsList: KNode = child { hasTestTag("NewsList") }
  val newsListItems: KNode = newsList.child { hasTestTag("NewsListItem") }
  val itemsTitle: KNode = newsListItems.child { hasTestTag("ItemsTitle") }
  val itemsDescription: KNode = newsListItems.child { hasTestTag("ItemsDescription") }
  val itemsDate: KNode = newsListItems.child { hasTestTag("ItemsDate") }
  val itemsAssociation: KNode = newsListItems.child { hasTestTag("ItemsAssociation") }
}
