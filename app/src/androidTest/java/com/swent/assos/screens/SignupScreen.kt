package com.swent.assos.screens

import androidx.compose.ui.test.SemanticsNodeInteractionsProvider
import io.github.kakaocup.compose.node.element.ComposeScreen
import io.github.kakaocup.compose.node.element.KNode

class SignupScreen(semanticsProvider: SemanticsNodeInteractionsProvider) :
    ComposeScreen<SignupScreen>(
        semanticsProvider = semanticsProvider, viewBuilderAction = { hasTestTag("SignUpScreen") }) {
  val signupScreen: KNode = onNode { hasTestTag("SignUpScreen") }
  val emailField: KNode = onNode { hasTestTag("EmailField") }
  val passwordField: KNode = onNode { hasTestTag("PasswordField") }
  val confirmPasswordField: KNode = onNode { hasTestTag("ConfirmPasswordField") }
  val signUpButton: KNode = onNode { hasTestTag("SignUpButton") }
  val loginButton: KNode = onNode { hasTestTag("LoginNavButton") }
}
