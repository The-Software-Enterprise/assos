package com.swent.assos.ui.login

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
          Modifier.fillMaxWidth()
              .semantics { testTagsAsResourceId = true }
              .testTag("LoginScreen")
              .background(color = MaterialTheme.colorScheme.primaryContainer)
              .fillMaxHeight(),
  ) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val loginViewModel: LoginViewModel = hiltViewModel()
    val userNotFound by remember { loginViewModel.userNotFound }
    val errorMessage by remember { loginViewModel.errorMessage }

    Row(
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.Start,
        modifier = Modifier.fillMaxWidth()) {
          Text(
              modifier = Modifier.padding(horizontal = 20.dp, vertical = 32.dp),
              text = "Sign In",
              style =
                  TextStyle(
                      fontSize = 30.sp,
                      lineHeight = 32.sp,
                      fontFamily = FontFamily(Font(R.font.sf_pro_display_regular)),
                      fontWeight = FontWeight.SemiBold,
                      color = MaterialTheme.colorScheme.onSurface))
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
          label = {
            Text(
                "Email",
                style =
                    TextStyle(
                        fontSize = 16.sp,
                        lineHeight = 32.sp,
                        fontFamily = FontFamily(Font(R.font.sf_pro_display_regular)),
                        color = MaterialTheme.colorScheme.onSurface))
          },
          modifier = Modifier.padding(horizontal = 16.dp).fillMaxWidth().testTag("EmailField"))

      Spacer(modifier = Modifier.height(16.dp))

      OutlinedTextField(
          value = password,
          onValueChange = {
            password = it
            loginViewModel.userNotFound.value = false
          },
          label = {
            Text(
                "Password",
                style =
                    TextStyle(
                        fontSize = 16.sp,
                        lineHeight = 32.sp,
                        fontFamily = FontFamily(Font(R.font.sf_pro_display_regular)),
                        color = MaterialTheme.colorScheme.onSurface))
          },
          modifier = Modifier.fillMaxWidth().testTag("PasswordField").padding(horizontal = 16.dp),
          visualTransformation = PasswordVisualTransformation(),
      )
      Spacer(modifier = Modifier.height(32.dp))

      Button(
          modifier =
              Modifier.testTag("LoginButton")
                  .shadow(
                      elevation = 3.dp,
                      spotColor = MaterialTheme.colorScheme.onSurface,
                      ambientColor = MaterialTheme.colorScheme.onSurface)
                  .width(100.dp)
                  .height(42.dp)
                  .background(
                      color = MaterialTheme.colorScheme.primary,
                      shape = MaterialTheme.shapes.small),
          colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
          // allow the child composable to be full sized

          onClick = {
            loginViewModel.signIn(email, password) {
              navigationActions.navigateTo(Destinations.HOME.route)
            }
          },
      ) {
        Text(
            "Sign In",
            color = MaterialTheme.colorScheme.onPrimary,
            modifier = Modifier.fillMaxWidth(),
            style =
                TextStyle(
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    lineHeight = 32.sp,
                    fontFamily = FontFamily(Font(R.font.sf_pro_display_regular)),
                    color = MaterialTheme.colorScheme.onSurface))
      }

      if (userNotFound) {

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            modifier = Modifier.testTag("ErrorMessage"),
            text = errorMessage,
            color = MaterialTheme.colorScheme.error,
            style =
                TextStyle(
                    fontSize = 16.sp,
                    lineHeight = 32.sp,
                    fontFamily = FontFamily(Font(R.font.sf_pro_display_regular))))
      }

      Spacer(modifier = Modifier.height(16.dp))

      Text(
          // if clicked, go to sign up page using hilt navigation
          modifier =
              Modifier.clickable {
                    loginViewModel.userNotFound.value = false
                    navigationActions.navigateTo(Destinations.SIGN_UP)
                  }
                  .testTag("SignUpNavButton"),
          color = MaterialTheme.colorScheme.primary,
          textDecoration = TextDecoration.Underline,
          text = "Don't have an account? Sign up",
          style =
              TextStyle(
                  fontSize = 16.sp,
                  lineHeight = 32.sp,
                  fontFamily = FontFamily(Font(R.font.sf_pro_display_regular)),
              ))
    }
  }
}
