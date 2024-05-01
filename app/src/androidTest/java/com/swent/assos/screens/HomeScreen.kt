package com.swent.assos.screens

import androidx.compose.ui.test.SemanticsNodeInteractionsProvider
import androidx.compose.ui.test.hasTestTag
import io.github.kakaocup.compose.node.element.ComposeScreen
import io.github.kakaocup.compose.node.element.KNode
import com.swent.assos.model.navigation.HomeNavigation

class HomeScreen(semanticsProvider: SemanticsNodeInteractionsProvider) :
    ComposeScreen<HomeScreen>(
        semanticsProvider = semanticsProvider,
        viewBuilderAction = { hasTestTag("HomeNavigation") }) {

    val homeNavigation: KNode = child { hasTestTag("HomeNavigation") }
    val navigationBar: KNode = homeNavigation.child { hasTestTag("NavigationBar") }
    val profileButton: KNode = navigationBar.child { hasTestTag("NavigationBarItem3") }
    val profileScreen: KNode = homeNavigation.child { hasTestTag("ProfileScreen") }

    }
