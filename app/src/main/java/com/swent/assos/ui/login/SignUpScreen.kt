package com.swent.assos.ui.login

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.firebase.functions.FirebaseFunctions
import com.google.firebase.functions.functions
import com.swent.assos.R
import com.swent.assos.config.Config
import com.swent.assos.model.navigation.Destinations
import com.swent.assos.model.navigation.NavigationActions
import com.swent.assos.model.view.LoginViewModel
import org.w3c.dom.Text

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SignUpScreen(navigationActions: NavigationActions) {
  var email by remember { mutableStateOf("") }
  var password by remember { mutableStateOf("") }
  var confirmPassword by remember { mutableStateOf("") }
  val loginViewModel: LoginViewModel = hiltViewModel()
  var error by remember { mutableStateOf("") }
  var passwordTooShort by remember { mutableStateOf(false) }

  Column(
      modifier =
          Modifier.fillMaxWidth().padding(40.dp).testTag("SignUpScreen").semantics {
            testTagsAsResourceId = true
          },
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.Center,
  ) {
    OutlinedTextField(
        value = email,
        onValueChange = { email = it },
        label = { Text("Email") },
        modifier = Modifier.testTag("EmailField"))
    OutlinedTextField(
        value = password,
        onValueChange = {
          passwordTooShort = false
          password = it
        },
        label = { Text("Password") },
        visualTransformation = PasswordVisualTransformation(),
        modifier = Modifier.testTag("PasswordField"))
    OutlinedTextField(
        value = confirmPassword,
        onValueChange = { confirmPassword = it },
        label = { Text("Confirm Password") },
        visualTransformation = PasswordVisualTransformation(),
        modifier = Modifier.testTag("ConfirmPassword"))
    if (password != confirmPassword) {
      Text("Passwords do not match", color = Color.Red)
    }
    Button(
        onClick = {
          if (password == confirmPassword && password.isNotEmpty() && password.length >= 6) {

            loginViewModel.signUp(email, password)

            // call the firebasefunction -> oncallFind.py
            val data = hashMapOf("email" to email)

            val functions = FirebaseFunctions.getInstance("europe-west6")
            val config = Config()

            // change the region of the function to europe-west6
            if (config.get_all().contains("functions")) {
              functions.useEmulator(R.string.emulatorIP.toString(), 5001)
            }
            functions
                .getHttpsCallable("oncallFind")
                .call(data)
                .addOnSuccessListener { task ->
                  // keep result for future use
                  val result = task.data as? Map<String, String>

                  navigationActions.navigateTo(Destinations.HOME)
                }
                .addOnFailureListener { error = it.message.toString() }
          } else if (password.length < 6) {
            passwordTooShort = true
          }
        },
        modifier = Modifier.testTag("SignUpButton")) {
          Text("Sign Up")
        }
    if (passwordTooShort) {
      Text("Password must be at least 6 characters", color = Color.Red)
    }

    Text(
        "Already have an account?",
        modifier =
            Modifier.clickable { navigationActions.navigateTo(Destinations.LOGIN) }
                .testTag("LoginNavButton")
                .clickable { navigationActions.goBack() })
  }
}
