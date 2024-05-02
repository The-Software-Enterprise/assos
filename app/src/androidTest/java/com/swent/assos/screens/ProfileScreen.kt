package com.swent.assos.screens

import androidx.compose.ui.test.SemanticsNodeInteractionsProvider
import io.github.kakaocup.compose.node.element.ComposeScreen
import io.github.kakaocup.compose.node.element.KNode

class ProfileScreen(semanticsProvider: SemanticsNodeInteractionsProvider) :
    ComposeScreen<ProfileScreen>(
        semanticsProvider = semanticsProvider,
        viewBuilderAction = { hasTestTag("ProfileScreen") }) {

  val pagetile: KNode = onNode { hasTestTag("PageTitle") }

  val contentSection: KNode = child { hasTestTag("ContentSection") }

  val name: KNode = contentSection.child { hasTestTag("Name") }
  val myAssociationsButton: KNode = contentSection.child { hasTestTag("My AssociationsButton") }
  val followingAssociationsButton: KNode =
      contentSection.child { hasTestTag("Following AssociationsButton") }
  val settingsButton: KNode = contentSection.child { hasTestTag("SettingsButton") }
  val logoutButton: KNode = contentSection.child { hasTestTag("Log OutButton") }

  val logoutDialog: KNode = contentSection.child { hasTestTag("LogoutDialog") }

  val logoutTitle: KNode = child { hasTestTag("LogoutTitle") }
  val logoutText: KNode = child { hasTestTag("LogoutText") }
  val logoutConfirmButton: KNode = logoutDialog.child { hasTestTag("LogoutConfirmButton") }
  val logoutCancelButton: KNode = logoutDialog.child { hasTestTag("LogoutCancelButton") }
}
