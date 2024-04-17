package com.swent.assos.screens

import androidx.compose.ui.test.SemanticsNodeInteractionsProvider
import io.github.kakaocup.compose.node.element.ComposeScreen
import io.github.kakaocup.compose.node.element.KNode

class LoginScreen(semanticsProvider: SemanticsNodeInteractionsProvider) :
    ComposeScreen<LoginScreen>(
        semanticsProvider = semanticsProvider, viewBuilderAction = { hasTestTag("LoginScreen") }) {
  val loginScreen: KNode = onNode { hasTestTag("LoginScreen") }
  val emailField: KNode = onNode { hasTestTag("EmailField") }
  val passwordField: KNode = onNode { hasTestTag("PasswordField") }
  val loginButton: KNode = onNode { hasTestTag("LoginButton") }
  val signUpButton: KNode = onNode { hasTestTag("SignUpNavButton") }
  val errorMessage: KNode = onNode { hasTestTag("ErrorMessage") }
}
