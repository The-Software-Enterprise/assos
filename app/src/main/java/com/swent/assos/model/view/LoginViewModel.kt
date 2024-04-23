package com.swent.assos.model.view

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.swent.assos.model.data.User
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

  var user = User("", "", "", "", emptyList(), emptyList())
  var userNotFound = mutableStateOf(false)
  var errorMessage = mutableStateOf("")
  var badCredentials = mutableStateOf(false)

  fun updateUserInfo() {

    val cuser = Firebase.auth.currentUser
    cuser?.let {
      val docRef = firestoreInstance.collection("users").document(it.uid)
      docRef
          .get()
          .addOnSuccessListener { document ->
            if (document != null) {
              user =
                  User(
                      document.id,
                      (document.data?.get("firstname") ?: "") as String,
                      (document.data?.get("name") ?: "") as String,
                      (document.data?.get("email") ?: "") as String,
                      (document.data?.get("associations")
                          ?: emptyList<Triple<String, String, Int>>())
                          as List<Triple<String, String, Int>>,
                      (document.data?.get("following") ?: emptyList<String>()) as List<String>)
            }
          }
          .addOnFailureListener { exception -> println("Error getting documents: $exception") }
    }
  }

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
      userNotFound.value = true
      errorMessage.value = "Please fill in all fields"
      return
    }
    accountService.signIn(email, password).addOnCompleteListener {
      if (it.isSuccessful) {
        updateUserInfo()
        callback()
      } else {
        errorMessage.value = it.exception?.message ?: ""
        userNotFound.value = true
        Log.e("LoginViewModel", "Error signing in")
      }
    }
  }

  fun signUp(email: String, password: String, confirmPassword: String) {
    if (password == confirmPassword && password.length >= 6 && email.isNotEmpty()) {
      accountService.signUp(email, password).addOnCompleteListener {
        if (it.isSuccessful) {
          updateUserInfo()
        } else {
          Log.e("LoginViewModel", "Error signing up")
        }
      }
    } else {
      badCredentials.value = true
    }
  }
}
