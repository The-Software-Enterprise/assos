package com.swent.assos.screens

import androidx.compose.ui.test.SemanticsNodeInteractionsProvider
import io.github.kakaocup.compose.node.element.ComposeScreen
import io.github.kakaocup.compose.node.element.KNode

class ApplicationManagementScreen(semanticsProvider: SemanticsNodeInteractionsProvider) :
    ComposeScreen<ApplicationManagementScreen>(
        semanticsProvider = semanticsProvider,
        viewBuilderAction = { hasTestTag("ApplicationManagementScreen") }) {

  val applicationList: KNode = onNode { hasTestTag("ApplicationList") }

  val applicationListItem: KNode = applicationList.child { hasTestTag("ApplicationListItem") }

  val applicationItemRow: KNode = applicationListItem.child { hasTestTag("ApplicationItemRow") }

  val applicationItemName: KNode =
      applicationItemRow.child { hasTestTag("ApplicationListItemFullName") }

  val acceptButton: KNode = applicationItemRow.child { hasTestTag("AcceptApplicationButton") }
}
