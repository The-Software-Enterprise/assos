package com.swent.assos.screens

import androidx.compose.ui.test.SemanticsNodeInteractionsProvider
import io.github.kakaocup.compose.node.element.ComposeScreen
import io.github.kakaocup.compose.node.element.KNode

class CreateEventScreen(semanticsProvider: SemanticsNodeInteractionsProvider) :
    ComposeScreen<CreateEventScreen>(
        semanticsProvider = semanticsProvider, viewBuilderAction = { hasTestTag("CreateEvent") }) {

  val goBackButton: KNode = onNode { hasTestTag("GoBackButton") }
  val form: KNode = child { hasTestTag("Form") }
  val inputTitle: KNode =
      form.child {
        hasTestTag("InputTitle")
        hasSetTextAction()
      }
  val inputDescription: KNode =
      form.child {
        hasTestTag("InputDescription")
        hasSetTextAction()
      }
  val inputDate: KNode =
      form.child {
        hasTestTag("InputDate")
        hasSetTextAction()
      }
  val buttonSave: KNode = form.child { hasTestTag("ButtonSave") }
}
