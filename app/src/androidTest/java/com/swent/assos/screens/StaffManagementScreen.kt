package com.swent.assos.screens

import androidx.compose.ui.test.SemanticsNodeInteractionsProvider
import io.github.kakaocup.compose.node.element.ComposeScreen
import io.github.kakaocup.compose.node.element.KNode

class StaffManagementScreen(semanticsProvider: SemanticsNodeInteractionsProvider) :
    ComposeScreen<StaffManagementScreen>(
        semanticsProvider = semanticsProvider,
        viewBuilderAction = { hasTestTag("StaffManagementScreen") }) {

  val staffList: KNode = onNode { hasTestTag("StaffList") }

  val staffItem: KNode = staffList.child { hasTestTag("NameListItem") }
}
