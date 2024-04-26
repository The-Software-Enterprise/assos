package com.swent.assos.screens

import androidx.compose.ui.test.SemanticsNodeInteractionsProvider
import androidx.compose.ui.test.hasTestTag
import io.github.kakaocup.compose.node.element.ComposeScreen
import io.github.kakaocup.compose.node.element.KNode

class SignupScreen(semanticsProvider: SemanticsNodeInteractionsProvider) :
    ComposeScreen<SignupScreen>(
        semanticsProvider = semanticsProvider, viewBuilderAction = { hasTestTag("SignUpScreen") }) {
  val signupScreen: KNode = child { hasTestTag("SignUpScreen") }
  val emailField: KNode = signupScreen.child { hasTestTag("EmailField") }
  val passwordField: KNode = signupScreen.child { hasTestTag("PasswordField") }
  val confirmPasswordField: KNode = signupScreen.child { hasTestTag("ConfirmPasswordField") }
  val signUpButton: KNode = signupScreen.child { hasTestTag("SignUpButton") }
  val loginNavButton: KNode = signupScreen.child { hasTestTag("LoginNavButton") }
}
