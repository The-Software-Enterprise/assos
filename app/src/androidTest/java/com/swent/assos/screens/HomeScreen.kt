package com.swent.assos.screens

import androidx.compose.ui.test.SemanticsNodeInteractionsProvider
import androidx.compose.ui.test.hasTestTag
import io.github.kakaocup.compose.node.element.ComposeScreen
import io.github.kakaocup.compose.node.element.KNode

class HomeScreen(semanticsProvider: SemanticsNodeInteractionsProvider) :
    ComposeScreen<HomeScreen>(
        semanticsProvider = semanticsProvider,
        viewBuilderAction = { hasTestTag("HomeNavigation") }) {

  val homeNavigation: KNode = child { hasTestTag("HomeNavigation") }
  val horizontalPager: KNode = homeNavigation.child { hasTestTag("HorizontalPager") }
  val navigationBar: KNode = homeNavigation.child { hasTestTag("NavigationBar") }
  val profileButton: KNode = navigationBar.child { hasTestTag("NavigationBarItem3") }
  val profileScreen: KNode = horizontalPager.child { hasTestTag("ProfileScreen") }
  val profileIcon: KNode = child { hasTestTag("ProfileIcon") }
}
