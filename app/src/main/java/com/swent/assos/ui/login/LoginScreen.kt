package com.swent.assos.ui.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.swent.assos.R
import com.swent.assos.model.navigation.Destinations
import com.swent.assos.model.navigation.NavigationActions
import com.swent.assos.model.view.LoginViewModel

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun LoginScreen(navigationActions: NavigationActions) {
  Column(
      modifier =
          Modifier.fillMaxWidth().semantics { testTagsAsResourceId = true }.testTag("LoginScreen"),
      verticalArrangement = Arrangement.Center,
      horizontalAlignment = Alignment.CenterHorizontally) {
        var email by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }
        val loginViewModel: LoginViewModel = hiltViewModel()
        val userNotFound by remember { loginViewModel.userNotFound }
        val errorMessage by remember { loginViewModel.errorMessage }

        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "Logo",
            modifier = Modifier.size(200.dp).padding(16.dp))

        OutlinedTextField(
            value = email,
            onValueChange = {
              email = it
              loginViewModel.userNotFound.value = false
            },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth().padding(16.dp).testTag("EmailField"))

        OutlinedTextField(
            value = password,
            onValueChange = {
              password = it
              loginViewModel.userNotFound.value = false
            },
            label = { Text("Password") },
            modifier = Modifier.fillMaxWidth().padding(16.dp).testTag("PasswordField"),
            visualTransformation = PasswordVisualTransformation())

        Button(
            modifier = Modifier.testTag("LoginButton"),
            onClick = {
              loginViewModel.signIn(email, password) {
                navigationActions.navigateTo(Destinations.HOME.route)
              }
            },
        ) {
          Text("Login")
        }

        if (userNotFound) {
          Text(modifier = Modifier.testTag("ErrorMessage"), text = errorMessage, color = Color.Red)
        }

        Text(
            // if clicked, go to sign up page using hilt navigation
            modifier =
                Modifier.clickable { navigationActions.navigateTo(Destinations.SIGN_UP) }
                    .testTag("SignUpNavButton"),
            color = Color.Blue,
            textDecoration = TextDecoration.Underline,
            text = "Don't have an account? Sign up")
      }
}
