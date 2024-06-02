package com.swent.assos.screens

import androidx.compose.ui.test.SemanticsNodeInteractionsProvider
import androidx.compose.ui.test.hasTestTag
import io.github.kakaocup.compose.node.element.ComposeScreen
import io.github.kakaocup.compose.node.element.KNode

class CommitteeScreen(semanticsProvider: SemanticsNodeInteractionsProvider) :
    ComposeScreen<CommitteeScreen>(
        semanticsProvider = semanticsProvider,
        viewBuilderAction = { hasTestTag("CommitteeScreen") }) {

  val newsList: KNode = onNode { hasTestTag("CommitteeList") }
  val CommitteeListItem: KNode = newsList.child { hasTestTag("NewsListItem") }
  val CommitteeItemColumn: KNode = CommitteeListItem.child { hasTestTag("NewsItemColumn") }
  val CommitteMemeberName: KNode = CommitteeItemColumn.child { hasTestTag("NewsItemsTitle") }
}
