package com.swent.assos.screens

import androidx.compose.ui.test.SemanticsNodeInteractionsProvider
import io.github.kakaocup.compose.node.element.ComposeScreen
import io.github.kakaocup.compose.node.element.KNode

class CalendarScreen(semanticsProvider: SemanticsNodeInteractionsProvider) :
    ComposeScreen<CalendarScreen>(
        semanticsProvider = semanticsProvider,
        viewBuilderAction = { hasTestTag("CalendarScreen") }) {

  val topBarTitle: KNode = onNode { hasTestTag("PageTitle") }

  val daysList: KNode = child { hasTestTag("DaysList") }
  val dayItem: KNode = daysList.child { hasTestTag("DayItem") }
  val dayItemSelected: KNode = daysList.child { hasTestTag("DayItemSelected") }
  val titleItemSelected: KNode = dayItemSelected.child { hasTestTag("TitleItemSelected") }

  val eventUI: KNode = child { hasTestTag("EventUI") }
  val eventItem: KNode = eventUI.child { hasTestTag("EventItem") }
}
