package com.swent.assos.screens

import androidx.compose.ui.test.SemanticsNodeInteractionsProvider
import io.github.kakaocup.compose.node.element.ComposeScreen
import io.github.kakaocup.compose.node.element.KNode

class ReminderScreen(semanticsProvider: SemanticsNodeInteractionsProvider) :
    ComposeScreen<ReminderScreen>(
        semanticsProvider = semanticsProvider,
        viewBuilderAction = { hasTestTag("ReminderScreen") }) {
  val reminder: KNode = child { hasTestTag("Reminder") }
  val description: KNode = child { hasTestTag("Description") }
  val item: KNode = child { hasTestTag("Item") }
  val title: KNode = item.child { hasTestTag("Title") }
}
