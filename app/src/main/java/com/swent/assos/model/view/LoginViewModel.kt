package com.swent.assos.model.view

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.swent.assos.model.service.AuthService
import com.swent.assos.model.service.DbService
import dagger.hilt.android.lifecycle.HiltViewModel
import java.security.MessageDigest
import javax.inject.Inject

@HiltViewModel
class LoginViewModel
@Inject
constructor(private val storageService: DbService, private val accountService: AuthService) :
    ViewModel() {
  val currentUser = accountService.currentUser

  val firestoreInstance = Firebase.firestore

  var errorMessage = mutableStateOf("")
  var userNotFound = mutableStateOf(false)
  var badCredentials = mutableStateOf(false)
  var responseError = mutableStateOf("")
  var firebaseError = mutableStateOf(false)

  fun hashEmail(email: String): Int {
    val bytes = email.toByteArray()
    val md = MessageDigest.getInstance("SHA-256")
    val digest = md.digest(bytes)
    // get the integer value
    return digest.fold(0) { acc, byte -> acc * 256 + byte.toInt() }
  }

  // fun goToSignUp() = navController.navigate("SignUp")
  // fun goToSignIn() = navController.navigate("Login")

  fun signIn(email: String, password: String, callback: () -> Unit) {
    if (email.isEmpty() || password.isEmpty()) {
      errorMessage.value = "Please fill in all fields"
      userNotFound.value = true
      return
    }
    accountService.signIn(email, password).addOnCompleteListener {
      if (it.isSuccessful) {
        Log.d("LoginViewModel", "User signed in")
        callback()
      } else {
        errorMessage.value = it.exception?.message ?: "User not found, please sign up"
        userNotFound.value = true
        Log.e("LoginViewModel", "Error signing in")
      }
    }
  }

  fun signUp(
      email: String,
      password: String,
      confirmPassword: String,
      callback: (Boolean) -> Unit
  ) {
    if (password == confirmPassword && password.length >= 6 && email.isNotEmpty()) {
      accountService.signUp(email, password).addOnCompleteListener { task ->
        if (task.isSuccessful) {
          Log.d("LoginViewModel", "User signed up")
          responseError.value = ""
          callback(true)
        } else {
          Log.e("LoginViewModel", "Error signing up: ${task.exception}")
          firebaseError.value = true
          responseError.value = task.exception?.message ?: "Error signing up"
          callback(false)
        }
      }
    } else {
      badCredentials.value = true
    }
  }
}
