package com.swent.assos.screens

import androidx.compose.ui.test.SemanticsNodeInteractionsProvider
import io.github.kakaocup.compose.node.element.ComposeScreen
import io.github.kakaocup.compose.node.element.KNode

class CreateEventScreen(semanticsProvider: SemanticsNodeInteractionsProvider) :
    ComposeScreen<CreateEventScreen>(
        semanticsProvider = semanticsProvider, viewBuilderAction = { hasTestTag("CreateEvent") }) {

  val goBackButton: KNode = onNode { hasTestTag("GoBackButton") }
  val pageTitle: KNode = onNode { hasTestTag("PageTitle") }
  val inputTitle: KNode = onNode {
    hasTestTag("InputTitle")
    hasSetTextAction()
  }
  val inputDescription: KNode = onNode {
    hasTestTag("InputDescription")
    hasSetTextAction()
  }
  val image: KNode = onNode { hasTestTag("InputImage") }
  val startTimePicker: KNode = onNode { hasTestTag("StartTimePicker") }
  val endTimePicker: KNode = onNode { hasTestTag("EndTimePicker") }
}
