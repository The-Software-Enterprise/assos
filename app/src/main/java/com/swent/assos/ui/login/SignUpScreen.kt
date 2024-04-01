package com.swent.assos.ui.login

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.firebase.Firebase
import com.google.firebase.functions.functions
import com.swent.assos.model.view.LoginViewModel

@Preview
@Composable
fun SignUpScreen(navController: NavController) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    val loginViewModel: LoginViewModel = hiltViewModel()

    Column {
        OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("Email") })
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation()
        )
        OutlinedTextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            label = { Text("Confirm Password") },
            visualTransformation = PasswordVisualTransformation()
        )
        if (password != confirmPassword) {
            Text("Passwords do not match", color = Color.Red)
        }
        Button(
            onClick = {
                if (password == confirmPassword && password.isNotEmpty()) {
                    loginViewModel.signUp(email, password)

                    // call the firebasefunction -> oncallFind.py
                    val data = hashMapOf(
                        "email" to email
                    )
                    Firebase.functions
                        .getHttpsCallable("oncallFind")
                        .call(data)
                        .addOnSuccessListener { task ->
                            val result = task.data as String
                            println(result)
                            loginViewModel.updateUserInfo()
                        }
                        .addOnFailureListener {
                            println("Error")
                        }


                    loginViewModel.currentUser?.let {
                        navController.navigate("Home")
                    }
                }
            }) {
            Text("Sign Up")
        }

        Text("Already have an account?",
        modifier = Modifier.clickable {
            navController.navigate("Login")
        })
    }
}
