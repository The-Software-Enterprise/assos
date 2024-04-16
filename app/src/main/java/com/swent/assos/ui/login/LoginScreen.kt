package com.swent.assos.ui.login

import android.util.Log
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
      horizontalAlignment = Alignment.CenterHorizontally
  ) {
        var email by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }
        val loginViewModel: LoginViewModel = hiltViewModel()
        var userNotFound by remember { mutableStateOf(false) }
        var errorMessage by remember { mutableStateOf("") }

        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "Logo",
            modifier = Modifier.size(200.dp).padding(16.dp))

        OutlinedTextField(
            value = email,
            onValueChange = {
                userNotFound = false
                email = it
                            },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth().padding(16.dp))

        OutlinedTextField(
            value = password,
            onValueChange = {
                userNotFound = false
                password = it
                            },
            label = { Text("Password") },
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            visualTransformation = PasswordVisualTransformation())

        Button(
            onClick = {
                if (email.isEmpty() || password.isEmpty()) {
                    userNotFound = true
                    errorMessage = "Please fill in all fields"
                } else {
                    loginViewModel.signIn(email, password) { e ->
                        Log.d("LoginScreen", "User: ${loginViewModel.user}")
                        if (loginViewModel.user != User("", "", "", "", emptyList(), emptyList())) {
                            navigationActions.navigateTo(Destinations.HOME.route)
                        } else {
                            userNotFound = true
                            errorMessage = e?.message ?: "User not found"
                        }
                    }
                }
            }
        ) {
          Text("Login")
        }

        if (userNotFound) {
          Text(
              text = errorMessage,
              color = Color.Red
          )
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
