package com.swent.assos.ui.screens.login

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
import androidx.compose.material3.OutlinedTextFieldDefaults
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
fun SignUpScreen(navigationActions: NavigationActions) {
  var email by remember { mutableStateOf("") }
  var password by remember { mutableStateOf("") }
  var confirmPassword by remember { mutableStateOf("") }
  val loginViewModel: LoginViewModel = hiltViewModel()
  val badCredentials by remember { loginViewModel.badCredentials }
  val responseError by remember { loginViewModel.responseError }
  val firebaseError by remember { loginViewModel.firebaseError }

  Column(
      modifier =
          Modifier.background(color = MaterialTheme.colorScheme.primaryContainer)
              .padding(bottom = 24.dp)
              .fillMaxHeight()
              .fillMaxWidth()
              .testTag("SignUpScreen")
              .semantics { testTagsAsResourceId = true },
  ) {
    Row(
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.Start,
        modifier = Modifier.fillMaxWidth()) {
          Text(
              modifier = Modifier.padding(horizontal = 20.dp, vertical = 32.dp),
              text = "Sign Up",
              style =
                  TextStyle(
                      fontSize = 30.sp,
                      lineHeight = 32.sp,
                      fontFamily = FontFamily(Font(R.font.sf_pro_display_regular)),
                      fontWeight = FontWeight.SemiBold,
                      color = MaterialTheme.colorScheme.onBackground))
        }
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
      OutlinedTextField(
          singleLine = true,
          value = email,
          onValueChange = {
            email = it
            loginViewModel.badCredentials.value = false
            loginViewModel.firebaseError.value = false
          },
          textStyle =
              TextStyle(
                  fontSize = 16.sp,
                  lineHeight = 32.sp,
                  fontFamily = FontFamily(Font(R.font.sf_pro_display_regular)),
                  color = MaterialTheme.colorScheme.onBackground),
          colors =
              OutlinedTextFieldDefaults.colors(
                  focusedBorderColor = MaterialTheme.colorScheme.secondary,
                  focusedLabelColor = MaterialTheme.colorScheme.secondary,
                  cursorColor = MaterialTheme.colorScheme.secondary),
          label = {
            Text(
                "Email",
                style =
                    TextStyle(
                        fontSize = 16.sp,
                        lineHeight = 32.sp,
                        fontFamily = FontFamily(Font(R.font.sf_pro_display_regular)),
                        color = MaterialTheme.colorScheme.onBackground),
            )
          },
          modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp).testTag("EmailField"))

      Spacer(modifier = Modifier.height(16.dp))

      OutlinedTextField(
          value = password,
          singleLine = true,
          onValueChange = {
            loginViewModel.badCredentials.value = false
            loginViewModel.firebaseError.value = false
            password = it
          },
          textStyle =
              TextStyle(
                  fontSize = 16.sp,
                  lineHeight = 32.sp,
                  fontFamily = FontFamily(Font(R.font.sf_pro_display_regular)),
                  color = MaterialTheme.colorScheme.onBackground),
          colors =
              OutlinedTextFieldDefaults.colors(
                  focusedBorderColor = MaterialTheme.colorScheme.secondary,
                  focusedLabelColor = MaterialTheme.colorScheme.secondary,
                  cursorColor = MaterialTheme.colorScheme.secondary),
          label = {
            Text(
                "Password",
                style =
                    TextStyle(
                        fontSize = 16.sp,
                        lineHeight = 32.sp,
                        fontFamily = FontFamily(Font(R.font.sf_pro_display_regular)),
                        color = MaterialTheme.colorScheme.onBackground))
          },
          visualTransformation = PasswordVisualTransformation(),
          modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp).testTag("PasswordField"))
      Spacer(modifier = Modifier.height(16.dp))

      OutlinedTextField(
          value = confirmPassword,
          singleLine = true,
          onValueChange = {
            confirmPassword = it
            loginViewModel.badCredentials.value = false
            loginViewModel.firebaseError.value = false
          },
          textStyle =
              TextStyle(
                  fontSize = 16.sp,
                  lineHeight = 32.sp,
                  fontFamily = FontFamily(Font(R.font.sf_pro_display_regular)),
                  color = MaterialTheme.colorScheme.onBackground),
          colors =
              OutlinedTextFieldDefaults.colors(
                  focusedBorderColor = MaterialTheme.colorScheme.secondary,
                  focusedLabelColor = MaterialTheme.colorScheme.secondary,
                  cursorColor = MaterialTheme.colorScheme.secondary),
          label = {
            Text(
                "Confirm Password",
                style =
                    TextStyle(
                        fontSize = 16.sp,
                        lineHeight = 32.sp,
                        fontFamily = FontFamily(Font(R.font.sf_pro_display_regular)),
                        color = MaterialTheme.colorScheme.onBackground))
          },
          visualTransformation = PasswordVisualTransformation(),
          modifier =
              Modifier.fillMaxWidth().padding(horizontal = 16.dp).testTag("ConfirmPasswordField"))
      if (password != confirmPassword) {
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            "Passwords do not match",
            color = MaterialTheme.colorScheme.error,
            style =
                TextStyle(
                    fontSize = 16.sp,
                    lineHeight = 32.sp,
                    fontFamily = FontFamily(Font(R.font.sf_pro_display_regular)),
                ))
      }
      Spacer(modifier = Modifier.height(32.dp))

      Button(
          onClick = {
            loginViewModel.signUp(email, password, confirmPassword) { success ->
              if (!success) {
                return@signUp
              } else {
                try {
                  navigationActions.navigateTo(Destinations.HOME.route)
                } catch (e: Exception) {
                  throw e
                }
              }
            }
          },
          modifier =
              Modifier.testTag("SignUpButton")
                  .shadow(
                      elevation = 3.dp,
                      spotColor = MaterialTheme.colorScheme.onSurface,
                      ambientColor = MaterialTheme.colorScheme.onSurface)
                  .width(105.dp)
                  .height(42.dp)
                  .background(
                      color = MaterialTheme.colorScheme.secondary,
                      shape = MaterialTheme.shapes.small),
          colors =
              ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary),
          // allow the child composable to be full sized
      ) {
        Text(
            "Sign Up",
            style =
                TextStyle(
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    lineHeight = 32.sp,
                    fontFamily = FontFamily(Font(R.font.sf_pro_display_regular)),
                    color = MaterialTheme.colorScheme.onPrimary))
      }
      when (firebaseError) {
        true -> {
          Spacer(modifier = Modifier.height(16.dp))
          Text(
              text = responseError,
              color = MaterialTheme.colorScheme.error,
              modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
              style =
                  TextStyle(
                      fontSize = 16.sp,
                      lineHeight = 32.sp,
                      fontFamily = FontFamily(Font(R.font.sf_pro_display_regular))))
        }
        false -> {}
      }

      when (badCredentials) {
        true -> {
          Spacer(modifier = Modifier.height(16.dp))
          Text(
              text = "Password must be at least 6 characters and email must be filled",
              color = MaterialTheme.colorScheme.error,
              modifier =
                  Modifier.fillMaxWidth()
                      .padding(horizontal = 32.dp)
                      .align(Alignment.CenterHorizontally),
              style =
                  TextStyle(
                      fontSize = 16.sp,
                      lineHeight = 32.sp,
                      fontFamily = FontFamily(Font(R.font.sf_pro_display_regular))))
        }
        false -> {}
      }

      Spacer(modifier = Modifier.height(16.dp))
      Text(
          "Already have an account?",
          style =
              TextStyle(
                  fontSize = 16.sp,
                  lineHeight = 32.sp,
                  fontFamily = FontFamily(Font(R.font.sf_pro_display_regular)),
              ),
          modifier =
              Modifier.testTag("LoginNavButton").clickable {
                loginViewModel.firebaseError.value = false
                loginViewModel.badCredentials.value = false
                navigationActions.navigateTo(Destinations.LOGIN)
              },
          color = MaterialTheme.colorScheme.primary,
          textDecoration = TextDecoration.Underline,
      )
    }
  }
}
