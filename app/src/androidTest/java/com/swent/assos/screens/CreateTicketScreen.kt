package com.swent.assos.screens

import androidx.compose.ui.test.SemanticsNodeInteractionsProvider
import androidx.compose.ui.test.hasTestTag
import io.github.kakaocup.compose.node.element.ComposeScreen
import io.github.kakaocup.compose.node.element.KNode

class CreateTicketScreen(semanticsProvider: SemanticsNodeInteractionsProvider) :
    ComposeScreen<CreateTicketScreen>(
        semanticsProvider = semanticsProvider,
        viewBuilderAction = { hasTestTag("CreateTicketScreen") }) {

  val goBackButton: KNode = onNode { hasTestTag("GoBackButton") }
  val pageTitle: KNode = onNode { hasTestTag("PageTitle") }
  val form: KNode = onNode { hasTestTag("Form") }
  val emailField: KNode = form.child { hasTestTag("EmailField") }
  val submitButton: KNode = form.child { hasTestTag("SubmitButton") }
}
