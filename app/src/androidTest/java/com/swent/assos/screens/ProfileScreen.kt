package com.swent.assos.screens

import androidx.compose.ui.test.SemanticsNodeInteractionsProvider
import io.github.kakaocup.compose.node.element.ComposeScreen
import io.github.kakaocup.compose.node.element.KNode

class ProfileScreen(semanticsProvider: SemanticsNodeInteractionsProvider) :
    ComposeScreen<ProfileScreen>(
        semanticsProvider = semanticsProvider,
        viewBuilderAction = { hasTestTag("ProfileScreen") }) {

  val topBar: KNode = child { hasTestTag("TopBar") }
  val pagetile: KNode = topBar.child { hasTestTag("PageTitle") }

  val contentSection: KNode = child { hasTestTag("ContentSection") }

  val name: KNode = contentSection.child { hasTestTag("Name") }
  val myAssociationsButton: KNode = contentSection.child { hasTestTag("My AssociationsButton") }
  val followingAssociationsButton: KNode =
      contentSection.child { hasTestTag("Following AssociationsButton") }
  val settingsButton: KNode = contentSection.child { hasTestTag("SettingsButton") }
  val logoutButton: KNode = contentSection.child { hasTestTag("Log OutButton") }

  val logoutTitle: KNode = child { hasTestTag("LogoutTitle") }
  val logoutText: KNode = child { hasTestTag("LogoutText") }
  val logoutConfirmButton: KNode = child { hasTestTag("LogoutConfirmButton") }
  val logoutCancelButton: KNode = child { hasTestTag("LogoutCancelButton") }
}
