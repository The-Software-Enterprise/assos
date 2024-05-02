package com.swent.assos.screens

import androidx.compose.ui.test.SemanticsNodeInteractionsProvider
import io.github.kakaocup.compose.node.element.ComposeScreen
import io.github.kakaocup.compose.node.element.KNode

class CreateNewsScreen(semanticsProvider: SemanticsNodeInteractionsProvider) :
    ComposeScreen<CreateNewsScreen>(
        semanticsProvider = semanticsProvider,
        viewBuilderAction = { hasTestTag("CreateNewsScreen") }) {

  val goBackButton: KNode = onNode { hasTestTag("GoBackButton") }
  val createButton: KNode = onNode { hasTestTag("CreateButton") }
  val pagetile: KNode = onNode { hasTestTag("PageTitle") }

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
  val addImages: KNode = onNode { hasTestTag("AddImages") }
}
