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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.swent.assos.R
import com.swent.assos.model.data.User
import com.swent.assos.model.navigation.Destinations
import com.swent.assos.model.navigation.NavigationActions
import com.swent.assos.model.view.LoginViewModel

@Composable
fun LoginScreen(navigationActions: NavigationActions) {
  Column(
      modifier = Modifier.fillMaxWidth(),
      verticalArrangement = Arrangement.Center,
      horizontalAlignment = Alignment.CenterHorizontally) {
        var email by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }
        val loginViewModel: LoginViewModel = hiltViewModel()
        var userNotFound by remember { mutableStateOf(false) }

        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "Logo",
            modifier = Modifier.size(200.dp).padding(16.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth().padding(16.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            visualTransformation = PasswordVisualTransformation())

        Button(
            onClick = {
              loginViewModel.signIn(email, password)
              if (loginViewModel.user != User("", "", "", "", emptyList(), emptyList())) {
                navigationActions.navigateTo(Destinations.HOME.route)
              } else {
                userNotFound = true
              }
            },
        ) {
          Text("Login")
        }

        if (userNotFound) {
          Text("User not found, please sign up", color = Color.Red)
        }

        Text(
            // if clicked, go to sign up page using hilt navigation
            modifier =
                Modifier.clickable { navigationActions.navigateTo(Destinations.SIGN_UP.route) },
            color = Color.Blue,
            textDecoration = TextDecoration.Underline,
            text = "Don't have an account? Sign up")
      }
}
