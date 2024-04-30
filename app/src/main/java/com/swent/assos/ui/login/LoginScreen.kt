package com.swent.assos.ui.login

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.swent.assos.model.navigation.Destinations
import com.swent.assos.model.navigation.NavigationActions
import com.swent.assos.model.view.LoginViewModel

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun LoginScreen(navigationActions: NavigationActions) {

  Column(
      modifier =
          Modifier.fillMaxWidth().semantics { testTagsAsResourceId = true }.testTag("LoginScreen"),
  ) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val loginViewModel: LoginViewModel = hiltViewModel()
    val userNotFound by remember { loginViewModel.userNotFound }
    val errorMessage by remember { loginViewModel.errorMessage }

    Row(
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.Start,
        modifier = Modifier.padding(5.dp, 10.dp, 0.dp, 16.dp)) {
          Text(
              text = "Sign In",
              style =
                  TextStyle(
                      fontSize = 24.sp,
                      lineHeight = 32.sp,
                      color = Color(0xFF1D1B20),
                  ))
        }
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
      OutlinedTextField(
          value = email,
          onValueChange = {
            email = it
            loginViewModel.userNotFound.value = false
          },
          label = { Text("Email") },
          modifier =
              Modifier.padding(start = 16.dp, top = 4.dp, bottom = 4.dp)
                  .fillMaxWidth()
                  .padding(16.dp)
                  .testTag("EmailField"))

      OutlinedTextField(
          value = password,
          onValueChange = {
            password = it
            loginViewModel.userNotFound.value = false
          },
          label = { Text("Password") },
          modifier =
              Modifier.fillMaxWidth()
                  .padding(16.dp)
                  .testTag("PasswordField")
                  .padding(start = 16.dp, top = 4.dp, bottom = 4.dp),
          visualTransformation = PasswordVisualTransformation(),
      )

      Button(
          modifier =
              Modifier.testTag("LoginButton")
                  .shadow(
                      elevation = 3.dp,
                      spotColor = Color(0x4D000000),
                      ambientColor = Color(0x4D000000))
                  .width(100.dp)
                  .height(42.dp)
                  .background(color = Color(0xFF5465FF), shape = RoundedCornerShape(size = 16.dp)),
          colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF5465FF)),
          // allow the child composable to be full sized

          onClick = {
            loginViewModel.signIn(email, password) {
              navigationActions.navigateTo(Destinations.HOME.route)
            }
          },
      ) {
        Text("Sign In", color = Color.White, modifier = Modifier.fillMaxWidth())
      }

      if (userNotFound) {
        Text(modifier = Modifier.testTag("ErrorMessage"), text = errorMessage, color = Color.Red)
      }

      Text(
          // if clicked, go to sign up page using hilt navigation
          modifier =
              Modifier.clickable {
                    loginViewModel.userNotFound.value = false
                    navigationActions.navigateTo(Destinations.SIGN_UP)
                  }
                  .testTag("SignUpNavButton"),
          color = Color.Blue,
          textDecoration = TextDecoration.Underline,
          text = "Don't have an account? Sign up")
    }
  }
}
