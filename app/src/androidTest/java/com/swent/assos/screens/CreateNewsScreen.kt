package com.swent.assos.screens

import androidx.compose.ui.test.SemanticsNodeInteractionsProvider
import io.github.kakaocup.compose.node.element.ComposeScreen
import io.github.kakaocup.compose.node.element.KNode

class CreateNewsScreen(semanticsProvider: SemanticsNodeInteractionsProvider) :
    ComposeScreen<CreateNewsScreen>(
        semanticsProvider = semanticsProvider,
        viewBuilderAction = { hasTestTag("CreateNewsScreen") }) {

  val form: KNode = child { hasTestTag("Form") }
  val inputTitle: KNode = form.child { hasTestTag("InputTitle"); hasSetTextAction() }
  val inputDescription: KNode = form.child { hasTestTag("InputDescription"); hasSetTextAction() }
  val buttonSave: KNode = form.child { hasTestTag("ButtonSave") }
}
