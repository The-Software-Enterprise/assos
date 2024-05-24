package com.swent.assos.screens

import androidx.compose.ui.test.SemanticsNodeInteractionsProvider
import io.github.kakaocup.compose.node.element.ComposeScreen

class ApplicationsScreen(semanticsProvider: SemanticsNodeInteractionsProvider) :
    ComposeScreen<ApplicationsScreen>(
        semanticsProvider = semanticsProvider,
        viewBuilderAction = { hasTestTag("ApplicationsScreen") }) {

  val associationsApplicationsTitle = onNode { hasTestTag("AssociationsApplicationsTitle") }
  val staffingApplicationsTitle = onNode { hasTestTag("StaffingApplicationsTitle") }
}
