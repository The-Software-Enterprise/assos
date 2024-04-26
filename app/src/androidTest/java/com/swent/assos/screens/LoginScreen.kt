package com.swent.assos.screens

import androidx.compose.ui.test.SemanticsNodeInteractionsProvider
import androidx.compose.ui.test.hasTestTag
import io.github.kakaocup.compose.node.element.ComposeScreen
import io.github.kakaocup.compose.node.element.KNode

class LoginScreen(semanticsProvider: SemanticsNodeInteractionsProvider) :
    ComposeScreen<LoginScreen>(
        semanticsProvider = semanticsProvider, viewBuilderAction = { hasTestTag("LoginScreen") }) {
  val loginScreen: KNode = child { hasTestTag("LoginScreen") }
  val emailField: KNode = loginScreen.child { hasTestTag("EmailField") }
  val passwordField: KNode = loginScreen.child { hasTestTag("PasswordField") }
  val loginButton: KNode = loginScreen.child { hasTestTag("LoginButton") }
  val signUpButton: KNode = loginScreen.child { hasTestTag("SignUpNavButton") }
  val errorMessage: KNode = loginScreen.child { hasTestTag("ErrorMessage") }
}
