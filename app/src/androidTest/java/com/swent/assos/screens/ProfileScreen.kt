package com.swent.assos.screens

import androidx.compose.ui.test.SemanticsNodeInteractionsProvider
import io.github.kakaocup.compose.node.element.ComposeScreen
import io.github.kakaocup.compose.node.element.KNode

class ProfileScreen(semanticsProvider: SemanticsNodeInteractionsProvider) :
    ComposeScreen<ProfileScreen>(
        semanticsProvider = semanticsProvider,
        viewBuilderAction = { hasTestTag("ProfileScreen") }) {

  val topBar: KNode = child { hasTestTag("TopBar") }
  val settingsButton: KNode = topBar.child { hasTestTag("SettingsButton") }
  val name: KNode = topBar.child { hasTestTag("Name") }
  val contentSection: KNode = child { hasTestTag("ContentSection") }
  val myAssociationSectionTitle: KNode =
      contentSection.child { hasTestTag("MyAssociationSectionTitle") }
  val myAssociationItem: KNode = contentSection.child { hasTestTag("MyAssociationItem") }
  val followedAssociationSectionTitle: KNode =
      contentSection.child { hasTestTag("FollowedAssociationSectionTitle") }
  val followedAssociationItem: KNode = child { hasTestTag("FollowedAssociationItem") }
  val signoutButton: KNode = child { hasTestTag("SignoutButton") }
}
