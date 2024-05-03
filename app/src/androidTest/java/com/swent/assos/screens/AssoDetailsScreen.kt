package com.swent.assos.screens

import androidx.compose.ui.test.SemanticsNodeInteractionsProvider
import io.github.kakaocup.compose.node.element.ComposeScreen
import io.github.kakaocup.compose.node.element.KNode

class AssoDetailsScreen(semanticsProvider: SemanticsNodeInteractionsProvider) :
    ComposeScreen<AssoDetailsScreen>(
        semanticsProvider = semanticsProvider,
        viewBuilderAction = { hasTestTag("AssoDetailsScreen") }) {

  val header: KNode = onNode { hasTestTag("Header") }
  val goBackButton: KNode = header.child { hasTestTag("GoBackButton") }
  val title: KNode = header.child { hasTestTag("Title") }
  val followButton: KNode = header.child { hasTestTag("FollowButton") }
  val textFollowButton: KNode = followButton.child { hasTestTag("TextFollowButton") }

  val joinButton: KNode = onNode { hasTestTag("JoinButton") }
}
