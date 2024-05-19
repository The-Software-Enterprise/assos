package com.swent.assos.model.view

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.functions.FirebaseFunctions
import com.google.firebase.ktx.Firebase
import com.swent.assos.config.Config
import com.swent.assos.model.data.DataCache
import com.swent.assos.model.data.User
import com.swent.assos.model.di.IoDispatcher
import com.swent.assos.model.service.AuthService
import com.swent.assos.model.service.DbService
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch

@HiltViewModel
class LoginViewModel
@Inject
constructor(
    private val dbService: DbService,
    private val accountService: AuthService,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {
  val currentUser = accountService.currentUser

  val firestoreInstance = Firebase.firestore

  var errorMessage = mutableStateOf("")
  var userNotFound = mutableStateOf(false)
  var badCredentials = mutableStateOf(false)
  var responseError = mutableStateOf("")
  var firebaseError = mutableStateOf(false)

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

  private fun callScrapping(email: String = "") {
    // call the firebasefunction -> oncallFind.py
    val data = hashMapOf("email" to email)
    // wait for user to be created
    val functions = FirebaseFunctions.getInstance("europe-west6")
    val config = Config()
    config.get_all { onlineServices ->
      val emu = onlineServices.contains("functions")
      if (emu) {
        functions.useEmulator("10.0.2.2", 5001)
      }
    }
    // change the region of the function to europe-west6
    functions.getHttpsCallable("oncallFind").call(data).addOnFailureListener {
      // add an empty user to the database with only the email
      val temp = User(email = email)
      viewModelScope.launch(ioDispatcher) { dbService.addUser(temp) }
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
          callScrapping(email)
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

  fun signOut() {
    DataCache.signOut()
    accountService.signOut()
  }
}
